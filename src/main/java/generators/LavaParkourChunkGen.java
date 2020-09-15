package generators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class LavaParkourChunkGen extends ChunkGen{
	public LavaParkourChunkGen(World world) {
		super("Lava Parkour", ChunkType.PARKOUR, false, 1, world);
	}
	
	
	@Override
	public ChunkData generate(ChunkData chunkData, int chunkX, int chunkZ) {
		Random random = this.createRandom(chunkX, chunkZ);
		chunkData.setRegion(1, baseHeight, 1, 15 + 1, baseHeight + 1, 15 + 1, Material.AIR);
		chunkData.setRegion(1, baseHeight - 8, 1, 15 + 1, baseHeight, 15 + 1, Material.LAVA);
		for(int i = 0; i <= random.nextInt(5) + 10; i++){
			int platformX = 2 + random.nextInt(13);
			int platformZ = 2 + random.nextInt(13);
			int platformHeight = 0 + random.nextInt(2);
			chunkData.setRegion(platformX, baseHeight - 8, platformZ, platformX + 1, baseHeight + platformHeight + 1, platformZ + 1, baseMaterial);
		}
		return chunkData;
	}
}
