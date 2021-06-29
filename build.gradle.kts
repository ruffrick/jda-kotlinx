import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
    kotlin("jvm") version "1.5.20"

    `maven-publish`
}

group = "dev.ruffrick"
version = "0.1"

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
}

dependencies {
    // https://github.com/DV8FromTheWorld/JDA/
    compileOnly("net.dv8tion:JDA:4.3.0_287")

    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val sourcesJar = task<Jar>("sourcesJar") {
    from(sourceSets["main"].allSource)
    archiveClassifier.set("sources")
}

tasks {
    build {
        dependsOn(sourcesJar)
        dependsOn(jar)
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = project.group as String
            artifactId = project.name
            version = project.version as String

            from(components["kotlin"])
            artifact(sourcesJar)
        }
    }
}
