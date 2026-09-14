package io.github.littleshystar02.lss02.config.bukkit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class BukkitResourceExtractorTest {

    @Test
    void delegatesToSaveResourceWhenTargetIsUnderDataFolder(@TempDir Path tempDir) {
        JavaPlugin plugin = Mockito.mock(JavaPlugin.class);
        Mockito.when(plugin.getDataFolder()).thenReturn(tempDir.toFile());

        File target = new File(tempDir.toFile(), "config.yml");
        new BukkitResourceExtractor(plugin).extract("config.yml", target);

        verify(plugin).saveResource("config.yml", false);
    }

    @Test
    void fallsBackToClassLoaderExtractionWhenTargetIsElsewhere(@TempDir Path tempDir) throws IOException {
        JavaPlugin plugin = Mockito.mock(JavaPlugin.class);
        Mockito.when(plugin.getDataFolder()).thenReturn(tempDir.resolve("data").toFile());

        File target = tempDir.resolve("outside/config.yml").toFile();
        new BukkitResourceExtractor(plugin).extract("test-resource.yml", target);

        verify(plugin, never()).saveResource(anyString(), anyBoolean());
        assertEquals("value: 42\n", Files.readString(target.toPath()));
    }

}
