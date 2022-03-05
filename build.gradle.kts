import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    id("org.springframework.boot") version "2.6.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"

    // For MapStruct
    kotlin("kapt") version "1.3.72"
}

group = "me.theseems"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Migrations
    implementation("org.liquibase.ext:liquibase-hibernate5:4.7.1")
    implementation("org.liquibase:liquibase-core:4.7.1")

    runtimeOnly("org.postgresql:postgresql")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.5.0.Beta2")
    kapt("org.mapstruct:mapstruct-processor:1.5.0.Beta2")

    // Kotlin telegram SDK wrapper
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.0.6")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// For JPA compatibility with Kotlin
// https://habr.com/ru/company/haulmont/blog/572574/
allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Include migrations
sourceSets {
    main {
        resources {
            srcDirs("migrations")
            exclude("generated.yml")
        }
    }
}

// Generate changelog only after our entities are compiled
tasks.findByPath("diffChangeLog")?.apply {
    dependsOn("jar")
}

/**
 * Liquibase settings
 */
val liquibase = mutableMapOf(
    "referenceUrl" to
        "hibernate:spring:me.theseems.thrut.entity" +
        "?dialect=org.hibernate.dialect.PostgreSQL10Dialect" +
        "&hibernate.physical_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy" +
        "&hibernate.implicit_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy",
    "mainChangeLog" to "migrations/changelog.yml",
    "newChangeLog" to "migrations/generated.yml",
    "referenceDriver" to "liquibase.ext.hibernate.database.connection.HibernateDriver",
    "driver" to "org.postgresql.Driver",
    "url" to (System.getenv("spring.datasource.url") ?: "jdbc:postgresql://localhost/thrut"),
    "username" to (System.getenv("spring.datasource.username") ?: "postgres"),
    "password" to (System.getenv("spring.datasource.password") ?: "postgres")
)

tasks.register<JavaExec>("makemigrations") {
    classpath(sourceSets.main.get().runtimeClasspath)
    classpath(configurations.findByName("liquibase"))

    group = "liquibase"
    mainClass.set("liquibase.integration.commandline.Main")

    args = listOf(
        "--logLevel=info",
        "--changeLogFile=${liquibase["newChangeLog"]}",
        "--referenceUrl=${liquibase["referenceUrl"]}",
        "--url=${liquibase["url"]}",
        "--driver=${liquibase["driver"]}",
        "--username=${liquibase["username"]}",
        "--password=${liquibase["password"]}",
        "diffChangeLog"
    )
}

tasks.register<JavaExec>("applymigrations") {
    classpath(sourceSets.main.get().runtimeClasspath)
    classpath(configurations.findByName("liquibase"))

    group = "liquibase"
    mainClass.set("liquibase.integration.commandline.Main")

    args = listOf(
        "--logLevel=info",
        "--changeLogFile=${liquibase["mainChangeLog"]}",
        "--referenceUrl=${liquibase["referenceUrl"]}",
        "--url=${liquibase["url"]}",
        "--driver=${liquibase["driver"]}",
        "--username=${liquibase["username"]}",
        "--password=${liquibase["password"]}",
        "update"
    )
}

tasks.register<JavaExec>("diffmigrations") {
    classpath(sourceSets.main.get().runtimeClasspath)
    classpath(configurations.findByName("liquibase"))

    group = "liquibase"
    mainClass.set("liquibase.integration.commandline.Main")

    args = listOf(
        "--logLevel=info",
        "--changeLogFile=${liquibase["mainChangeLog"]}",
        "--referenceUrl=${liquibase["referenceUrl"]}",
        "--url=${liquibase["url"]}",
        "--driver=${liquibase["driver"]}",
        "--username=${liquibase["username"]}",
        "--password=${liquibase["password"]}",
        "diff"
    )
}
