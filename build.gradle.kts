import dev.slne.surf.api.gradle.util.registerSoft

plugins {
    id("dev.slne.surf.api.gradle.paper-plugin") version "+"
}

group = "dev.slne.surf.deathmessages"
version = "1.1.0-SNAPSHOT"

dependencies {
    compileOnly("dev.slne.surf.settings:surf-settings-api:+")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.deathmessages.PaperMain")
    authors.add("Jo_field")

    generateLibraryLoader(false)
    foliaSupported(true)

    withSurfDatabaseR2dbc("1.3.0", "dev.slne.surf.deathmessages.libs")

    serverDependencies {
        registerSoft("surf-settings-paper")
    }
}