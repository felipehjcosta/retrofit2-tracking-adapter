pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.novoda.bintray-release") {
                useModule("com.novoda:bintray-release:${requested.version}")
            }
        }
    }

    repositories {
        maven {
            setUrl("https://dl.bintray.com/novoda/maven/")
        }
        gradlePluginPortal()
    }
}