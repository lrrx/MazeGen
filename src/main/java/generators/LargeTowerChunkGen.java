package generators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class LargeTowerChunkGen extends ChunkGen{
	public LargeTowerChunkGen(World world, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
		super("Large Tower", ChunkType.NEUTRAL, false, 1, chunkX, chunkZ, world, biomeGrid);
	}
	
	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = this.createRandom();
		
		int width = random.nextInt(6);
		
		chunkData.setRegion(8 - width, baseHeight, 8 - width, 9 + width, 128, 9 + width, baseMaterial);
		
		return chunkData;
	}
}
