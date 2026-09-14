package io.github.littleshystar02.lss02.config.validation;

import io.github.littleshystar02.lss02.config.ConfigKey;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ConfigKeyValidator {

    private ConfigKeyValidator() {}

    public static <E extends Enum<E> & ConfigKey> void validate(YamlFile file, Class<E> enumClass) {
        Map<String, E> pathToKey = collectPathToKey(enumClass);
        List<String> duplicatePaths = findDuplicatePaths(enumClass);
        List<String> orphanPaths = findOrphanPaths(file, pathToKey);

        if (!duplicatePaths.isEmpty() || !orphanPaths.isEmpty()) {
            throw new ConfigValidationException(enumClass, duplicatePaths, orphanPaths);
        }
    }

    public static <E extends Enum<E> & ConfigKey> void validateTemplate(YamlFile templateFile, Class<E> enumClass) {
        Map<String, E> pathToKey = collectPathToKey(enumClass);
        List<String> duplicatePaths = findDuplicatePaths(enumClass);
        List<String> orphanPaths = findOrphanPaths(templateFile, pathToKey);

        List<String> missingPaths = new ArrayList<>();
        for (String path : pathToKey.keySet()) {
            if (!templateFile.contains(path)) {
                missingPaths.add(path);
            }
        }

        if (!duplicatePaths.isEmpty() || !orphanPaths.isEmpty() || !missingPaths.isEmpty()) {
            throw new ConfigValidationException(enumClass, duplicatePaths, orphanPaths, missingPaths);
        }
    }

    private static <E extends Enum<E> & ConfigKey> Map<String, E> collectPathToKey(Class<E> enumClass) {
        Map<String, E> pathToKey = new LinkedHashMap<>();
        for (E key : enumClass.getEnumConstants()) {
            pathToKey.put(key.getPath(), key);
        }
        return pathToKey;
    }

    private static <E extends Enum<E> & ConfigKey> List<String> findDuplicatePaths(Class<E> enumClass) {
        Map<String, E> seen = new LinkedHashMap<>();
        List<String> duplicatePaths = new ArrayList<>();
        for (E key : enumClass.getEnumConstants()) {
            E previous = seen.put(key.getPath(), key);
            if (previous != null) {
                duplicatePaths.add(key.getPath());
            }
        }
        return duplicatePaths;
    }

    private static List<String> findOrphanPaths(YamlFile file, Map<String, ?> pathToKey) {
        List<String> orphanPaths = new ArrayList<>();
        for (String path : file.getKeys(true)) {
            if (file.isConfigurationSection(path)) {
                continue;
            }
            if (!pathToKey.containsKey(path)) {
                orphanPaths.add(path);
            }
        }
        return orphanPaths;
    }

}
