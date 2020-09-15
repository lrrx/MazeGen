package generators;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class LavaChunkGen extends ChunkGen{
	public LavaChunkGen(World world) {
		super("Lava", ChunkType.OBSTACLE, false, 1, world);
	}
	
	@Override
	public ChunkData generate(ChunkData chunkData, int chunkX, int chunkZ) {
		chunkData.setRegion(1, baseHeight, 1, 15 + 1, baseHeight + 1, 15 + 1, Material.AIR);
		chunkData.setRegion(1, baseHeight - 8, 1, 15 + 1, baseHeight, 15 + 1, Material.LAVA);
		return chunkData;
	}
}
