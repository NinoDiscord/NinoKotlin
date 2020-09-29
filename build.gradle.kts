import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "6.0.0"
    kotlin("plugin.serialization") version "1.4.10"
    kotlin("jvm") version "1.4.10"
    application
}

val ver = Version(1, 0, 0)
group = "dev.augu"
version = ver.string()

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.9")

    // Serialization
    implementation("com.charleskorn.kaml:kaml:0.23.0")

    // Dependency Injection
    implementation("org.koin:koin-core:2.1.6")

    // JDA
    implementation("net.dv8tion:JDA:4.2.0_207") {
        exclude(module = "opus-java")
    }
    implementation("club.minnced:jda-reactor:1.2.0")
    implementation("dev.augu.nino:Butterfly:0.3.2")

    // Testing tools
    testImplementation("junit:junit:4.13")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.2.5")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.2.5")
    testImplementation("io.kotest:kotest-property-jvm:4.2.5")
    testImplementation("io.mockk:mockk:1.10.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.9")
    testImplementation("org.koin:koin-test:2.1.6")

    // Logging
    api("org.slf4j:slf4j-api:1.7.30")
    implementation("org.slf4j:slf4j-simple:1.7.30")
}

application {
    mainClassName = "dev.augu.nino.Bootstrap"
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

val shadowJar: ShadowJar by tasks

shadowJar.apply {
    archiveBaseName.set("Nino")
    archiveClassifier.set(null as String?)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

class Version(
        private val major: Int,
        private val minor: Int,
        private val revision: Int
) {
    fun string(): String = "$major.$minor${if (revision == 0) "" else ".$revision"}"
    fun commit(): String = "git rev-parse HEAD".shell()
}

fun String.shell(): String {
    val parts = this.split("\\s".toRegex())
    val process = ProcessBuilder(*parts.toTypedArray())
            .directory(File("."))
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

    process.waitFor(1, TimeUnit.MINUTES)
    return process
            .inputStream
            .bufferedReader()
            .readText()
            .trim()
            .slice(0..8)
}
