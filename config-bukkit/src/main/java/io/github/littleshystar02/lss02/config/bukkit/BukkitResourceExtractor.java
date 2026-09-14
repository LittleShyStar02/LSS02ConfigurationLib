package io.github.littleshystar02.lss02.config.bukkit;

import io.github.littleshystar02.lss02.config.resource.ClassLoaderResourceExtractor;
import io.github.littleshystar02.lss02.config.resource.ResourceExtractor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class BukkitResourceExtractor implements ResourceExtractor {

    private final JavaPlugin plugin;

    public BukkitResourceExtractor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public File extract(String resourcePath, File targetFile) {
        File nativeTarget = new File(plugin.getDataFolder(), resourcePath);

        if (targetFile.getAbsoluteFile().toPath().normalize().equals(nativeTarget.getAbsoluteFile().toPath().normalize())) {
            plugin.saveResource(resourcePath, false);
            return targetFile;
        }

        return new ClassLoaderResourceExtractor(plugin.getClass().getClassLoader()).extract(resourcePath, targetFile);
    }

}
