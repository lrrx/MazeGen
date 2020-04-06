package generators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class MeadowChunkGen extends ChunkGen{
	public MeadowChunkGen(World world, int chunkX, int chunkZ) {
		super("Meadow", ChunkType.GARDEN, false, 1, chunkX, chunkZ, world);
	}
	
	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = this.createRandom(chunkX, chunkZ);

		chunkData.setRegion(1, baseHeight, 1, 15 + 1, baseHeight + 1, 15 + 1, Material.GRASS_BLOCK);
		chunkData.setRegion(1, baseHeight - 4, 1, 15 + 1, baseHeight, 15 + 1, Material.DIRT);

		for(int i3 = 0; i3 < random.nextInt(5) + 3; i3++) {

			int x = random.nextInt(13) + 2;
			int z = random.nextInt(13) + 2;

			chunkData.setBlock(x, baseHeight + 1, z, Material.GRASS);

		}

		for(int i2 = 0; i2 < random.nextInt(3) + 3; i2++) {

			int x = random.nextInt(13) + 2;
			int z = random.nextInt(13) + 2;
			int type = random.nextInt(4);

			switch (type) {
			case 0:
				chunkData.setBlock(x, baseHeight + 1, z, Material.POPPY);
				break;

			case 1:
				chunkData.setBlock(x, baseHeight + 1, z, Material.DANDELION);
				break;

			case 2:
				chunkData.setBlock(x, baseHeight + 1, z, Material.BLUE_ORCHID);
				break;

			case 3:
				chunkData.setBlock(x, baseHeight + 1, z, Material.PINK_TULIP);
				break;
			}

		}

		for(int i = 0; i < random.nextInt(3) + 1; i++) {

			int x = random.nextInt(13) + 2;
			int z = random.nextInt(13) + 2;
			int size = random.nextInt(2) + 2;

			chunkData.setRegion(x - size, baseHeight + 1, z - size, x + size, baseHeight + 2, z + size, Material.OAK_LEAVES);
			chunkData.setBlock(x, baseHeight + 1, z, Material.OAK_LOG);
			chunkData.setRegion(x - (size-1), baseHeight + 2, z - (size-1), x + (size-1), baseHeight + 3, z + (size-1), Material.OAK_LEAVES);

		}

		return chunkData;
	}
}
