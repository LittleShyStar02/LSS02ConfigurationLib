package io.github.littleshystar02.lss02.config;

import io.github.littleshystar02.lss02.config.validation.ConfigKeyValidator;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.configuration.implementation.api.YamlImplementation;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public abstract class AbstractConfiguration extends YamlFile {

    protected AbstractConfiguration() {
        super();
    }

    protected AbstractConfiguration(YamlImplementation implementation) {
        super(implementation);
    }

    protected AbstractConfiguration(String path) {
        super(path);
    }

    protected AbstractConfiguration(File file) {
        super(file);
    }

    protected AbstractConfiguration(URI uri) {
        super(uri);
    }

    protected AbstractConfiguration(URL url) throws URISyntaxException {
        super(url);
    }

    public void addDefault(ConfigKey key) {
        addDefault(key.getPath(), key.getDefaultValue());
    }

    @SuppressWarnings("unchecked")
    private <T> T castDefault(ConfigKey key) {
        return (T) key.getDefaultValue();
    }

    public Object get(ConfigKey key) {
        return contains(key.getPath()) ? get(key.getPath()) : key.getDefaultValue();
    }

    public boolean getBoolean(ConfigKey key) {
        return contains(key.getPath()) ? getBoolean(key.getPath()) : castDefault(key);
    }

    public List<Boolean> getBooleanList(ConfigKey key) {
        return contains(key.getPath()) ? getBooleanList(key.getPath()) : castDefault(key);
    }

    public byte getByte(ConfigKey key) {
        return contains(key.getPath()) ? getByte(key.getPath()) : castDefault(key);
    }

    public List<Byte> getByteList(ConfigKey key) {
        return contains(key.getPath()) ? getByteList(key.getPath()) : castDefault(key);
    }

    public char getCharacter(ConfigKey key) {
        return contains(key.getPath()) ? getCharacter(key.getPath()) : castDefault(key);
    }

    public List<Character> getCharacterList(ConfigKey key) {
        return contains(key.getPath()) ? getCharacterList(key.getPath()) : castDefault(key);
    }

    public double getDouble(ConfigKey key) {
        return contains(key.getPath()) ? getDouble(key.getPath()) : castDefault(key);
    }

    public List<Double> getDoubleList(ConfigKey key) {
        return contains(key.getPath()) ? getDoubleList(key.getPath()) : castDefault(key);
    }

    public float getFloat(ConfigKey key) {
        return contains(key.getPath()) ? getFloat(key.getPath()) : castDefault(key);
    }

    public List<Float> getFloatList(ConfigKey key) {
        return contains(key.getPath()) ? getFloatList(key.getPath()) : castDefault(key);
    }

    public int getInt(ConfigKey key) {
        return contains(key.getPath()) ? getInt(key.getPath()) : castDefault(key);
    }

    public List<Integer> getIntegerList(ConfigKey key) {
        return contains(key.getPath()) ? getIntegerList(key.getPath()) : castDefault(key);
    }

    public List<?> getList(ConfigKey key) {
        return contains(key.getPath()) ? getList(key.getPath()) : castDefault(key);
    }

    public long getLong(ConfigKey key) {
        return contains(key.getPath()) ? getLong(key.getPath()) : castDefault(key);
    }

    public List<Long> getLongList(ConfigKey key) {
        return contains(key.getPath()) ? getLongList(key.getPath()) : castDefault(key);
    }

    public List<Map<?, ?>> getMapList(ConfigKey key) {
        return contains(key.getPath()) ? getMapList(key.getPath()) : castDefault(key);
    }

    public short getShort(ConfigKey key) {
        return contains(key.getPath()) ? getShort(key.getPath()) : castDefault(key);
    }

    public List<Short> getShortList(ConfigKey key) {
        return contains(key.getPath()) ? getShortList(key.getPath()) : castDefault(key);
    }

    public String getString(ConfigKey key) {
        return contains(key.getPath()) ? getString(key.getPath()) : castDefault(key);
    }

    public List<String> getStringList(ConfigKey key) {
        return contains(key.getPath()) ? getStringList(key.getPath()) : castDefault(key);
    }

    public void set(ConfigKey key, Object value) {
        set(key.getPath(), value);
    }

    public <E extends Enum<E> & ConfigKey> void validate(Class<E> enumClass) {
        ConfigKeyValidator.validate(this, enumClass);
    }

    public <E extends Enum<E> & ConfigKey> void validateTemplate(Class<E> enumClass) {
        ConfigKeyValidator.validateTemplate(this, enumClass);
    }

}
