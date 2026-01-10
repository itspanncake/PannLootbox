import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.pluginYml)
}

group = "fr.panncake.lootbox"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.smartInvs)

    implementation(libs.boostedYaml)
}

paper {
    name = "PannLootbox"
    version = "${rootProject.version}"
    apiVersion = "1.21"

    website = "https://github.com/ItsPanncake/"
    author = "ItsPanncake"

    main = "fr.panncake.pannlootbox.PannLootbox"

    permissions {
        // No perms at the moment :3
    }
}