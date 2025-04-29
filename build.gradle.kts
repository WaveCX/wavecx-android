plugins {
    `maven-publish`
}

val sdkVersion = "0.0.9"

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
                    }
                }
            }
        }
    }
}

tasks.register("assemble") {
    group = "build"
    description = "Placeholder assemble task for JitPack"
}

