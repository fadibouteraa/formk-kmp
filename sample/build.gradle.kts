plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
}

kotlin {
    jvm()
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":formk-core"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "io.cyan.formk.sample.MainKt"
    }
}
