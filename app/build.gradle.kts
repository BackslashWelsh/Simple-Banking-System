/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.1/userguide/building_java_projects.html
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    id("org.sonarqube") version "3.3"
}

sonarqube {
    properties {
        property ("sonar.projectKey", "BackslashWelsh_Simple-Banking-System")
        property ("sonar.organization", "backslash-welsh")
        property ("sonar.host.url", "https://sonarcloud.io")
    }
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")

    // test dependency for hyperskill tests
    testImplementation("com.github.hyperskill:hs-test:release-SNAPSHOT")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:30.1-jre")

    implementation("org.xerial:sqlite-jdbc:3.8.11.2")

}

application {
    // Define the main class for the application.
    mainClass.set("bank.Main")
}

tasks.test {
    systemProperty("file.encoding", "utf-8")
    outputs.upToDateWhen { false }
//    // Use JUnit Platform for unit tests.
//    useJUnitPlatform()
}
