package generators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class StoneMineChunkGen extends ChunkGen{
	public StoneMineChunkGen(World world, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
		super("Stone Mine", ChunkType.RESOURCES, false, 1, chunkX, chunkZ, world, biomeGrid);
	}

	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = this.createRandom();
		//generate Underground Stone
		
		int mineDepth = 8;
		chunkData.setRegion(1, baseHeight - mineDepth, 1, 16, baseHeight , 16, Material.STONE);

		//generate Surface Rocks
		for(int i = 0; i <= random.nextInt(10) + 15; i++){
			int platformX = 2 + random.nextInt(13);
			int platformZ = 2 + random.nextInt(13);
			int platformWidth = 1 + random.nextInt(3);
			int platformLength = platformWidth + random.nextInt(2) - 1;
			int platformHeight = 1 + random.nextInt(4);
			chunkData.setRegion(platformX, baseHeight, platformZ, platformX + platformWidth, baseHeight + platformHeight, platformZ + platformLength, Material.STONE);
		}

		//generate Ores
		//LinkedHashMap<Material, Double> oreChanceMap = new LinkedHashMap<Material, Double>();
		/*oreChanceMap.put(Material.LAPIS_ORE,	0.00060D);
		    	oreChanceMap.put(Material.DIAMOND_ORE, 	0.00080D);
		    	oreChanceMap.put(Material.GOLD_ORE, 	0.00130D);
		    	oreChanceMap.put(Material.IRON_ORE, 	0.00400D);
		    	oreChanceMap.put(Material.REDSTONE_ORE, 0.00600D);
		    	oreChanceMap.put(Material.COAL_ORE, 	0.00800D);*/

		//oreChanceMap.entrySet().sorted(Map.Entry.comparingByValue()).forEachOrdered;
		// * (1+2+3+4+5+6+7+8) * 0.125 * (12/60) * 8 * 16 * 16 * 2;
		/*Set<Material> ks = oreChanceMap.keySet();
		    	Collection<Double> vs = oreChanceMap.values();*/

		Material[] oreArray = {
				Material.GRAVEL,
				Material.COAL_ORE,
				Material.REDSTONE_ORE,
				Material.IRON_ORE,
				Material.GOLD_ORE,
				Material.DIAMOND_ORE,
				Material.LAPIS_ORE
		};

		double[] chanceSumArray = {
				0.0307,
				0.0210,
				0.0130,
				0.0070,
				0.0030,
				0.0014,
				0.0006,
				0.0
		};
		
		double rarityFactor = 1.2;
		//generate upper ores
		for(int x = 1; x <= 16; x++) {
			for(int y = baseHeight; y <= baseHeight + 4; y++) {
				for(int z = 1; z <= 16; z++) {
					if(chunkData.getBlockData(x, y, z).getMaterial() == Material.STONE) {
						double rnd = random.nextDouble();
						if(rnd * rarityFactor <= chanceSumArray[0]) {
							for(int i = 0; i <= 6; i++) {
								if(rnd * rarityFactor <= chanceSumArray[i] && rnd >= chanceSumArray[i + 1]){
									chunkData.setBlock(x, y, z, oreArray[i]);
								}
							}
						}
					}
				}
			}
		}

		//Generate lower ores
		for(int x = 1; x <= 16; x++) {
			for(int y = baseHeight - mineDepth; y <= baseHeight - 1; y++) {
				for(int z = 1; z <= 16; z++) {
					double rnd = random.nextDouble();
					if(rnd * rarityFactor <= chanceSumArray[0]) {
						for(int i = 0; i <= 6; i++) {
							if(rnd * rarityFactor <= chanceSumArray[i] && rnd >= chanceSumArray[i + 1]){
								chunkData.setBlock(x, y, z, oreArray[i]);
							}
						}
					}

				}
			}
		}
		return chunkData;
	}
}
