package generators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class FountainChunkGen extends ChunkGen{
	public FountainChunkGen(World world, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
		super("Fountain Room", ChunkType.GARDEN, false, 1, chunkX, chunkZ, world, biomeGrid);
	}
	
	@Override
	public ChunkData generate(ChunkData chunkData) {
		chunkData.setRegion(1, baseHeight - 4, 1, 15 + 1, baseHeight, 15 + 1, Material.DIRT);
		chunkData.setRegion(1, baseHeight, 1, 15 + 1, baseHeight + 1, 15 + 1, Material.GRASS_BLOCK);
		chunkData.setRegion(1, baseHeight, 8, 15 + 1, baseHeight + 1, 8 + 1, Material.GRASS_PATH);
		chunkData.setRegion(8, baseHeight, 1, 8 + 1, baseHeight + 1, 15 + 1, Material.GRASS_PATH);
		chunkData.setRegion(7, baseHeight, 7, 9 + 1, baseHeight + 1, 9 + 1, Material.WATER);
		return chunkData;
	}
}
