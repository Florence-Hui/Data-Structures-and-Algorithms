plugins {
    kotlin("jvm") version "1.9.22" // or whatever version your project uses
    application
}

repositories {
    mavenCentral()
}

dependencies {
    // This provides kotlin.test annotations like @Test, assertEquals, etc.
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
