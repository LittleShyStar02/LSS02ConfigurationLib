package io.github.littleshystar02.lss02.config.validation;

import io.github.littleshystar02.lss02.config.ConfigKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigKeyValidatorTest {

    private enum TestKey implements ConfigKey {
        NAME("app.name", "default-name"),
        PORT("app.port", 8080);

        private final String path;
        private final Object defaultValue;

        TestKey(String path, Object defaultValue) {
            this.path = path;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getPath() {
            return path;
        }

        @Override
        public Object getDefaultValue() {
            return defaultValue;
        }
    }

    private enum DuplicateKey implements ConfigKey {
        FIRST("app.name", "a"),
        SECOND("app.name", "b");

        private final String path;
        private final Object defaultValue;

        DuplicateKey(String path, Object defaultValue) {
            this.path = path;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getPath() {
            return path;
        }

        @Override
        public Object getDefaultValue() {
            return defaultValue;
        }
    }

    @Test
    void failsWhenEnumHasDuplicatePaths(@TempDir Path tempDir) throws IOException {
        YamlFile file = loadYaml(tempDir, "app:\n  name: real-name\n");

        ConfigValidationException exception = assertThrows(ConfigValidationException.class,
                () -> ConfigKeyValidator.validate(file, DuplicateKey.class));

        assertTrue(exception.getDuplicatePaths().contains("app.name"));
    }

    @Test
    void failsWhenFileHasAnOrphanKey(@TempDir Path tempDir) throws IOException {
        YamlFile file = loadYaml(tempDir, "app:\n  name: real-name\n  unknown: 1\n");

        ConfigValidationException exception = assertThrows(ConfigValidationException.class,
                () -> ConfigKeyValidator.validate(file, TestKey.class));

        assertTrue(exception.getOrphanPaths().contains("app.unknown"));
        assertTrue(exception.getDuplicatePaths().isEmpty());
    }

    private YamlFile loadYaml(Path tempDir, String content) throws IOException {
        Path file = tempDir.resolve("config.yml");
        Files.writeString(file, content);
        YamlFile yamlFile = new YamlFile(file.toFile());
        yamlFile.load();
        return yamlFile;
    }

    @Test
    void passesWhenFileHasFewerKeysThanEnum(@TempDir Path tempDir) throws IOException {
        YamlFile file = loadYaml(tempDir, "app:\n  name: real-name\n");

        assertDoesNotThrow(() -> ConfigKeyValidator.validate(file, TestKey.class));
    }

    @Test
    void passesWhenFileMatchesEnumExactly(@TempDir Path tempDir) throws IOException {
        YamlFile file = loadYaml(tempDir, "app:\n  name: real-name\n  port: 9090\n");

        assertDoesNotThrow(() -> ConfigKeyValidator.validate(file, TestKey.class));
    }

    @Test
    void templateFailsWhenEnumKeyIsMissingFromFile(@TempDir Path tempDir) throws IOException {
        YamlFile file = loadYaml(tempDir, "app:\n  name: real-name\n");

        ConfigValidationException exception = assertThrows(ConfigValidationException.class,
                () -> ConfigKeyValidator.validateTemplate(file, TestKey.class));

        assertTrue(exception.getMissingPaths().contains("app.port"));
    }

    @Test
    void templatePassesWhenFileMatchesEnumExactly(@TempDir Path tempDir) throws IOException {
        YamlFile file = loadYaml(tempDir, "app:\n  name: real-name\n  port: 9090\n");

        assertDoesNotThrow(() -> ConfigKeyValidator.validateTemplate(file, TestKey.class));
    }

}
