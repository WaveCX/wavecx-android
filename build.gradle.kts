plugins {
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.WaveCX"
            artifactId = "wavecx-android-sdk"
            version = "0.0.1"

            artifact(file("lib/wavecx-android-sdk.aar"))

            pom {
                name.set("WaveCX Android SDK")
                description.set("WaveCX Android SDK")
            }
        }
    }
}
