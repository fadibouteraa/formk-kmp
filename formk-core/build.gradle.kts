plugins {
    `maven-publish`
    signing
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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


group = "io.github.fadibouteraa.formk"
version = "1.0.0"

// Setup Dokka if we want javadoc jars, but for now we'll just use empty jars or simple jar tasks to pass Sonatype requirements
val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications.withType<MavenPublication> {
        artifact(javadocJar)
        
        pom {
            name.set("Formk")
            description.set("A pure Kotlin, UI-agnostic library for standardizing form validation across KMP.")
            url.set("https://github.com/fadibouteraa/formk-kmp")
            
            licenses {
                license {
                    name.set("MIT License")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("fadibouteraa")
                    name.set("Fadi Bouteraa")
                    email.set("fadi@example.com")
                }
            }
            scm {
                connection.set("scm:git:git://github.com/fadibouteraa/formk-kmp.git")
                developerConnection.set("scm:git:ssh://github.com/fadibouteraa/formk-kmp.git")
                url.set("https://github.com/fadibouteraa/formk-kmp")
            }
        }
    }
    
    repositories {
        maven {
            name = "OSSRH"
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            
            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

signing {
    val signingKeyId = System.getenv("SIGNING_KEY_ID")
    val signingKey = System.getenv("SIGNING_KEY")
    val signingPassword = System.getenv("SIGNING_PASSWORD")
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications)
}
