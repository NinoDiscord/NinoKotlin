import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "6.1.0"
    kotlin("plugin.serialization") version "1.4.10"
    id("com.diffplug.spotless") version "5.9.0"
    id("org.liquibase.gradle") version "2.0.4"
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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.2")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("com.charleskorn.kaml:kaml:0.27.0")

    // Dependency Injection
    implementation("org.koin:koin-core:2.2.2")
    implementation("org.koin:koin-ktor:2.2.2")

    // JDA
    implementation("net.dv8tion:JDA:4.2.0_227") {
        exclude(module = "opus-java")
    }
    implementation("club.minnced:jda-reactor:1.2.0")
    implementation("dev.augu.nino:Butterfly:0.3.5")

    // Redis
    implementation("io.lettuce:lettuce-core:6.0.2.RELEASE")

    // Postgres
    implementation("org.postgresql:postgresql:42.2.18")
    implementation("com.zaxxer:HikariCP:4.0.1")
    implementation("org.jetbrains.exposed:exposed-core:0.29.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.28.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.29.1")

    // MongoDB
    implementation("org.litote.kmongo:kmongo-coroutine-serialization:4.2.4")
    implementation("org.litote.kmongo:kmongo-id:4.2.4")

    // Liquibase (Database Migration)
    liquibaseRuntime("org.liquibase:liquibase-core:4.2.2")
    liquibaseRuntime("org.liquibase:liquibase-groovy-dsl:3.0.0")
    liquibaseRuntime("org.postgresql:postgresql:42.2.18")
    liquibaseRuntime("javax.xml.bind", "jaxb-api", "2.3.1")

    // Testing tools
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.4.0")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.4.0")
    testImplementation("io.kotest:kotest-property-jvm:4.4.0")
    testImplementation("io.kotest:kotest-extensions-koin-jvm:4.4.0")
    testImplementation("io.mockk:mockk:1.10.5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2")
    testImplementation("org.koin:koin-test:2.2.2")

    // Logging
    api("org.slf4j:slf4j-api:1.7.30")
    implementation("org.slf4j:slf4j-simple:1.7.30")

    // Ktor libraries (Internal API)
    implementation("io.ktor:ktor-client-serialization:1.5.1")
    implementation("io.ktor:ktor-serialization:1.5.1")
    implementation("io.ktor:ktor-server-netty:1.5.1")
    implementation("io.ktor:ktor-server-core:1.5.1")
}

application {
    mainClassName = "dev.augu.nino.Bootstrap"
    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

liquibase {
    activities {
        create("main") {
            if (project.extra.properties.containsKey("databaseJdbcUrl")) {
                val databaseJdbcUrl by project.extra.properties
                val databaseUsername by project.extra.properties
                val databasePassword by project.extra.properties
                arguments = mapOf(
                        "logLevel" to "info",
                        "changeLogFile" to "src/main/resources/db/changelog.sql",
                        "url" to databaseJdbcUrl,
                        "username" to databaseUsername,
                        "password" to databasePassword
                )
            }
        }
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

    build {
        dependsOn(shadowJar)
        dependsOn(spotlessApply)
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

spotless {
    kotlin {
        // Removes all trailing whitespace
        trimTrailingWhitespace()

        // End with a new line
        endWithNewline()

        // ktlint configuration
        //
        // Spotless won't load .editorconfig for the configuration
        // so it's specified here.
        // Read the issue here: https://github.com/diffplug/spotless/issues/142
        ktlint()
                .userData(mapOf(
                        // https://ktlint.github.io/#rule-blank
                        "no-consecutive-blank-lines" to "true",

                        // Disallows `Unit` in return statements
                        "no-unit-return" to "true",

                        // Disable the wildcards import and colon spacing rule
                        "disabled_rules" to "no-wildcard-imports,colon-spacing,import-ordering",

                        // Set the indent size to 4
                        "indent_size" to "4"
                ))
    }
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
