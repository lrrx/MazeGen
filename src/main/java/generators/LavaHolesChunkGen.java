package generators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class LavaHolesChunkGen extends ChunkGen{
	public LavaHolesChunkGen(World world, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
		super("Lava Holes", ChunkType.OBSTACLE, false, 1, chunkX, chunkZ, world, biomeGrid);
	}

	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = this.createRandom();

		chunkData.setRegion(1, baseHeight, 1, 15 + 1, baseHeight + 1, 15 + 1, Material.AIR);

		//Obsidian Decal
		for(int i = 0; i <= 50 + random.nextInt(50); i++) {
			chunkData.setBlock(1 + random.nextInt(15), baseHeight - 1, 1 + random.nextInt(15), Material.OBSIDIAN);
		}

		//Lava Holes
		for(int i = 0; i <= random.nextInt(10) + 15; i++){
			int posX = 2 + random.nextInt(13);
			int posZ = 2 + random.nextInt(13);
			int width = 1 + random.nextInt(3);
			int length = width + random.nextInt(2) - 1;
			int depth = 2 + random.nextInt(4);
			chunkData.setRegion(posX, baseHeight - depth, posZ, posX + width, baseHeight, posZ + length, Material.LAVA);
		}

		//Obsidian Pillars
		for(int i = 0; i <= random.nextInt(5) + 10; i++){
			int posX = 2 + random.nextInt(13);
			int posZ = 2 + random.nextInt(13);
			int height = 1 + random.nextInt(3);
			chunkData.setRegion(posX, baseHeight - height, posZ, posX + 1, baseHeight + height, posZ + 1, Material.OBSIDIAN);
		}
		return chunkData;
	}
}
