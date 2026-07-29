# Death-Lookup Weltfilter — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** `/death lookup` zeigt ohne `--world` Tode aus allen Welten und mit `--world <name>` gezielt Tode einer beliebigen Welt, unabhängig davon, wo der ausführende Spieler steht.

**Architecture:** `DeathLookupFilter.worldName: String` wird zu `world: World?`, wobei `null` "alle Welten" bedeutet. Der Weltfilter wandert aus dem In-Memory-Sequence-Filter in die SQL-Query, damit das 500-Zeilen-Limit von `findAll` nicht Treffer aus selten benutzten Welten verschluckt. `parseDeathFilters` löst `--world` case-insensitive auf und bricht bei unbekanntem Namen mit einer Spielermeldung ab.

**Tech Stack:** Kotlin, Paper 1.21.x, CommandAPI 11.2.0, Exposed v1 R2DBC (geshadet unter `dev.slne.surf.database.libs.…`), Gradle Wrapper.

**Spec:** `docs/superpowers/specs/2026-07-29-lookup-world-filter-design.md`

## Global Constraints

- **Kein Test-Framework vorhanden.** Das Projekt hat kein `src/test`, keine Test-Dependency und keinen Test-Task. Dieser Plan folgt deshalb **nicht** dem üblichen TDD-Zyklus. Jede Task wird durch (a) einen erfolgreichen Compile und (b) einen exakt beschriebenen manuellen In-Game-Check verifiziert. Lege keine Test-Infrastruktur an — das ist nicht Teil dieses Auftrags.
- **Build-Kommando:** `./gradlew build` im Projektroot. Auf Windows/PowerShell `.\gradlew.bat build`.
- **Exposed ist geshadet.** Alle Exposed-Imports beginnen mit `dev.slne.surf.database.libs.org.jetbrains.exposed.v1.…`. Niemals `org.jetbrains.exposed.…` direkt importieren.
- **`Op.TRUE` existiert in dieser Exposed-Version nicht.** Der `Op.Companion` hat nur `build` und `nullOp` (per javap gegen `surf-database-r2dbc-2.3.1-all.jar` verifiziert). Bedingte WHERE-Klauseln müssen über einen nullable `Op<Boolean>` gebaut werden, nicht über einen Always-True-Platzhalter.
- **Commit-Stil:** Gitmoji-Prefix plus Conventional-Commit-Typ, wie in der Repo-History (`✨ feat(death): …`, `♻️ refactor(death): …`).
- **Branch:** `feat/lookup-world-filter-` (aktuell ausgecheckt).

---

### Task 1: Weltfilter in die SQL-Query von `DeathRepository`

Beide Query-Methoden bekommen einen optionalen `worldId`-Parameter. Durch den Default `null` ändert sich für bestehende Aufrufer nichts — diese Task ist für sich allein compilierbar und verhaltensneutral.

**Files:**
- Modify: `src/main/kotlin/dev/slne/surf/deathmessages/database/repository/DeathRepository.kt:35-43` (`findHistory`)
- Modify: `src/main/kotlin/dev/slne/surf/deathmessages/database/repository/DeathRepository.kt:62-69` (`findAll`)

**Interfaces:**
- Consumes: nichts aus früheren Tasks.
- Produces:
  - `suspend fun findHistory(playerUuid: UUID, worldId: UUID? = null): List<Death>`
  - `suspend fun findAll(worldId: UUID? = null, amount: Int = 500): List<Death>`

  In beiden Fällen bedeutet `worldId == null` "keine Welt-Einschränkung".

- [ ] **Step 1: Import für `and` ergänzen**

In `DeathRepository.kt` neben den bestehenden Exposed-Core-Imports einfügen:

```kotlin
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.and
```

Der Import gehört alphabetisch vor `…v1.core.eq` (Zeile 7).

- [ ] **Step 2: `findHistory` um den Weltfilter erweitern**

Ersetze die bestehende `findHistory`-Funktion (Zeile 35-43) vollständig durch:

```kotlin
    suspend fun findHistory(playerUuid: UUID, worldId: UUID? = null): List<Death> =
        suspendTransaction {
            val playerCondition = DeathsTable.playerId eq playerUuid
            val condition = if (worldId == null) {
                playerCondition
            } else {
                playerCondition.and(DeathsTable.worldId eq worldId)
            }

            DeathsTable
                .selectAll()
                .where(condition)
                .orderBy(DeathsTable.id, SortOrder.DESC)
                .map { it.toDeath() }
                .toList()
        }
```

`Query` hat zwei `where`-Overloads: einen mit Lambda und einen, der direkt ein `Op<Boolean>` nimmt. Hier wird der zweite benutzt, weil die Bedingung vorher zusammengebaut wird.

`and` wird bewusst mit Punktnotation (`playerCondition.and(...)`) statt infix aufgerufen — das funktioniert unabhängig davon, ob die Funktion infix deklariert ist.

- [ ] **Step 3: `findAll` um den Weltfilter erweitern**

Ersetze die bestehende `findAll`-Funktion (Zeile 62-69) vollständig durch:

```kotlin
    suspend fun findAll(worldId: UUID? = null, amount: Int = 500): List<Death> =
        suspendTransaction {
            val condition = worldId?.let { DeathsTable.worldId eq it }

            DeathsTable.selectAll()
                .let { query -> if (condition == null) query else query.where(condition) }
                .orderBy(DeathsTable.diedAt to SortOrder.DESC)
                .limit(amount)
                .map { it.toDeath() }
                .toList()
        }
```

Das `let` statt eines `apply` ist Absicht: `Query.where` gibt die Query zurück, und diese Form ist unabhängig davon korrekt, ob `where` die Query mutiert oder eine neue liefert.

Wichtig: `amount` bleibt der **zweite** Parameter. Bestehende Aufrufer rufen `findAll()` ohne Argumente auf, das bleibt gültig.

- [ ] **Step 4: Compile prüfen**

Run: `./gradlew build`
Expected: `BUILD SUCCESSFUL`. Keine Verhaltensänderung — dieser Schritt prüft nur, dass die Exposed-API korrekt getroffen wurde.

- [ ] **Step 5: Commit**

```bash
git add src/main/kotlin/dev/slne/surf/deathmessages/database/repository/DeathRepository.kt
git commit -m "♻️ refactor(death): allow filtering death queries by world id"
```

---

### Task 2: `DeathLookupFilter` auf optionale Welt umstellen

Diese Task macht den Default "alle Welten". `--world` wird noch **nicht** gelesen — das ist Task 3. Filter-Datenklasse, Service und Parser müssen zusammen geändert werden, sonst compiliert nichts.

**Files:**
- Modify: `src/main/kotlin/dev/slne/surf/deathmessages/command/subcommand/LookupCommand.kt:188-205` (`DeathLookupFilter`)
- Modify: `src/main/kotlin/dev/slne/surf/deathmessages/command/subcommand/LookupCommand.kt:176-185` (Rückgabe von `parseDeathFilters`)
- Modify: `src/main/kotlin/dev/slne/surf/deathmessages/database/service/DeathLookupService.kt:9-33`

**Interfaces:**
- Consumes: `DeathRepository.findHistory(playerUuid, worldId)` und `DeathRepository.findAll(worldId, amount)` aus Task 1.
- Produces:
  - `DeathLookupFilter` mit dem Feld `val world: World?` **anstelle von** `val worldName: String`. Die Feldreihenfolge bleibt sonst unverändert, `world` steht an derselben Position wie vorher `worldName` (nach `centerZ`, vor `limit`).
  - `DeathLookupFilter.empty(player: Player)` liefert `world = null`.

- [ ] **Step 1: Import für `World` ergänzen**

In `LookupCommand.kt` neben `import org.bukkit.entity.Player` (Zeile 23) einfügen:

```kotlin
import org.bukkit.World
```

`org.bukkit.World` gehört alphabetisch vor `org.bukkit.entity.Player`.

- [ ] **Step 2: `DeathLookupFilter` umstellen**

Ersetze die Datenklasse (Zeile 188-205) vollständig durch:

```kotlin
data class DeathLookupFilter(
    val playerUuid: UUID?,
    val after: OffsetDateTime?,
    val radius: Double?,
    val centerX: Double,
    val centerY: Double,
    val centerZ: Double,
    val world: World?,
    val limit: Int
) {
    companion object {
        fun empty(player: Player) = DeathLookupFilter(
            null, null, null,
            player.location.x, player.location.y, player.location.z,
            null, 50
        )
    }
}
```

`world = null` bedeutet: keine Welt-Einschränkung, es wird über alle Welten gesucht.

- [ ] **Step 3: Rückgabe von `parseDeathFilters` anpassen**

Ersetze das `return DeathLookupFilter(...)` am Ende von `parseDeathFilters` (Zeile 176-185) durch:

```kotlin
    return DeathLookupFilter(
        playerUuid = playerUuid,
        after = after,
        radius = radius,
        centerX = player.location.x,
        centerY = player.location.y,
        centerZ = player.location.z,
        world = if (radius != null) player.world else null,
        limit = this["--limit"]?.toIntOrNull() ?: 50
    )
```

`radius != null -> player.world`: Eine Umkreissuche bleibt an eine Welt gebunden, weil dieselben XYZ-Koordinaten in Overworld, Nether und End nichts miteinander zu tun haben und sonst alle als Treffer im selben Umkreis auftauchen würden. Ohne `--radius` wird nicht eingeschränkt.

- [ ] **Step 4: `DeathLookupService` auf den SQL-Filter umstellen**

Ersetze den Rumpf von `lookup` in `DeathLookupService.kt` (Zeile 9-33) vollständig durch:

```kotlin
    suspend fun lookup(filter: DeathLookupFilter): List<Death> {
        val worldId = filter.world?.uid

        val source = if (filter.playerUuid != null) {
            DeathRepository.findHistory(filter.playerUuid, worldId)
        } else {
            DeathRepository.findAll(worldId)
        }

        return source
            .asSequence()
            .filter { filter.after == null || it.diedAt.isAfter(filter.after) }
            .filter {
                if (filter.radius == null) true
                else {
                    val loc = it.location
                    val distanceSq = (loc.x - filter.centerX).pow(2) +
                            (loc.y - filter.centerY).pow(2) +
                            (loc.z - filter.centerZ).pow(2)
                    distanceSq <= filter.radius.pow(2)
                }
            }
            .sortedByDescending { it.diedAt }
            .take(filter.limit)
            .toList()
    }
```

Der bisherige In-Memory-Vergleich `.filter { it.location.world.name == filter.worldName }` entfällt ersatzlos — die Einschränkung passiert jetzt in der Query. Das ist der Punkt der Änderung: `findAll` lädt nur 500 Zeilen, und ein nachgelagerter Filter würde Treffer aus einer selten benutzten Welt verschlucken, sobald 500 neuere Tode aus anderen Welten davorliegen.

- [ ] **Step 5: Compile prüfen**

Run: `./gradlew build`
Expected: `BUILD SUCCESSFUL`.

Falls der Compiler `worldName` noch irgendwo bemängelt: das Feld wird ausschließlich in diesen beiden Dateien benutzt (per Grep verifiziert), es gibt keine weiteren Call-Sites.

- [ ] **Step 6: In-Game verifizieren**

Plugin bauen, auf den Testserver deployen, dann:

1. Sorge dafür, dass in mindestens zwei Welten Tode existieren (z. B. in Overworld und Nether je einmal sterben).
2. Stelle dich in die Overworld.
3. `/death lookup`

Expected: Die Ergebnisliste enthält Einträge aus **beiden** Welten. Der Weltname steht in Klammern hinter den Koordinaten — der Row-Renderer gibt ihn bereits aus (`LookupCommand.kt:82`), daran ist nichts zu ändern.

4. `/death lookup --radius 50`

Expected: Nur Tode aus der Welt, in der du stehst.

- [ ] **Step 7: Commit**

```bash
git add src/main/kotlin/dev/slne/surf/deathmessages/command/subcommand/LookupCommand.kt src/main/kotlin/dev/slne/surf/deathmessages/database/service/DeathLookupService.kt
git commit -m "✨ feat(death): search deaths across all worlds by default"
```

---

### Task 3: `--world` parsen und unbekannte Welten abweisen

**Files:**
- Modify: `src/main/kotlin/dev/slne/surf/deathmessages/command/subcommand/LookupCommand.kt:114-150` (Executor-Body)
- Modify: `src/main/kotlin/dev/slne/surf/deathmessages/command/subcommand/LookupCommand.kt:155-186` (`parseDeathFilters`)

**Interfaces:**
- Consumes: `DeathLookupFilter` mit `world: World?` aus Task 2.
- Produces:
  - `private suspend fun Map<String, String>.parseDeathFilters(player: Player): DeathLookupFilter?` — Rückgabetyp ist jetzt **nullable**. `null` bedeutet: die Eingabe war ungültig, dem Spieler wurde bereits eine Fehlermeldung geschickt, der Aufrufer muss abbrechen.
  - `private fun resolveWorld(name: String): World?`

- [ ] **Step 1: Import für `server` ergänzen**

In `LookupCommand.kt` bei den `dev.slne.surf.api`-Imports einfügen:

```kotlin
import dev.slne.surf.api.paper.extensions.server
```

Das ist dieselbe Extension, die `DeathRepository.kt:4` bereits benutzt.

- [ ] **Step 2: `resolveWorld`-Helper anlegen**

Direkt über `parseDeathFilters` (also vor Zeile 155, unter der `rangeRegex`-Deklaration) einfügen:

```kotlin
private fun resolveWorld(name: String): World? =
    server.getWorld(name) ?: server.worlds.firstOrNull { it.name.equals(name, ignoreCase = true) }
```

Erst der exakte Treffer, dann der case-insensitive Fallback. Damit funktioniert sowohl `--world world_nether` als auch `--world WORLD_NETHER`.

Es werden ausschließlich echte Bukkit-Weltnamen akzeptiert. Baue **keine** Aliase wie `nether` oder `end` ein — über `World.Environment` wären die mehrdeutig, sobald der Server mehrere Welten desselben Environments betreibt.

- [ ] **Step 3: `--world` in `parseDeathFilters` auswerten**

Ersetze `parseDeathFilters` vollständig (von der Signatur bis zur schließenden Klammer, Zeile 155-186) durch:

```kotlin
private suspend fun Map<String, String>.parseDeathFilters(player: Player): DeathLookupFilter? {
    val playerUuid = this["--player"]?.let { PlayerLookupService.getUuid(it) }

    val radius = (this["--radius"] ?: this["--range"])?.toDoubleOrNull()

    val after = this["--time"]?.let { rangeStr ->
        val match = rangeRegex.find(rangeStr.trim()) ?: return@let null
        val (valueStr, unit) = match.destructured
        val value = valueStr.toLongOrNull() ?: return@let null

        val seconds = when (unit.lowercase()) {
            "s" -> value
            "m" -> value * 60
            "h" -> value * 3600
            "d" -> value * 86400
            "w" -> value * 604800
            else -> 0L
        }
        OffsetDateTime.now().minusSeconds(seconds)
    }

    val requestedWorld = this["--world"]
    val world = when {
        requestedWorld != null -> resolveWorld(requestedWorld) ?: run {
            player.sendText {
                appendErrorPrefix()
                error("Die Welt ")
                variableValue(requestedWorld)
                error(" existiert nicht.")
                appendNewline()
                info("Verfügbare Welten: ")
                variableValue(server.worlds.joinToString(", ") { it.name })
            }
            return null
        }

        radius != null -> player.world
        else -> null
    }

    return DeathLookupFilter(
        playerUuid = playerUuid,
        after = after,
        radius = radius,
        centerX = player.location.x,
        centerY = player.location.y,
        centerZ = player.location.z,
        world = world,
        limit = this["--limit"]?.toIntOrNull() ?: 50
    )
}
```

Die Reihenfolge im `when` ist bedeutsam: ein explizites `--world` gewinnt immer, auch in Kombination mit `--radius`. `--world world_nether --radius 50` sucht also im Umkreis der eigenen Koordinaten, aber innerhalb des Nether.

Der Abbruch läuft bewusst über `return null` plus Spielermeldung statt über einen CommandAPI-Exception-Throw: die Meldung soll optisch zum übrigen Command-Feedback passen, das durchgängig `appendErrorPrefix()` / `error()` benutzt (siehe `LookupCommand.kt:127-130`).

Ein unbekannter Weltname bricht ab, statt still 0 Treffer zu liefern — sonst wäre ein Tippfehler nicht von "es gibt wirklich keine Tode" zu unterscheiden.

- [ ] **Step 4: Executor an den nullable Rückgabewert anpassen**

Ersetze in `playerExecutorSuspend` die Zeile

```kotlin
        val filter = query?.parseDeathFilters(player) ?: DeathLookupFilter.empty(player)
```

durch:

```kotlin
        val queryMap = query
        val filter = if (queryMap == null) {
            DeathLookupFilter.empty(player)
        } else {
            queryMap.parseDeathFilters(player) ?: return@playerExecutorSuspend
        }
```

Zwei Gründe für diese Form:

1. Der Elvis-Operator geht nicht mehr, weil `null` jetzt zwei verschiedene Dinge bedeuten könnte — "kein Query-Argument angegeben" und "Query war ungültig". Die müssen unterschiedlich behandelt werden.
2. `query` ist eine **delegierte** Property (`val query: Map<String, String>? by args`). Auf delegierte Properties greift kein Smart-Cast, deshalb wird der Wert vorher in das lokale `queryMap` gelesen.

- [ ] **Step 5: Compile prüfen**

Run: `./gradlew build`
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 6: In-Game verifizieren**

Plugin bauen und deployen, dann in der **Overworld** stehend durchgehen:

| Kommando | Erwartung |
| --- | --- |
| `/death lookup --world world_nether` | nur Nether-Tode, obwohl du in der Overworld stehst |
| `/death lookup --world WORLD_NETHER` | identisches Ergebnis (case-insensitive) |
| `/death lookup --world quatsch` | Fehlermeldung „Die Welt quatsch existiert nicht." plus Liste der verfügbaren Welten, keine Ergebnisliste |
| `/death lookup --world world_nether --radius 50` | Umkreissuche um deine Koordinaten, aber nur Nether-Treffer |
| `/death lookup --player <name> --world world_nether` | nur Nether-Tode dieses Spielers |
| `/death lookup` | weiterhin Tode aus allen Welten |

- [ ] **Step 7: Commit**

```bash
git add src/main/kotlin/dev/slne/surf/deathmessages/command/subcommand/LookupCommand.kt
git commit -m "✨ feat(death): add --world filter to death lookup"
```

---

### Task 4: Nebenfix — doppelte Flag-Werte erlauben

Eigenständiger Bug, unabhängig vom Weltfilter, aber in derselben Zeile. Deshalb eigener Commit.

**Files:**
- Modify: `src/main/kotlin/dev/slne/surf/deathmessages/command/subcommand/LookupCommand.kt:110`

**Interfaces:**
- Consumes: nichts.
- Produces: nichts. Reine Verhaltensänderung am Argument-Parser.

- [ ] **Step 1: `withoutValueList` mit `allowDuplicates = true` aufrufen**

Ersetze in der `optionalArgument`-Deklaration die Zeile

```kotlin
            .withoutValueList()
```

durch:

```kotlin
            .withoutValueList(true)
```

Hintergrund: `withoutValueList()` delegiert an `withValueList(null, false)` (`MapArgumentBuilder.java:171` → `:144`), wobei das `false` `allowValueDuplicates` ist. Beim Parsen landet jeder Wert in einem Set; ist er schon enthalten, bricht der Befehl mit `"Duplicate values are not allowed!"` ab (`MapArgument.java:289` → `:416`). Praktische Folge: `/death lookup --page 2 --limit 2` wird abgelehnt, weil beide Flags den Wert `2` tragen.

Das `true` schaltet ausschließlich die Duplikatsprüfung für **Werte** ab. Doppelte **Keys** bleiben weiterhin verboten, was korrekt ist — eine Map kann `--page` nicht zweimal enthalten.

- [ ] **Step 2: Compile prüfen**

Run: `./gradlew build`
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 3: In-Game verifizieren**

| Kommando | Erwartung |
| --- | --- |
| `/death lookup --page 2 --limit 2` | wird angenommen, liefert Seite 2 mit Limit 2 — **vorher** roter Brigadier-Syntaxfehler |
| `/death lookup --radius 50 --limit 50` | wird angenommen |
| `/death lookup --page 1 --page 2` | wird weiterhin abgelehnt (doppelter Key) |

- [ ] **Step 4: Commit**

```bash
git add src/main/kotlin/dev/slne/surf/deathmessages/command/subcommand/LookupCommand.kt
git commit -m "🐛 fix(death): allow duplicate flag values in lookup query"
```

---

## Abschluss

Nach Task 4 ist der Branch fertig. Führe die Verifikationstabellen aus Task 3 Step 6 und Task 4 Step 3 noch einmal am Stück durch, bevor du einen PR aufmachst — die späteren Tasks fassen dieselbe Datei an wie die früheren.

Bewusst nicht Teil dieses Plans:

- Tab-Completion für `--world`. `MapArgumentBuilder` kennt nur eine globale Value-List für alle Keys, und diese Liste wird erzwungen: jeder Wert, der nicht drinsteht, wird abgelehnt (`MapArgument.java:282`). Weltnamen dort einzutragen würde `--player` und `--limit` unbrauchbar machen. Discovery läuft über die Fehlermeldung aus Task 3.
- Verschieben der übrigen Filter (`--time`, `--radius`, `--limit`) in die SQL-Query. Bestehendes Verhalten, außerhalb des Scopes.
