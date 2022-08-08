rootProject.name = "kotlin-dsl-testing-demo"

dependencyResolutionManagement {
    versionCatalogs {
        create("tools") {
            version("kotlin", "1.7.10")
            version("jvm", "17")

            plugin("kotlin-lang", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("kotlin-kapt", "org.jetbrains.kotlin.kapt").versionRef("kotlin")
            plugin("kotlin-allopen", "org.jetbrains.kotlin.plugin.allopen").versionRef("kotlin")
            plugin("shadow", "com.github.johnrengelman.shadow").version("7.1.2")

            library("kotlin-reflect", "org.jetbrains.kotlin", "kotlin-reflect").versionRef("kotlin")
            library("kotlin-stdlib", "org.jetbrains.kotlin", "kotlin-stdlib-jdk8").versionRef("kotlin")
        }

        create("libs") {
            version("kotest", "5.4.1")

            library("guava", "com.google.guava:guava:31.1-jre")
            library("kotlinlogging", "io.github.microutils:kotlin-logging-jvm:2.1.20")
            library("slf4j-logback", "ch.qos.logback:logback-classic:1.2.11")
            library("test.kotest.runner", "io.kotest", "kotest-runner-junit5-jvm").versionRef("kotest")
            library("test.kotest.assertions", "io.kotest", "kotest-assertions-core").versionRef("kotest")

            bundle("kotest", listOf("test.kotest.runner", "test.kotest.assertions"))
        }
    }
}
