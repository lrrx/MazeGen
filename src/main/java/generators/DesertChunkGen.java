package generators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class DesertChunkGen extends ChunkGen{
	public DesertChunkGen(World world) {
		super("Desert", ChunkType.OBSTACLE, false, 1, world);
	}

	@Override
	public ChunkData generate(ChunkData chunkData, int chunkX, int chunkZ) {
		
		Random random = createRandom(chunkX, chunkZ);
		chunkData.setRegion(1, baseHeight - 4, 1, 16, baseHeight + 1, 16, Material.SAND);
		for (int i = 0; i < 8; i++) {
			int x = random.nextInt(13) + 2;
			int z = random.nextInt(13) + 2;
			int size = random.nextInt(2) + 1;
			int type = random.nextInt(2);
			chunkData.setRegion(x - size, baseHeight + 1, z - size, x + size + 1, baseHeight + 2, z + size + 1, Material.SAND);
			switch (type) {
			case 0:
				chunkData.setBlock(x, baseHeight + 2, z, Material.CACTUS);
				chunkData.setBlock(x, baseHeight + 3, z, Material.CACTUS);
				break;
			case 1:
				chunkData.setBlock(x, baseHeight + 2, z, Material.DEAD_BUSH);
				break;
			} 
		} 
		return chunkData;
	}

	@Override
	public void populate(Chunk chunk) {}
}
