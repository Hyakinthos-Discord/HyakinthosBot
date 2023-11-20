plugins {
    application

    // Update this if you change the Kotlin version in libs.versions.toml
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
}

repositories {
    google()
    mavenCentral()

    maven {
        name = "Sonatype Snapshots (Legacy)"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }

    maven { url = uri("https://jitpack.io") }

    maven { url = uri("https://repo.hypixel.net/repository/Hypixel/") }

    maven { url = uri("https://eldonexus.de/repository/maven-public/") }

    maven { url = uri("https://m2.dv8tion.net/releases") }

    maven { url = uri("https://maven.arbjerg.dev/snapshots") }

    maven { url = uri("https://repo1.maven.org/maven2") }

    maven {
        name = "Sonatype Snapshots"
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

val ktor_version: String by project

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kord.extensions)
    implementation(libs.slf4j)
    implementation(libs.dotenv)
    implementation("com.github.ryanhcode:KoPixel:0.2")
    implementation("net.hypixel:hypixel-api:4.3")
    implementation("net.hypixel:hypixel-api-transport-apache:4.3")
    implementation("de.snowii:mojang-api:1.1.0")
    implementation("com.sedmelluq:lavaplayer:1.3.77")
    implementation("dev.schlaubi.lavakord:kord:5.1.7")
    implementation("dev.schlaubi.lavakord:sponsorblock:5.1.7")
    implementation("dev.schlaubi.lavakord:lavasrc:5.1.7")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.6")
    implementation("com.hexadevlabs:gpt4all-java-binding:1.1.5")
}

application {
    mainClass.set("me.thecuddlybear.Bot.AppKt")
}

tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources")) // We need this for Gradle optimization to work
        archiveClassifier.set("standalone") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}