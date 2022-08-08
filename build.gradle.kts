@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(tools.plugins.kotlin.lang)
    alias(tools.plugins.kotlin.kapt)
    alias(tools.plugins.kotlin.allopen)
}

group = "tech.zone84.examples"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sourceSets {
    create("integration") {
        compileClasspath += sourceSets.test.get().output
        runtimeClasspath += sourceSets.test.get().output
    }
}

val integrationImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

configurations["integrationRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

dependencies {
    implementation(libs.kotlinlogging)
    implementation(libs.guava)
    runtimeOnly(libs.slf4j.logback)
    testImplementation(libs.bundles.kotest)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = tools.versions.jvm.get()
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = tools.versions.jvm.get()
        }
    }
}
