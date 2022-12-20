import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.allopen") version "1.7.20"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.7"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets.all{
        java.setSrcDirs(listOf("$name/src"))
        resources.setSrcDirs(listOf("$name/resources"))
    }

    wrapper {
        gradleVersion = "7.6"
    }
}


configure<org.jetbrains.kotlin.allopen.gradle.AllOpenExtension> {
    annotation("org.openjdk.jmh.annotations.State")
}

dependencies {
    implementation("org.json:json:20220924")
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.6")
    implementation(kotlin("stdlib-jdk8"))
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

benchmark {
    configurations {
        named("main") {
            iterationTime = 5
            iterationTimeUnit = "sec"

        }
    }
    targets {
        register("main") {
            this as kotlinx.benchmark.gradle.JvmBenchmarkTarget
            jmhVersion = "1.21"
        }
    }
}
