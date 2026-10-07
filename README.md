# Track and Triumph

A hunting mod for **Minecraft 1.21.11** built on **NeoForge 21.11.45**. It adds huntable animals (animated with
[GeckoLib](https://github.com/bernie-g/geckolib)), animal drops and meats, and a magazine-fed hunting rifle.

- Mod id: `trackandtriumph`
- Base package: `com.blockbench.trackandtriumph`
- Build system: Gradle with [ModDevGradle](https://github.com/neoforged/ModDevGradle) (via the included wrapper)

## Requirements

| Requirement | Notes |
|---|---|
| **JDK 21 or newer** | Minecraft 1.21.11 runs on Java 21. Any distribution works (Temurin, Corretto, ...). JDK 17 will **not** work, see [Troubleshooting](#troubleshooting). |
| **Git** | To clone the repository. |
| **An IDE** | IntelliJ IDEA is recommended (Community edition is fine). Eclipse also works. |
| **Internet access** (first build) | Gradle downloads Minecraft, NeoForge and GeckoLib, and may download a JDK 21 for compiling if one isn't found. |

You do **not** need to install Gradle. The wrapper (`gradlew` / `gradlew.bat`) downloads the right version.

## Getting started

1. **Clone the repository** and switch to the working branch:

   ```sh
   git clone https://github.com/Blockframe-Studios/track-and-triumph.git
   cd track-and-triumph
   git checkout alpha-v1.0.0
   ```

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

Run directories (`run/`, `runs/`) and build output are git-ignored.

## Trying things in-game

Create a creative world, then:

```
/give @s trackandtriumph:hunting_rifle
/give @s trackandtriumph:rifle_magazine[trackandtriumph:rounds=5]
/give @s trackandtriumph:rifle_round 20
/summon trackandtriumph:deer
```

Animals also have spawn eggs, and everything is in the **Track and Triumph** creative tab.

### Rifle controls

| Action | Control |
|---|---|
| Aim | Hold right-click |
| Fire | Release right-click (uses one round) |
| Eject magazine | Sneak + right-click, or right-click the rifle in an inventory with an empty cursor |
| Insert magazine | Click a magazine (on the cursor) onto the rifle in an inventory |
| Fill magazine | Click rifle rounds (on the cursor) onto a magazine in an inventory |
| Reload Rifle hotkey | `R`: swap the fullest magazine in your inventory into the rifle |
| Load Magazine hotkey | `Shift + R`: top up a magazine from rounds in your inventory |

Both hotkeys can be rebound under `Options > Controls > Key Binds > Track and Triumph: Rifle`.

## Project layout

```
src/main/java/com/blockbench/trackandtriumph/
  TrackandTriumph.java         mod entry point and registry wiring
  TrackandTriumphClient.java   client-only setup (renderers, key mappings)
  entities/                    TTEntities registry and the animal classes (TTAnimal base class)
  items/                       TTItems, TTCreativeTabs, TTDataComponents
  items/weapons/               hunting rifle, magazine, and rifle hotkey actions
  network/                     client-to-server payloads
  sounds/                      TTSounds registry
  client/                      client-side helpers (animal model, key mappings)
src/main/resources/assets/trackandtriumph/
  items/                       item definitions (1.21.4+ client item format)
  models/item/                 item models
  textures/                    entity and item textures
  geckolib/models|animations/  GeckoLib entity models and animations
  sounds/, sounds.json         sound files and event definitions
  lang/en_us.json              English text
src/main/templates/META-INF/neoforge.mods.toml   mod metadata (filled in by Gradle)
```

### Asset notes

- GeckoLib 5 only loads entity assets from `assets/trackandtriumph/geckolib/models/entity/*.geo.json` and
  `assets/trackandtriumph/geckolib/animations/entity/*.animation.json`. Files placed in `geo/` or `animations/` are ignored.
- Animals use `<id>` assets for adults and `<id>_baby` assets for babies (model, texture and animations).
- The rifle's models and animation frames are chosen in `items/hunting_rifle.json`; its timing is tied to
  `HuntingRifleItem` (cooldown length and the bolt sound ticks).

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
