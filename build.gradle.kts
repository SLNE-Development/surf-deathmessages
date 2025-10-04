plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

group = "dev.slne.surf.deathmessages"
version = "1.0-SNAPSHOT"

surfPaperPluginApi {
    mainClass("dev.slne.surfDeathMessagesNew.SurfDeathMessagesNew")
    authors.add("Jo_field")

    generateLibraryLoader(false)
    foliaSupported(true)
}