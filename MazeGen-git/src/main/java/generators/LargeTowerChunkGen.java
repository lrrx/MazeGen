package generators;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class LargeTowerChunkGen extends ChunkGen{
	public LargeTowerChunkGen(World world, int chunkX, int chunkZ) {
		super("Large Tower", ChunkType.NEUTRAL, false, 1, chunkX, chunkZ, world);
	}
	
	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = this.createRandom(chunkX, chunkZ);
		
		int width = random.nextInt(6);
		
		chunkData.setRegion(8 - width, baseHeight, 8 - width, 9 + width, 128, 9 + width, baseMaterial);
		
		return chunkData;
	}
}
