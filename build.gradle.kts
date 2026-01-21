import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask
import org.gradle.kotlin.dsl.register

plugins {
    id("java")
    `maven-publish`
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.pluginYml)
    alias(libs.plugins.server)
}

group = "fr.panncake.lootbox"
version = "1.0.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.paper)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    implementation(libs.boostedYaml)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks.named("shadowJar")) {
                classifier = ""
            }
            groupId = "fr.panncake"
            artifactId = "lootbox"
            version = project.version.toString()
        }
    }

    repositories {
        maven {
            name = "EuropawsArtifactory"
            url = uri("https://repo.europaws.eu/artifactory/plugins/")

            val user: String? = findProperty("artifactory_user") as? String
            val pass: String? = findProperty("artifactory_password") as? String

            if (user != null && pass != null) {
                credentials {
                    username = user
                    password = pass
                }
            }
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()

    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
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

tasks {
    shadowJar {
        archiveClassifier.set("")

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        configurations = listOf(project.configurations.compileClasspath.get())

        mergeServiceFiles()

        manifest {
            attributes(
                "Implementation-Title" to "PannLootbox",
                "Implementation-Version" to version
            )
        }
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