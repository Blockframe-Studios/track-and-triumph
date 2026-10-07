# Track and Triumph

A hunting mod for **Minecraft 1.21.11** built on **NeoForge 21.11.45**.

- Mod id: `trackandtriumph`
- Base package: `com.blockbench.trackandtriumph`
- Build system: Gradle with [ModDevGradle](https://github.com/neoforged/ModDevGradle) (via the included wrapper)

> This branch is the clean project base (the NeoForge MDK with the example content removed). Active feature work happens on
> the `alpha-v1.0.0` branch, whose README also covers the game content and controls.

## Requirements

| Requirement | Notes |
|---|---|
| **JDK 21 or newer** | Minecraft 1.21.11 runs on Java 21. Any distribution works (Temurin, Corretto, ...). JDK 17 will **not** work, see [Troubleshooting](#troubleshooting). |
| **Git** | To clone the repository. |
| **An IDE** | IntelliJ IDEA is recommended (Community edition is fine). Eclipse also works. |
| **Internet access** (first build) | Gradle downloads Minecraft and NeoForge, and may download a JDK 21 for compiling if one isn't found. |

You do **not** need to install Gradle. The wrapper (`gradlew` / `gradlew.bat`) downloads the right version.

## Getting started

1. **Clone the repository:**

   ```sh
   git clone https://github.com/Blockframe-Studios/track-and-triumph.git
   cd track-and-triumph
   ```

   To work on the feature branch instead, run `git checkout alpha-v1.0.0`.

2. **Open the project in IntelliJ IDEA** (`File > Open`, pick the project folder) and let it import the Gradle project.

3. **Point IntelliJ at JDK 21+** (this is the most common setup problem):
   - `File > Project Structure > Project > SDK`: choose a JDK 21 or newer.
   - `Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JVM`: choose the same JDK.
   - Reload the Gradle project.

4. **Wait for the first sync to finish.** The first run downloads and decompiles Minecraft and can take several minutes.

5. **Run the game** from the Gradle tool window (`Tasks > neoforge`) or the generated run configurations, or from a terminal:

   ```sh
   ./gradlew runClient     # launch Minecraft with the mod  (Windows: gradlew.bat runClient)
   ./gradlew runServer     # launch a dedicated server
   ```

   The first `runServer` stops with an EULA message. Set `eula=true` in the `eula.txt` it creates in the server's run
   directory, then run it again.

6. **Build a jar** with `./gradlew build`. The output is in `build/libs/`.


## Project layout

```
src/main/java/com/blockbench/trackandtriumph/
  TrackandTriumph.java         mod entry point
  TrackandTriumphClient.java   client-only setup
  Config.java                  common config
src/main/resources/assets/trackandtriumph/
  lang/en_us.json              English text
src/main/templates/META-INF/neoforge.mods.toml   mod metadata (filled in by Gradle)
```

## Troubleshooting

- **`class file version 65.0 ... only recognizes class file versions up to 61.0`**: something is running on Java 17.
  Set the IDE project SDK and Gradle JVM to JDK 21+ (see step 3).
- **Missing libraries or odd IDE errors**: run `./gradlew --refresh-dependencies`, or `./gradlew clean` and sync again.
- **Gradle can't find a JDK 21 toolchain**: install JDK 21+ yourself, or check that you have internet access so Gradle can fetch one.

## Mappings

The project uses the official Mojang mappings (with Parchment). These names are covered by a specific license; see
<https://github.com/NeoForged/NeoForm/blob/main/Mojang.md>.

## License

See [LICENSE](LICENSE).
