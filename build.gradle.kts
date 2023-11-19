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

    maven {
        name = "Sonatype Snapshots"
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kord.extensions)
    implementation(libs.slf4j)
    implementation("com.github.dotenv-org:dotenv-vault-kotlin:0.0.2")
}

application {
    mainClass.set("me.thecuddlybear.Bot.AppKt")
}