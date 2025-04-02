plugins {
    alias(libs.plugins.shadow.jar)
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    `maven-publish`
}

version = "1.0.3"
description = "RFID Library for Java"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
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
