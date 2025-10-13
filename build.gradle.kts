plugins {
    `maven-publish`
}

val sdkVersion = "0.0.19"

tasks.register("assemble") {
    group = "build"
    description = "Placeholder assemble task for JitPack"
}


publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.WaveCX"
            artifactId = "wavecx-android-sdk"
            version = sdkVersion

            artifact(file("lib/wavecx-android-sdk.aar")) {
                builtBy(tasks.named("assemble"))
            }

            pom {
                name.set("WaveCX Android SDK")
                description.set("WaveCX Android SDK")

                withXml {
                    asNode().appendNode("dependencies").apply {
                        appendNode("dependency").apply {
                            appendNode("groupId", "org.jetbrains.kotlinx")
                            appendNode("artifactId", "kotlinx-serialization-json")
                            appendNode("version", "1.6.3")
                            appendNode("scope", "compile")
                        }
                        appendNode("dependency").apply {
                            appendNode("groupId", "com.google.code.gson")
                            appendNode("artifactId", "gson")
                            appendNode("version", "2.10.1")
                            appendNode("scope", "compile")
                        }
                        appendNode("dependency").apply {
                            appendNode("groupId", "com.google.android.material")
                            appendNode("artifactId", "material")
                            appendNode("version", "1.12.0")
                            appendNode("scope", "compile")
                        }
                    }
                }
            }
        }
    }
}


