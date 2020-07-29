/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 */

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72"

    // Apply the application plugin to add support for building a CLI application.
    application
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib", version = "1.3.72"))
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-runtime", "0.20.0") // JVM dependency
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.3.8")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor", "1.3.8")

    // Serialization
    implementation("com.charleskorn.kaml", "kaml", "0.18.1")

    // Database
    implementation("com.impossibl.pgjdbc-ng", "pgjdbc-ng", "0.8.3")
    implementation("org.jetbrains.exposed", "exposed-core", "0.24.1")
    implementation("org.jetbrains.exposed", "exposed-dao", "0.24.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.24.1")


    // Dependency Injection
    implementation("org.koin", "koin-core", "2.1.6")

    // JDA
    implementation("net.dv8tion", "JDA", "4.2.0_181") {
        exclude(module = "opus-java")
    }
    implementation("club.minnced", "jda-reactor", "1.2.0")
    implementation("dev.augu.nino", "Butterfly", "0.2.0")

    // Testing tools
    testImplementation("junit", "junit", "4.13")
    testImplementation("io.kotest", "kotest-runner-junit5-jvm", "4.1.3")
    testImplementation("io.kotest", "kotest-assertions-core-jvm", "4.1.3")
    testImplementation("io.kotest", "kotest-property-jvm", "4.1.3")
    testImplementation("io.mockk", "mockk", "1.10.0")
    testImplementation("org.jetbrains.kotlinx", "kotlinx-coroutines-test", "1.3.8")
    testImplementation("org.koin", "koin-test", "2.1.6")

    // Logging
    api("org.slf4j", "slf4j-api", "1.7.28")
    implementation("org.slf4j", "slf4j-simple", "1.7.28")
}

application {
    // Define the main class for the application.
    mainClassName = "dev.augu.nino.AppKt"
    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

tasks {
    compileKotlin {
        sourceSets {
            main {
                resources {
                    srcDir("src")
                }
            }
        }
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}