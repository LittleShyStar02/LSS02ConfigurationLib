description = "Bukkit/Paper adapter for LSS02ConfigurationLib: resource extraction backed by JavaPlugin#saveResource with automatic fallback."

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

apply(plugin = "com.vanniktech.maven.publish")

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":config-core"))
    compileOnlyApi("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")

    testImplementation("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    testImplementation("org.mockito:mockito-core:5.22.0")
}
