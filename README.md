# MazeGen

MazeGen is a Spigot/Bukkit/PaperMC plugin that turns your Minecraft world into an infinite, randomly generated maze.
It is compatible with the current 1.15.2 release of the game and the respective server platforms.

## Building

There are up-to-date builds of the current master available [here](https://github.com/DevMiner-BuildBot/MazeGen-Builds/releases).

To build MazeGen, run the `install` task from your IDE in the Maven section or run this command from your command line.

```bash
mvn install
```

## Setup

To enable the world generator, put the compiled jar into your plugins folder add the following lines to your bukkit.yml:

```yaml
worlds:
  world:
    generator: MazeGen
```

## Screenshots

  ![Screenshot 1](/assets/Maze-Screenshot-1.jpg)
  ![Screenshot 2](/assets/Maze-Screenshot-2.jpg)
  ![Screenshot 3](/assets/Maze-Screenshot-3.jpg)
