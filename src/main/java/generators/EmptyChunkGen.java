package generators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class EmptyChunkGen extends ChunkGen{
	public EmptyChunkGen(World world, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
		super("Empty Room", ChunkType.NEUTRAL, false, 1, chunkX, chunkZ, world, biomeGrid);
	}
	
	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = createRandom();
		for(int i = 0; i <= 5 + random.nextInt(10); i++) {
			chunkData.setBlock(1 + random.nextInt(15), baseHeight, 1 + random.nextInt(15), Material.OBSIDIAN);
		}
		return chunkData;
	}
}
