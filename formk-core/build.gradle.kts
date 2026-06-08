plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                // Core has no external dependencies unless necessary
            }
        }
    }
}
