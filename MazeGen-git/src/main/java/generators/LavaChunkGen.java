package generators;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class LavaChunkGen extends ChunkGen{
	public LavaChunkGen(World world, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
		super("Lava Floor", ChunkType.OBSTACLE, false, 1, chunkX, chunkZ, world, biomeGrid);
	}
	
	@Override
	public ChunkData generate(ChunkData chunkData) {
		chunkData.setRegion(1, baseHeight, 1, 15 + 1, baseHeight + 1, 15 + 1, Material.AIR);
		chunkData.setRegion(1, baseHeight - 8, 1, 15 + 1, baseHeight, 15 + 1, Material.LAVA);
		return chunkData;
	}
}
