# LSS02ConfigurationLib

LSS02ConfigurationLib is a small Java library that sits on top of [SimpleYAML](https://github.com/Carleslc/SimpleYAML)'s `YamlFile` and removes the repetitive parts of working with YAML configuration files: literal path strings scattered across the codebase, default values duplicated at every call site, and configuration files that silently drift out of sync with the code that reads them.

You don't get a new configuration format or a replacement for `YamlFile`. You get a thin abstract class you extend, plus an enum you write yourself to describe every key your configuration has.

The full tutorial, with the same examples used below, is published at [littleshystar02.github.io/LSS02ConfigurationLib-Wiki](https://littleshystar02.github.io/LSS02ConfigurationLib-Wiki/). The site is dark mode only for now, it doesn't follow your system theme.

## How it works

You define an enum that implements `ConfigKey`. Each constant carries the YAML path it maps to and the value that should be returned when that path is missing from the file.

```java
public enum AppKeys implements ConfigKey {

    SERVER_NAME("server.name", "My Server"),
    MAX_PLAYERS("server.max-players", 20),
    MOTD_LINES("server.motd", List.of("Welcome!"));

    private final String path;
    private final Object defaultValue;

    AppKeys(String path, Object defaultValue) {
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
```

Then you extend `AbstractConfiguration`, which itself extends `YamlFile`, so every method `YamlFile` already gives you (`load()`, `save()`, `set(String, Object)`, `getString(String)` and so on) is still there, untouched.

```java
public class AppConfig extends AbstractConfiguration {

    public AppConfig(File file) {
        super(file);
    }
}
```

```java
AppConfig config = new AppConfig(new File(dataFolder, "config.yml"));
config.load();

String name = config.getString(AppKeys.SERVER_NAME);
int maxPlayers = config.getInt(AppKeys.MAX_PLAYERS);
List<String> motd = config.getStringList(AppKeys.MOTD_LINES);

config.set(AppKeys.MAX_PLAYERS, 32);
config.save();
```

If `server.max-players` isn't in the file yet, `getInt(AppKeys.MAX_PLAYERS)` returns `20` without throwing anything. The library never tries to protect you from a `NullPointerException` on a missing key: `YamlFile` already handles that through the default value, and `ConfigKey` just gives that default a name you can reuse everywhere instead of retyping it.

Every typed getter you'd expect on `ConfigurationSection` has a `ConfigKey` overload: `getString`, `getInt`, `getBoolean`, `getDouble`, `getLong`, `getFloat`, `getByte`, `getShort`, `getCharacter`, the untyped `get`, and the list variants of each. The string-based methods you already know from `YamlFile` keep working exactly as before, nothing is overridden.

`addDefault(ConfigKey)` registers the key's default in `YamlFile`'s own defaults section, so even a plain `getInt("server.max-players")` call without a second argument benefits from it.

## Catching drift between the enum and the file

Once you have an enum and a real file, you can ask the library to check that the two agree.

```java
config.load();
config.validate(AppKeys.class);
```

`validate` walks every leaf key actually present in the loaded file and fails with a `ConfigValidationException` if it finds a path with no matching enum constant, or two enum constants pointing at the same path. It does not complain about an enum constant that isn't in the file yet: that's exactly the case the default value exists for, not an error.

That asymmetry leaves one gap open on purpose: a typo inside the enum itself, one that doesn't match anything in the file, will never be flagged this way, because the file simply won't contain an orphan for it. `validateTemplate` closes that gap by checking the *default* YAML resource you ship inside your jar, in both directions: an unexpected path in the template is still an error, and now so is an enum constant that the template doesn't define either.

```java
try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.yml")) {
    YamlFile template = new YamlFile();
    template.load(in);
    template.validateTemplate(AppKeys.class); // run this in a test, against the packaged default
}
```

Run `validate` against the user's live file at startup, and `validateTemplate` against your bundled default file in a unit test, and a path typo in the enum has nowhere left to hide.

## Extracting the bundled default file

Standalone applications and Minecraft plugins load their jar's classpath resources differently. `ClassLoaderResourceExtractor`, in `config-core`, copies a classpath resource to a target file using plain `ClassLoader#getResourceAsStream`, the same way regardless of platform, and does nothing if the target already exists.

```java
File target = new File(dataFolder, "config.yml");
new ClassLoaderResourceExtractor(getClass().getClassLoader()).extract("config.yml", target);
```

## Bukkit and Paper

The `config-bukkit` module adds `BukkitResourceExtractor`, which prefers `JavaPlugin#saveResource` when the requested target is the plugin's own data folder, since that's the native mechanism Bukkit already provides, and falls back to `ClassLoaderResourceExtractor` for anything else.

```java
File target = new File(plugin.getDataFolder(), "config.yml");
new BukkitResourceExtractor(plugin).extract("config.yml", target);
```

`config-bukkit` targets Java 21, matching Paper's current stable line. `config-core` only requires Java 17 and has no dependency on any server API, so it works the same in a standalone application, a Bukkit plugin, or anywhere else on the classpath.

## Thread-safety

`AbstractConfiguration` adds no synchronization of its own. It extends `YamlFile`, and `YamlFile` isn't thread-safe either, so touching the same configuration instance from more than one thread at a time (a Bukkit async task alongside the main thread, for instance) needs to be synchronized by the code calling into it. The library doesn't guess where your atomic boundaries should be, so it doesn't wrap anything for you.

## Modules and requirements

| Module | Requires | Depends on |
|---|---|---|
| `config-core` | Java 17 | SimpleYAML |
| `config-bukkit` | Java 21 | `config-core`, Paper API |

## Adding it to your build

The library isn't published to Maven Central yet. Until then, build it locally and use `mavenLocal()`.

```bash
git clone https://github.com/LittleShyStar02/LSS02ConfigurationLib.git
cd LSS02ConfigurationLib
./gradlew publishToMavenLocal
```

```kotlin
repositories {
    mavenLocal()
}

dependencies {
    implementation("io.github.littleshystar02:config-core:0.1.0-SNAPSHOT")
    // implementation("io.github.littleshystar02:config-bukkit:0.1.0-SNAPSHOT")
}
```

## License

LSS02ConfigurationLib is licensed under the [PolyForm Noncommercial License 1.0.0](LICENSE.md). The source is public and you're free to use, modify and share it for noncommercial purposes; selling it or a modified version of it is not permitted.
