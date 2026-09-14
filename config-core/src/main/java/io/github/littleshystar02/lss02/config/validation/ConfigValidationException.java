package io.github.littleshystar02.lss02.config.validation;

import java.util.List;

public class ConfigValidationException extends RuntimeException {

    private final List<String> duplicatePaths;
    private final List<String> orphanPaths;
    private final List<String> missingPaths;

    public ConfigValidationException(Class<?> enumClass, List<String> duplicatePaths, List<String> orphanPaths) {
        this(enumClass, duplicatePaths, orphanPaths, List.of());
    }

    public ConfigValidationException(Class<?> enumClass, List<String> duplicatePaths, List<String> orphanPaths, List<String> missingPaths) {
        super(buildMessage(enumClass, duplicatePaths, orphanPaths, missingPaths));
        this.duplicatePaths = duplicatePaths;
        this.orphanPaths = orphanPaths;
        this.missingPaths = missingPaths;
    }

    private static String buildMessage(Class<?> enumClass, List<String> duplicatePaths, List<String> orphanPaths, List<String> missingPaths) {
        StringBuilder message = new StringBuilder("Config validation failed for ").append(enumClass.getName()).append(':');
        if (!duplicatePaths.isEmpty()) {
            message.append("\n  duplicate paths in enum: ").append(duplicatePaths);
        }
        if (!orphanPaths.isEmpty()) {
            message.append("\n  paths in file with no matching enum constant: ").append(orphanPaths);
        }
        if (!missingPaths.isEmpty()) {
            message.append("\n  enum constants with no matching path in file: ").append(missingPaths);
        }
        return message.toString();
    }

    public List<String> getDuplicatePaths() {
        return duplicatePaths;
    }

    public List<String> getOrphanPaths() {
        return orphanPaths;
    }

    public List<String> getMissingPaths() {
        return missingPaths;
    }

}
