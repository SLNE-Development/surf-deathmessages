import dev.slne.surf.surfapi.gradle.util.registerSoft

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin") version "1.21.11+"
}

group = "dev.slne.surf.deathmessages"
version = "1.0-SNAPSHOT"

dependencies{
    compileOnly("dev.slne.surf.settings:surf-settings-api:1.21.11-2.0.0-SNAPSHOT")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.deathmessages.PaperMain")
    authors.add("Jo_field")

    generateLibraryLoader(false)
    foliaSupported(true)

    serverDependencies {
        registerSoft("surf-settings-paper")
    }
}