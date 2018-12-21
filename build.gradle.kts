import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getValue
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.novoda.gradle.release.PublishExtension

plugins {
    kotlin("jvm") version "1.3.10"
    id("org.mikeneck.junit.starter.normal") version "5.0.2"
    jacoco
    id("com.novoda.bintray-release") version "0.9"
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC12"
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
    compile("com.squareup.retrofit2:retrofit:2.5.0")

    testCompile("com.squareup.okhttp3:mockwebserver:3.12.0")
    testCompile("com.google.guava:guava:23.6-jre")
    testCompile("io.mockk:mockk:1.8.13.kotlin13")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

jacoco {
    toolVersion = "0.8.2"
}

tasks.withType<JacocoReport> {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
}

detekt {
    toolVersion = "1.0.0-RC12"
    input = files("src/main/kotlin")
    filters = ".*/resources/.*,.*/build/.*"
    config = files("${project.rootDir}/default-detekt-config.yml")
}

configure<PublishExtension> {
    userOrg = "fcostaa"
    groupId = "com.github.felipehjcosta"
    artifactId = "retrofit2-tracking-adapter"
    publishVersion = "1.0.0"
    desc = "A Retrofit 2 adapter for tracking responses"
    website = "https://github.com/felipehjcosta/retrofit2-tracking-adapter"
}

afterEvaluate {
    val junitPlatformTest: JavaExec by tasks
    jacoco {
        applyTo(junitPlatformTest)
    }
    task<JacocoReport>("jacocoJunit5TestReport") {
        executionData(junitPlatformTest)
        sourceSets(project.sourceSets["main"])
        sourceDirectories = files(project.sourceSets["main"].allSource.srcDirs)
        classDirectories = files(project.sourceSets["main"].output)
    }
}