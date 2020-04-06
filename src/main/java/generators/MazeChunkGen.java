package generators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;
import core.MazeChunkGenerator;
import core.NoiseGen;

public class MazeChunkGen extends ChunkGen{
	public MazeChunkGen(World world, int chunkX, int chunkZ) {
		super("Maze", ChunkType.PUZZLE, false, 1, chunkX, chunkZ, world);
	}

	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = this.createRandom(chunkX, chunkZ);
		int[][] maze = MazeChunkGenerator.generateMaze(NoiseGen.noise(chunkX, chunkZ, world));
		//int wH = 16 + ((int) Math.abs(NoiseGen.largeNoise(chunkX, chunkZ, world) * 8D));
		//construct the maze
		for(int x = 1; x <= 15; x++) {
			for(int z = 1; z <= 15; z++) {
				if(maze[x][z] == 0) {
					chunkData.setRegion(x, baseHeight + 1, z, x + 1, baseHeight + wallHeight + 4, z + 1, baseMaterial);
				}
			}
		}
		
		//fix edge in maze to avoid closed off space
		if(random.nextBoolean()) {
			chunkData.setRegion(1, baseHeight + 1, 2, 1 + 1, baseHeight + wallHeight + 4, 2 + 1, Material.AIR);
		}
		else {
			chunkData.setRegion(2, baseHeight + 1, 1, 2 + 1, baseHeight + wallHeight + 4, 1 + 1, Material.AIR);
		}
		return chunkData;
	}
}
