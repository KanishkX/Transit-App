pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {create<BasicAuthentication>("basic")}
            credentials {
                username = "mapbox"
                password = "sk.eyJ1Ijoia2FuaXNoazAxIiwiYSI6ImNscDA2Nm13cjA3NzIybHA1NGtubXI3aXcifQ.ychKEtpobCVewWaNsAiqcQ"
            }
        }
    }
}

rootProject.name = "TransitApp"
include(":app")
 