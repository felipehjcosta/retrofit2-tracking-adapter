import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getValue
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.2.10"
    id("org.mikeneck.junit.starter.normal") version "5.0.2"
}

val kotlinVersion: String? by extra {
    buildscript.configurations["classpath"]
            .resolvedConfiguration.firstLevelModuleDependencies
            .find { it.moduleName == "kotlin-gradle-plugin" }?.moduleVersion
}

repositories {
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8", kotlinVersion))
    compile("com.squareup.retrofit2:retrofit:2.3.0")

    testCompile("com.squareup.okhttp3:mockwebserver:3.9.1")
    testCompile("com.google.guava:guava:23.6-jre")
    testCompile("io.mockk:mockk:1.6")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}