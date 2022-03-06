plugins {
    application
    id("org.springframework.boot") version "2.6.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
    kotlin("kapt") version "1.3.72"
}

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

group = "me.theseems"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

application {
    mainClass.set("me.theseems.featbee.FeatbeeApplicationKt")
}
