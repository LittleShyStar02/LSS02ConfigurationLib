package io.github.littleshystar02.lss02.config.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClassLoaderResourceExtractorTest {

    private final ResourceExtractor extractor = new ClassLoaderResourceExtractor(getClass().getClassLoader());

    @Test
    void copiesResourceContentToTargetFile(@TempDir Path tempDir) throws IOException {
        File target = tempDir.resolve("copied.yml").toFile();

        extractor.extract("test-resource.yml", target);

        assertEquals("value: 42\n", Files.readString(target.toPath()));
    }

    @Test
    void doesNothingWhenTargetFileAlreadyExists(@TempDir Path tempDir) throws IOException {
        File target = tempDir.resolve("existing.yml").toFile();
        Files.writeString(target.toPath(), "value: untouched\n");

        extractor.extract("test-resource.yml", target);

        assertEquals("value: untouched\n", Files.readString(target.toPath()));
    }

    @Test
    void createsMissingParentDirectories(@TempDir Path tempDir) throws IOException {
        File target = tempDir.resolve("nested/sub/copied.yml").toFile();

        extractor.extract("test-resource.yml", target);

        assertEquals("value: 42\n", Files.readString(target.toPath()));
    }

    @Test
    void throwsWhenResourceDoesNotExist(@TempDir Path tempDir) {
        File target = tempDir.resolve("copied.yml").toFile();

        assertThrows(IllegalArgumentException.class, () -> extractor.extract("does-not-exist.yml", target));
    }

}
