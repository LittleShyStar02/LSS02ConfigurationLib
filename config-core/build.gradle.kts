java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

apply(plugin = "com.vanniktech.maven.publish")

description = "Core layer over SimpleYAML's YamlFile: enum-based ConfigKey accessors, typed getters with defaults, and fail-fast validation."

dependencies {
    api("com.github.Carleslc:Simple-YAML:1.8.5")
}
