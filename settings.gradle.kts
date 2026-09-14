plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "LSS02ConfigurationLib"

include(
    "config-core",
    "config-bukkit"
)
