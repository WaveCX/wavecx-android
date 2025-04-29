plugins {
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.WaveCX"
            artifactId = "wavecx-android-sdk"
            version = "0.0.7"

            artifact(file("lib/wavecx-android-sdk.aar"))

            pom {
                name.set("WaveCX Android SDK")
                description.set("WaveCX Android SDK")
            }
        }
    }
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

tasks.register("assemble") {
    group = "build"
    description = "Fake assemble task for JitPack"
}

