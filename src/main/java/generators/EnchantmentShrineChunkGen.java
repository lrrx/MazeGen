package generators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class EnchantmentShrineChunkGen extends ChunkGen{
	public EnchantmentShrineChunkGen(World world) {
		super("Enchantment Shrine", ChunkType.SHRINE, false, 1, world);
	}
	
	@Override
	public ChunkData generate(ChunkData chunkData, int chunkX, int chunkZ) {
		Random random = this.createRandom(chunkX, chunkZ);
		for(int i = 0; i <= random.nextInt(5) + 15; i++){
			int platformX = 1 + random.nextInt(14);
			int platformZ = 1 + random.nextInt(14);
			int platformWidth = 1 + random.nextInt(2);
			int platformLength = 1 + random.nextInt(2);
			int platformHeight = 2 + random.nextInt(11);
			chunkData.setRegion(platformX, baseHeight + 1, platformZ, platformX + platformWidth, baseHeight + platformHeight + 1, platformZ + platformLength, Material.BOOKSHELF);
		}
		chunkData.setRegion(5, baseHeight + 1, 5, 11 + 1, baseHeight + wallHeight + 1 , 11 + 1, Material.AIR);
		chunkData.setRegion(5, baseHeight + 1, 5, 11 + 1, baseHeight + 1, 11 + 1, baseMaterial);
		chunkData.setRegion(6, baseHeight + 1, 6, 10 + 1, baseHeight + 3, 10 + 1, Material.BOOKSHELF);
		chunkData.setRegion(8, baseHeight + 2, 6, 8 + 1, baseHeight + 3, 10 + 1, Material.AIR);
		chunkData.setRegion(6, baseHeight + 2, 8, 10 + 1, baseHeight + 3, 8 + 1, Material.AIR);
		chunkData.setRegion(7, baseHeight + 2, 7, 9 + 1, baseHeight + 3, 9 + 1, Material.AIR);
		chunkData.setBlock(8, baseHeight + 2, 8, Material.ENCHANTING_TABLE);
		return chunkData;
	}
}
