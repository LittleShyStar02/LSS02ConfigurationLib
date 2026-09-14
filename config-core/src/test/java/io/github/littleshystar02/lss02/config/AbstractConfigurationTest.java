package io.github.littleshystar02.lss02.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractConfigurationTest {

    private enum TestKey implements ConfigKey {
        NAME("app.name", "default-name"),
        PORT("app.port", 8080),
        DEBUG("app.debug", false),
        TAGS("app.tags", List.of("a", "b"));

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

    private static class TestConfiguration extends AbstractConfiguration {
        TestConfiguration(File file) {
            super(file);
        }
    }

    @Test
    void addDefaultFeedsThePlainStringGetter(@TempDir Path tempDir) {
        TestConfiguration config = new TestConfiguration(tempDir.resolve("defaults.yml").toFile());
        config.addDefault(TestKey.PORT);

        assertEquals(8080, config.getInt("app.port"));
    }

    @Test
    void returnsActualValueWhenKeyIsSet(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("filled.yml");
        Files.writeString(file, "app:\n  name: real-name\n  port: 9090\n");
        TestConfiguration config = new TestConfiguration(file.toFile());
        config.load();

        assertEquals("real-name", config.getString(TestKey.NAME));
        assertEquals(9090, config.getInt(TestKey.PORT));
    }

    @Test
    void returnsDefaultValueWhenKeyIsMissing(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("empty.yml");
        Files.writeString(file, "");
        TestConfiguration config = new TestConfiguration(file.toFile());
        config.load();

        assertEquals("default-name", config.getString(TestKey.NAME));
        assertEquals(8080, config.getInt(TestKey.PORT));
        assertEquals(false, config.getBoolean(TestKey.DEBUG));
        assertEquals(List.of("a", "b"), config.getStringList(TestKey.TAGS));
    }

    @Test
    void setWritesUsingKeyPath(@TempDir Path tempDir) {
        TestConfiguration config = new TestConfiguration(tempDir.resolve("set.yml").toFile());
        config.set(TestKey.NAME, "written-name");

        assertEquals("written-name", config.getString("app.name"));
    }

}
