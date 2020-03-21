package generators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class WaterHolesChunkGen extends ChunkGen{
	public WaterHolesChunkGen(World world, int chunkX, int chunkZ) {
		super("Water Holes", ChunkType.OBSTACLE, false, 1, chunkX, chunkZ, world);
	}

	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = this.createRandom(chunkX, chunkZ);

		for(int i = 0; i <= random.nextInt(10) + 15; i++){
			int posX = 2 + random.nextInt(13);
			int posZ = 2 + random.nextInt(13);
			int width = 1 + random.nextInt(3);
			int length = width + random.nextInt(2) - 1;
			int depth = 2 + random.nextInt(4);
			chunkData.setRegion(posX, baseHeight, posZ, posX + width, baseHeight + 1, posZ + length, Material.AIR);
			chunkData.setRegion(posX, baseHeight - depth, posZ, posX + width, baseHeight, posZ + length, Material.WATER);
		}
		return chunkData;
	}
}
