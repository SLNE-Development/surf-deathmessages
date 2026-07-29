# Death-Lookup: Weltfilter über `--world`

**Datum:** 2026-07-29
**Branch:** `feat/lookup-world-filter`

## Problem

`/death lookup` zeigt ausschließlich Tode aus der Welt, in der der ausführende
Spieler gerade steht. Wer aus der Overworld einen Tod im Nether nachschlagen
will, muss vorher in den Nether wechseln.

Ursache: `parseDeathFilters` setzt `worldName` hart auf `player.world.name`
(`LookupCommand.kt:183`), und `DeathLookupService.lookup` filtert bedingungslos
darauf (`DeathLookupService.kt:18`). Der Key `--world` steht bereits in der
Key-List des `MapArgument` (`LookupCommand.kt:109`), wird aber nirgends gelesen.

## Ziel

- Ohne `--world` werden Tode aus **allen** Welten angezeigt.
- Mit `--world <name>` wird auf genau diese Welt eingeschränkt, unabhängig davon,
  wo der ausführende Spieler steht.
- `--radius` bleibt an genau eine Welt gebunden.

## Design

### Filter-Modell

`DeathLookupFilter.worldName: String` wird zu `world: World?`.

Ein Bukkit-`World` statt eines Strings, weil daraus sowohl `uid` (für den
SQL-Filter) als auch `name` (für Meldungen) verfügbar ist. `null` bedeutet
"alle Welten".

`DeathLookupFilter.empty(player)` setzt `world = null`. Der Default ohne jegliche
Flags ist damit serverweit.

Die Center-Koordinaten (`centerX/Y/Z`) bleiben unverändert; sie sind ohnehin nur
relevant, wenn `radius != null`.

### Parsing (`parseDeathFilters`)

| Eingabe                          | Ergebnis                                              |
| -------------------------------- | ----------------------------------------------------- |
| `--world <name>`                 | die Welt, case-insensitive gematcht                    |
| kein `--world`, aber `--radius`  | `player.world` — Umkreissuche bleibt an eine Welt gebunden |
| weder `--world` noch `--radius`  | `null` → alle Welten                                   |
| `--world <unbekannt>`            | Fehlermeldung mit verfügbaren Welten, Suche bricht ab  |

Auflösung des Weltnamens: `Bukkit.getWorld(name)`, bei `null` Fallback auf
`server.worlds.firstOrNull { it.name.equals(name, ignoreCase = true) }`.

**Warum `--radius` ohne `--world` die aktuelle Welt impliziert:** Eine
Umkreissuche über Weltgrenzen hinweg ist inhaltlich sinnlos — dieselben
XYZ-Koordinaten in Overworld, Nether und End haben nichts miteinander zu tun und
würden als Treffer im selben Umkreis erscheinen.

**Fehlerbehandlung:** `parseDeathFilters` bekommt einen nullable Rückgabewert.
Die Funktion sendet die Fehlermeldung selbst über das im File etablierte
`appendErrorPrefix()` / `error()`-Pattern; der Executor macht bei `null` ein
`return@playerExecutorSuspend`. Bewusst kein CommandAPI-Exception-Throw — das
würde optisch aus dem Rahmen des übrigen Command-Feedbacks fallen.

Ein unbekannter Weltname bricht ab, statt still 0 Treffer zu liefern: sonst ist
ein Tippfehler nicht von "wirklich keine Tode" zu unterscheiden.

### Repository: Weltfilter gehört in die Query

`DeathRepository.findAll()` lädt die 500 neuesten Tode serverweit
(`DeathRepository.kt:62`); erst danach filtert `DeathLookupService` in-memory auf
die Welt. Bliebe das so, würde `--world nether` weiterhin "keine Tode gefunden"
liefern, sobald 500 neuere Tode aus anderen Welten davorliegen — derselbe
sichtbare Fehler wie bisher, nur mit anderer Ursache. Der Weltfilter muss deshalb
auf DB-Ebene greifen:

- `findAll(worldId: UUID? = null, amount: Int = 500)` — optionales
  `.where { DeathsTable.worldId eq worldId }`
- `findHistory(playerUuid: UUID, worldId: UUID? = null)` analog
- `DeathLookupService.lookup` reicht `filter.world?.uid` durch und lässt den
  In-Memory-Weltvergleich ersatzlos fallen

`DeathsTable.worldId` ist bereits eine eigene Spalte (`DeathsTable.kt:12`), es
sind keine Schema-Änderungen nötig.

### Anzeige

Unverändert. Der Row-Renderer gibt den Weltnamen bereits pro Zeile aus
(`spacer("(${record.location.world.name})")`, `LookupCommand.kt:82`),
weltübergreifende Ergebnisse sind damit ohne Anpassung lesbar.

## Betroffene Dateien

- `command/subcommand/LookupCommand.kt`
- `database/service/DeathLookupService.kt`
- `database/repository/DeathRepository.kt`

`DeathLookupFilter` wird außerhalb dieser Dateien nicht verwendet (per Grep
verifiziert), es gibt also keine weiteren Call-Sites.

## Verifikation

Das Projekt hat kein Test-Verzeichnis (`src/test` existiert nicht) und keine
Test-Infrastruktur. Verifikation erfolgt über:

1. Gradle-Build (Compile) muss durchlaufen.
2. Manueller Test im Spiel:
   - `/death lookup` in der Overworld zeigt Tode aus Nether und End mit.
   - `/death lookup --world world_nether` aus der Overworld heraus zeigt
     Nether-Tode.
   - `/death lookup --world WORLD_NETHER` funktioniert ebenfalls
     (case-insensitive).
   - `/death lookup --world quatsch` liefert die Fehlermeldung mit der
     Weltenliste.
   - `/death lookup --radius 50` beschränkt weiterhin auf die aktuelle Welt.
   - `/death lookup --world world_nether --radius 50` sucht im Umkreis der
     eigenen Koordinaten, aber innerhalb des Nether.

## Bewusst nicht enthalten

- Wert-Vorschläge (Tab-Completion) für `--world`: `MapArgumentBuilder`
  unterstützt nur eine globale Value-List für alle Keys, nicht pro Key.
- Verschieben der übrigen Filter (`--time`, `--radius`, `--limit`) in die
  SQL-Query. Bestehendes Verhalten, außerhalb des Scopes dieser Änderung.
