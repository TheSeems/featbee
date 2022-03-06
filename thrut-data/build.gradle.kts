plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("kapt")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "me.theseems"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Migrations
    implementation("org.liquibase.ext:liquibase-hibernate5:4.7.1")
    implementation("org.liquibase:liquibase-core:4.7.1")

    runtimeOnly("org.postgresql:postgresql")
}

// For JPA compatibility with Kotlin
// https://habr.com/ru/company/haulmont/blog/572574/
allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
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
tasks.findByPath("makemigrations")?.apply {
    dependsOn("jar")
    doLast {
        project.projectDir.resolve("derby.log").delete()
    }
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

tasks.findByName("bootJar")?.apply {
    enabled = false
}