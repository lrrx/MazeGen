package generators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class SpawnChunkGen extends ChunkGen{
	public SpawnChunkGen(World world, int chunkX, int chunkZ) {
		super("Spawn Chunk", ChunkType.NEUTRAL, false, 1, chunkX, chunkZ, world);
	}
	
	@Override
	public ChunkData generate(ChunkData chunkData) {
		
		if(Math.abs(chunkX) == spawnSize && Math.abs(chunkZ) == spawnSize) {
			chunkData.setRegion(0, baseHeight, 0, 16, baseHeight + 16 * (spawnSize  * 2 + 1) + 1, 16, baseMaterial);
		}
		else if(-spawnSize < chunkX && chunkX < spawnSize && Math.abs(chunkZ) == spawnSize || -spawnSize < chunkZ && chunkZ < spawnSize && Math.abs(chunkX) == spawnSize) {
			chunkData.setRegion(0, baseHeight + 16 * spawnSize * 2, 0, 16, baseHeight + 16 * (spawnSize * 2 + 1) + 1, 16, baseMaterial);
		}
		
		/*
		if(Math.abs(chunkX % 10 - 5) == 2 && Math.abs(chunkZ % 10 - 5) == 2) {
			chunkData.setRegion(0, baseHeight, 0, 16, baseHeight + 16 * 5 + 1, 16, baseMaterial);
		}
		else if(-2 < (chunkX % 10 - 5) && (chunkX % 10 - 5) < 2 && Math.abs(chunkZ % 10 - 5) == 2 || -2 < (chunkZ % 10 - 5) && (chunkZ % 10 - 5) < 2 && Math.abs(chunkX % 10 - 5) == 2 ) {
			chunkData.setRegion(0, baseHeight + 16 * 4, 0, 16, baseHeight + 16 * 5 + 1, 16, baseMaterial);
		}
		 */
		
		return chunkData;
	}
	
	@Override
	public void populate(Chunk chunk) {}
}
