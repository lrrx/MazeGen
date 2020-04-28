package generators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import core.ChunkGen;
import core.ChunkType;
import core.NoiseGen;

public class ForestChunkGen extends ChunkGen{
	public ForestChunkGen(World world, int chunkX, int chunkZ) {
		super("Inner Forest", ChunkType.FOREST, false, 1, chunkX, chunkZ, world);
	}

	public double forestEdgeSmoothingFunction(double x) {
		double startPoint = 0.7;
		double slope = 16;
		
		double endPoint = startPoint + 1/slope;
		
		if(x < startPoint) {
			return 0.5;//1.0D;
		}
		else if(startPoint <= x && x <= endPoint) {
			return 0.5;// -slope * (x - startPoint) + 1;
		}
		return 0.0D;
	}
	
	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = this.createRandom(chunkX, chunkZ);

		//clear ground for landscape
		chunkData.setRegion(0, baseHeight - 8, 0, 16, baseHeight + 1, 16, Material.AIR);

		//generate landscape
		
		SimplexOctaveGenerator rootsGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		rootsGenerator.setScale(0.01D);
		
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		generator.setScale(0.025D);

		SimplexOctaveGenerator detailGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		detailGenerator.setScale(0.3D);
		
		SimplexOctaveGenerator leavesGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		leavesGenerator.setScale(0.08D);
		
		SimplexOctaveGenerator leavesGeneratorBig = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		leavesGeneratorBig.setScale(0.02D);

		for(int x = 0; x <= 16; x++) {
			for(int z = 0; z <= 16; z++) {
				double largeNoise = NoiseGen.largeNoise(chunkX * 16 + x, chunkZ * 16 + z, world);
				int noise = (int) (generator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.2D, 0.4D) * 3);
				int detailNoise = (int) (detailGenerator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.2D, 0.4D) * 3);
				int leavesNoise = (int) (leavesGenerator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.5D, 0.2D) * 4 * forestEdgeSmoothingFunction(largeNoise));
				int leavesNoiseBig = (int) (leavesGeneratorBig.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.5D, 0.5D, true) * 5);
				
				boolean doTrunkGeneration = false;
				
				if (leavesNoiseBig >= 1) {
					doTrunkGeneration = true;
				}
				
				int leavesBaseHeight = 7;

				int groundHeight = baseHeight + noise;

				Material top = Material.GRASS_BLOCK;;

				if (detailNoise >= 2) {
					double d = random.nextDouble();

					if (0.0 <= d && d <= 0.3) {
						top = Material.DIRT;
					}
					else if (0.3 < d && d <= 0.5) {
						top = Material.COARSE_DIRT;
					}
					else if (0.5 < d && d <= 0.7) {
						top = Material.PODZOL;
					}
					else if (0.7 < d && d <= 1.0) {
						if (random.nextDouble() >= 0.7) {
							top = Material.COBBLESTONE;
						}
						else {
							top = Material.MOSSY_COBBLESTONE;
						}
						if (random.nextDouble() >= 0.75) {
							chunkData.setBlock(x, groundHeight + 1, z, top);
						}
					}
				}

				chunkData.setRegion(x, baseHeight - 32 - Math.abs(noise), z, x + 1, groundHeight, z + 1, Material.DIRT);
				chunkData.setBlock(x, groundHeight, z, top);
				
				if (detailNoise >= 2 || detailNoise <= 0) {
					double n = random.nextDouble();

					Material deco = Material.GRASS;
					
					if (0 <= n && n < 0.02) {
						deco = flowerTypes[random.nextInt(flowerTypes.length - 1)];
					}
					else if (0.05 <= n && n < 0.3) {
						deco = Material.TALL_GRASS;
					}

					chunkData.setBlock(x, groundHeight + 1, z, deco);
				}
				
				int leavesMidPoint = groundHeight + leavesBaseHeight + leavesNoiseBig;
				
				if (leavesNoise > 0) {
					
					//create leaves
					chunkData.setRegion(x, leavesMidPoint - leavesNoise, z, x + 1, leavesMidPoint + leavesNoise + Math.abs(leavesNoiseBig) + random.nextInt(2), z + 1, Material.OAK_LEAVES);
					
					//create trunk
					if(random.nextDouble() <= 0.01) {
						chunkData.setRegion(x, groundHeight, z, x + 1, leavesMidPoint + leavesNoise - 2, z + 1, Material.OAK_LOG);
					}
				}

				//generate tree roots
				int rootsOffset = 32;
				for (int i = 0; i < 12; i ++) {
					int n = (int) (Math.pow(Math.abs(rootsGenerator.noise(chunkX * 16 + x + i * rootsOffset, chunkZ * 16 + z + i * rootsOffset, 0.3D, 0.5D, true)) * 70 + 1, 2));
					if (n == 1) {
						chunkData.setBlock(x, groundHeight + (int) (leavesNoise / 1.8) - 2, z, Material.STRIPPED_OAK_WOOD);
						break;	
					}
				}
				
				//generate random debree
				if (random.nextDouble() <= 0.8) {
					Material b = Material.COBBLESTONE;
					
					if (random.nextDouble() <= 0.3) {
						b = Material.GRAVEL;
					}
					else {
						if (random.nextDouble() <= 0.4) {
							b = Material.INFESTED_COBBLESTONE;
						}
						else {
							if (random.nextDouble() <= 0.15) {
								b = Material.COAL_BLOCK;
							}
							else {
								b = Material.MOSSY_COBBLESTONE;
							}
						}
					}
					chunkData.setBlock(x, groundHeight - 16 + random.nextInt(14 + noise), z, b);
				}
			}
		}
		return chunkData;
	}
}
