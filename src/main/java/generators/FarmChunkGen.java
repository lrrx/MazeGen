package generators;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class FarmChunkGen extends ChunkGen{
	public FarmChunkGen(World world, int chunkX, int chunkZ) {
		super("Farm Chunk", ChunkType.RESOURCES, false, 1, chunkX, chunkZ, world);
	}

	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = createRandom(chunkX, chunkZ);

		Map<Material, Double> cropMaterialMap = new HashMap<Material, Double>();

		cropMaterialMap.put(Material.POTATOES, 0.2);
		cropMaterialMap.put(Material.WHEAT, 0.4);
		cropMaterialMap.put(Material.GRASS, 0.8);
		cropMaterialMap.put(Material.CARROTS, 0.2);

		Object[] cropMaterials = cropMaterialMap.keySet().toArray();

		int posX = 4 + random.nextInt(3);
		int posZ = 4 + random.nextInt(3);

		chunkData.setRegion(1, baseHeight - 3, 1, 16, baseHeight, 16, Material.DIRT);
		chunkData.setRegion(1, baseHeight, 1, 16, baseHeight + 1, 16, Material.GRASS_BLOCK);
		chunkData.setRegion(posX, baseHeight, posZ, 16 - posX, baseHeight + 1, 16 - posX, Material.AIR);
		chunkData.setRegion(posX, baseHeight - posZ, 1, 16 - posX, baseHeight, 16 - posZ, Material.FARMLAND);

		for(int x = 1; x <= 15; x++) {
			for(int z = 1; z <= 15; z++) {

				if(random.nextDouble() <= 0.2) {
					chunkData.setBlock(x, baseHeight - 1, z, Material.GRASS_BLOCK);
				}
				else if(random.nextDouble() <= 0.2) {
					chunkData.setBlock(x, baseHeight - 1, z, Material.WATER);
				}
				else if(random.nextDouble() <= 1 - 0.12 * 0.5 * (Math.abs(x - 8) + Math.abs(z - 8))){
					int randomIndex = random.nextInt(cropMaterials.length);
					Material currentMaterial = (Material) cropMaterials[randomIndex];

					if(cropMaterialMap.get(currentMaterial) >= random.nextDouble()) {
						chunkData.setBlock(x, baseHeight, z, currentMaterial);
						chunkData.setBlock(x, baseHeight - 1, z, Material.FARMLAND);
					}
				}
			}
		}
		return chunkData;
	}

	@Override
	public void populate(Chunk chunk) {
		Random random = createRandom(chunk.getX(), chunk.getZ());

		int posX = 4 + random.nextInt(3);
		int posZ = 4 + random.nextInt(3);
		
		for(int x = 1; x <= 15; x++) {
			for(int z = 1; z <= 15; z++) {

				if(random.nextDouble() <= 0.2) {
					world.generateTree(chunk.getBlock(8 + posX, baseHeight + 2, 8 + posZ).getLocation(), TreeType.DARK_OAK);
				}
			}
		}
	}
}
