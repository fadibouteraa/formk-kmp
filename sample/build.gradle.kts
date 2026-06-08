plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":formk-core"))
                implementation(project(":formk-inputs"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            }
        }
    }
}
