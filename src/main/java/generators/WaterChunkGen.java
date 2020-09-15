package generators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class WaterChunkGen extends ChunkGen{
	public WaterChunkGen(World world) {
		super("Water Chunk", ChunkType.OBSTACLE, false, 1, world);
	}

	@Override
	public ChunkData generate(ChunkData chunkData, int chunkX, int chunkZ) {
		Random random = this.createRandom(chunkX, chunkZ);
		chunkData.setRegion(1, baseHeight, 1, 16, baseHeight + 1, 16, Material.AIR);
		chunkData.setRegion(1, baseHeight - 8, 1, 16, baseHeight - 7, 16, Material.SAND);
		chunkData.setRegion(1, baseHeight - 7, 1, 16, baseHeight, 16, Material.WATER);
		for (int i = 0; i < random.nextInt(5) + 4; i++) {
			int x = random.nextInt(13) + 2;
			int z = random.nextInt(13) + 2;
			chunkData.setRegion(x, baseHeight - 7, z, x + 1, baseHeight - random.nextInt(5) +  - 1, z + 1, Material.KELP_PLANT); 
				
		} 
		/*
		for (int i2 = 0; i2 < random.nextInt(3) + 1; i2++) {
			int x = random.nextInt(13) + 2;
			int z = random.nextInt(13) + 2;
			for (int i = 0; i < random.nextInt(2) + 3; i++) {
				int direction = random.nextInt(4);
				switch (direction) {
				case 0:
					x++;
					z++;
					break;
				case 1:
					x--;
					z--;
					break;
				case 2:
					z++;
					x++;
					break;
				case 3:
					z--;
					x--;
					break;
				} 
				if (x < 14 && x > 1 && z < 14 && z > 1)
					chunkData.setRegion(x - 1, baseHeight - 1, z - 1, x + 1, baseHeight, z + 1, Material.ICE); 
			} 
		}*/
		return chunkData;
	}
}
