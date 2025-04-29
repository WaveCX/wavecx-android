plugins {
    `maven-publish`
}

val release by configurations.creating

dependencies {
    release("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.WaveCX"
            artifactId = "wavecx-android-sdk"
            version = "0.0.8"

            artifact(file("lib/wavecx-android-sdk.aar")) {
                builtBy(tasks.named("assemble"))
            }

            pom {
                name.set("WaveCX Android SDK")
                description.set("WaveCX Android SDK")
            }

            withDependenciesFromConfiguration(release)
        }
    }
}

tasks.register("assemble") {
    group = "build"
    description = "Placeholder assemble task for JitPack"
}
