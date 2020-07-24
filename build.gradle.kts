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
    val kotlinVersion = "1.3.72"
    val kotlinxSerializationVersion = "0.20.0"
    val kotlinxCoroutinesVersion = "1.3.8"

    implementation(kotlin("stdlib", version = kotlinVersion))
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-runtime", kotlinxSerializationVersion) // JVM dependency
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", kotlinxCoroutinesVersion)
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor", kotlinxCoroutinesVersion)

    // Serialization
    val kamlVersion = "0.18.1"

    implementation("com.charleskorn.kaml", "kaml", kamlVersion)


    // Dependency Injection
    val koinVersion = "2.1.6"

    implementation("org.koin", "koin-core", koinVersion)

    // JDA
    val jdaVersion = "4.2.0_181"
    val jdaReactorVersion = "1.2.0"
    val butterflyVersion = "0.1.1"

    implementation("net.dv8tion", "JDA", jdaVersion) {
        exclude(module = "opus-java")
    }
    implementation("club.minnced", "jda-reactor", jdaReactorVersion)
    implementation("dev.augu.nino", "Butterfly", butterflyVersion)

    // Testing tools
    val junitVersion = "4.13"
    val kotestVersion = "4.1.3"
    val mockkVersion = "1.10.0"

    testImplementation("junit", "junit", junitVersion)
    testImplementation("io.kotest", "kotest-runner-junit5-jvm", kotestVersion)
    testImplementation("io.kotest", "kotest-assertions-core-jvm", kotestVersion)
    testImplementation("io.kotest", "kotest-property-jvm", kotestVersion)
    testImplementation("io.mockk", "mockk", mockkVersion)
    testImplementation("org.jetbrains.kotlinx", "kotlinx-coroutines-test", kotlinxCoroutinesVersion)
    testImplementation("org.koin", "koin-test", koinVersion)

    // Logging
    val slf4jVersion = "1.7.28"
    api("org.slf4j", "slf4j-api", slf4jVersion)
    implementation("org.slf4j", "slf4j-simple", slf4jVersion)
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