group = "click.seichi"
version = "0.1.0-alpha"

plugins {
    kotlin("jvm") version "2.1.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.21")
    }
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://eldonexus.de/repository/maven-public/") {
        name = "sonatype-nexus"
    }
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:2.1.21")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.1.21")
    implementation("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    implementation("com.fastasyncworldedit:FastAsyncWorldEdit-Core:2.9.2")
    implementation("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit:2.9.2")
}

val targetJavaVersion = 17
kotlin {
    jvmToolchain(targetJavaVersion)
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    build {
        dependsOn("shadowJar")
    }
    shadowJar {
        archiveFileName.set("region-grid-fitter-${project.version}.jar")
    }
}
