package generators;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class PillarsChunkGen extends ChunkGen{
	public PillarsChunkGen(World world, int chunkX, int chunkZ) {
		super("Pillar Room", ChunkType.NEUTRAL, false, 1, chunkX, chunkZ, world);
	}

	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = this.createRandom(chunkX, chunkZ);
		for(int i = 0; i <= random.nextInt(10) + 15; i++){
			int platformX = 2 + random.nextInt(13);
			int platformZ = 2 + random.nextInt(13);
			int platformWidth = random.nextInt(3);
			int platformLength = random.nextInt(3);
			int platformHeight = 4 + random.nextInt(11);
			chunkData.setRegion(platformX, baseHeight, platformZ, platformX + platformWidth, baseHeight + platformHeight, platformZ + platformLength, baseMaterial);
		}
		return chunkData;
	}
}
