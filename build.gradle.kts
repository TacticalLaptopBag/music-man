plugins {
    kotlin("jvm") version "2.0.0"
    application
}

application {
    mainClass = "com.github.tacticallaptopbag.MainKt"
}

group = "com.github.tacticallaptopbag"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("com.github.ajalt.clikt:clikt:5.0.3")

    // optional support for rendering markdown in help messages
    // implementation("com.github.ajalt.clikt:clikt-markdown:5.0.3")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}