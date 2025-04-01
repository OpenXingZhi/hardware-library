plugins {
    alias(libs.plugins.shadow.jar)
    `java-library`
    `maven-publish`
}

version = "1.0.0"
description = "RFID Library for Java"

repositories {
    mavenCentral()
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar", "*.aar"), "dir" to "libs")))
}

tasks.shadowJar {
    archiveClassifier = ""
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/OpenXingZhi/hardware-library")
            credentials(PasswordCredentials::class)
        }
    }
    publications {
        create<MavenPublication>("shadow") {
            from(components["shadow"])
        }
    }
}