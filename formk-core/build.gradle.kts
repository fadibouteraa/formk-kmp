plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.vanniktech.maven.publish") version "0.29.0"
}

kotlin {
    jvm()
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

group = "io.github.fadibouteraa"
version = "1.0.0"

mavenPublishing {
    coordinates("io.github.fadibouteraa", "formk-core", "1.0.0")

    pom {
        name.set("Formk")
        description.set("A pure Kotlin, UI-agnostic library for standardizing form validation across KMP.")
        inceptionYear.set("2024")
        url.set("https://github.com/fadibouteraa/formk-kmp")
        licenses {
            license {
                name.set("The MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("fadibouteraa")
                name.set("Fadi Bouteraa")
                url.set("https://github.com/fadibouteraa/")
            }
        }
        scm {
            url.set("https://github.com/fadibouteraa/formk-kmp")
            connection.set("scm:git:git://github.com/fadibouteraa/formk-kmp.git")
            developerConnection.set("scm:git:ssh://git@github.com/fadibouteraa/formk-kmp.git")
        }
    }

    // Publish to the NEW Sonatype Central Portal
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)

    // Automatically handles signing
    signAllPublications()
}
