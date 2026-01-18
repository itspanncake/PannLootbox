import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask
import org.gradle.kotlin.dsl.register

plugins {
    id("java")
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.pluginYml)
    alias(libs.plugins.server)
}

group = "fr.panncake.lootbox"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.paper)

    implementation(libs.boostedYaml)
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        relocate("dev.dejvokep.boostedyaml", "fr.panncake.lootbox.libs")

        minimize()
    }

    build {
        dependsOn(shadowJar)
    }
}

tasks.register<LaunchMinecraftServerTask>("server") {
    dependsOn("shadowJar")

    doFirst {
        copy {
            val jarName = tasks.shadowJar.get().archiveFileName.get()
            from(layout.buildDirectory.file("libs/$jarName"))
            into(layout.buildDirectory.asFile.get().resolve("MinecraftServer/plugins"))
        }
    }

    jarUrl.set(LaunchMinecraftServerTask.JarUrl.Paper("1.21.5"))
    agreeEula.set(true)
}

paper {
    main = "${project.group}.PannLootbox"

    generateLibrariesJson = true

    apiVersion = "1.21"
    version = "${project.version}"

    authors = listOf("ItsPanncake")

    permissions {
        register("pannlootbox.*") {
            childrenMap = mapOf(
                "pannlootbox.commands.reload" to true
            )
        }
        register("pannlootbox.commands.reload") {
            description = "Allows you to reload the plugin"
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}