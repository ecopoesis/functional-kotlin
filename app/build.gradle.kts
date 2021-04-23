val arrowVersion = "0.13.1"

plugins {
    val kotlinVersion = "1.4.32"

    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion

    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:29.0-jre")

    // arrow
    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    implementation("io.arrow-kt:arrow-fx-coroutines:$arrowVersion")
    implementation("io.arrow-kt:arrow-optics:$arrowVersion")
    kapt("io.arrow-kt:arrow-meta:$arrowVersion")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // dependencies for Spek
    val junitVersion = "5.7.1"
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter", "junit-jupiter-api", junitVersion)
    testImplementation("org.spekframework.spek2", "spek-dsl-jvm", "2.0.15")
    testRuntimeOnly("org.spekframework.spek2", "spek-runner-junit5", "2.0.15")

    // kotlin.test
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.junit.platform", "junit-platform-engine", "1.7.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.3")
}

application {
    // Define the main class for the application.
    mainClass.set("org.miker.AppKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
