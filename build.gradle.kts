plugins {
    application

    // Update this if you change the Kotlin version in libs.versions.toml
    kotlin("jvm") version "1.9.20"
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

    maven {
        name = "Sonatype Snapshots"
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

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
}

application {
    mainClass.set("me.thecuddlybear.Bot.AppKt")
}