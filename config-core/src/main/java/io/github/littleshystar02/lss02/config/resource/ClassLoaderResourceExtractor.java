package io.github.littleshystar02.lss02.config.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ClassLoaderResourceExtractor implements ResourceExtractor {

    private final ClassLoader classLoader;

    public ClassLoaderResourceExtractor(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public File extract(String resourcePath, File targetFile) {
        if (targetFile.exists()) {
            return targetFile;
        }

        try (InputStream resource = classLoader.getResourceAsStream(resourcePath)) {
            if (resource == null) {
                throw new IllegalArgumentException("Resource not found on classpath: " + resourcePath);
            }

            File parent = targetFile.getParentFile();
            if (parent != null) {
                Files.createDirectories(parent.toPath());
            }

            Files.copy(resource, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return targetFile;
    }

}
