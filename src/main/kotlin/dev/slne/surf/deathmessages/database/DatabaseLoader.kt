package dev.slne.surf.deathmessages.database

import dev.slne.surf.database.DatabaseApi
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.SchemaUtils
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import dev.slne.surf.deathmessages.database.table.DeathsTable
import java.nio.file.Path

val databaseLoader = DatabaseLoader()

class DatabaseLoader {
    lateinit var databaseApi: DatabaseApi

    fun connect(dataPath: Path) {
        databaseApi = DatabaseApi.create(dataPath)
    }
    suspend fun createTables() {
        suspendTransaction {
            SchemaUtils.create(DeathsTable)
        }

    }
    fun disconnect() {
        databaseApi.shutdown()
    }
}