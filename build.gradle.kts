import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getValue
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.2.10"
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
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}