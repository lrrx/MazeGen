package generators;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class WaterChunkGen extends ChunkGen{
	public WaterChunkGen(World world, int chunkX, int chunkZ) {
		super("Water Floor", ChunkType.OBSTACLE, false, 1, chunkX, chunkZ, world);
	}

	@Override
	public ChunkData generate(ChunkData chunkData) {
		chunkData.setRegion(1, baseHeight, 1, 15 + 1, baseHeight + 1, 15 + 1, Material.AIR);
		chunkData.setRegion(1, baseHeight - 8, 1, 15 + 1, baseHeight, 15 + 1, Material.WATER);
		return chunkData;
	}
}
