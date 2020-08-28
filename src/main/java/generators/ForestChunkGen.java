package generators;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import core.ChunkGen;
import core.ChunkType;
import core.GeneratorChooser;
import core.NoiseGen;

public class ForestChunkGen extends ChunkGen{
	public ForestChunkGen(World world, int chunkX, int chunkZ) {
		super("Inner Forest", ChunkType.FOREST, false, 1, chunkX, chunkZ, world);
	}

	private static int[][] isForestEdge(int chunkX, int chunkZ, World world) {
		int[][] isForest = {
				{1, 1},
				{1, 1}
		};

		for (int x = 0; x <= 1; x++) {
			for (int z = 0; z <= 1; z++) {
				int xOffset = -1 + 2 * x;
				int zOffset = -1 + 2 * z;

				if(!GeneratorChooser.isForest(chunkX + xOffset, chunkZ + zOffset, world)) {
					isForest[x][z] = 0;
				}
				else if (!GeneratorChooser.isForest(chunkX + xOffset, chunkZ, world)) {
					isForest[x][z] = 0;
				}
				else if(!GeneratorChooser.isForest(chunkX, chunkZ + zOffset, world)) {
					isForest[x][z] = 0;
				}
			}
		}

		return isForest;
	}

	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = this.createRandom(chunkX, chunkZ);

		//clear ground for landscape
		chunkData.setRegion(0, baseHeight - 8, 0, 16, baseHeight + 1, 16, Material.AIR);

		int isForest[][] = isForestEdge(chunkX, chunkZ, world);

		int isForestSum = 0;

		for (int xOffset = 0; xOffset <= 1; xOffset++) {
			for (int zOffset = 0; zOffset <= 1; zOffset++) {
				isForestSum += isForest[xOffset][zOffset];
			}
		}

		boolean isForestEdge = (isForestSum != 4);


		//Initialize Noise Generators
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

		for(int x = 0; x <= 15; x++) {
			for(int z = 0; z <= 15; z++) {

				//Calculate Noise values for each x-z Point
				double largeNoise = NoiseGen.largeNoise(chunkX * 16 + x, chunkZ * 16 + z, world);
				int noise = (int) (generator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.2D, 0.4D) * 3);
				int detailNoise = (int) (detailGenerator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.2D, 0.4D) * 3);
				int leavesNoise = (int) (leavesGenerator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.5D, 0.2D) * 4);
				int leavesNoiseBig = (int) (leavesGeneratorBig.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.5D, 0.5D, true) * 5);

				boolean doTrunkGeneration = false; 

				//enable trunk generation if leaves are thick enough
				if (leavesNoiseBig >= 1) {
					doTrunkGeneration = true;
				}
				//TODO: increase tree height to at least 16
				int leavesBaseHeight = 8;

				int groundHeight = baseHeight + noise;

				//cache bilinear interpolation with edges value for x-z Point

				double forestEdgeBilerpValue = 1;

				//interpolate ground with forest edges
				if (isForestEdge) {
					forestEdgeBilerpValue = bilerp(
							(double)isForest[0][0],
							(double)isForest[0][1],
							(double)isForest[1][0],
							(double)isForest[1][1],
							(double)z / 16,
							(double)x / 16
							);

					groundHeight = baseHeight + (int) Math.round(noise * forestEdgeBilerpValue);
				}

				//add random decal like stones and patches of dirt
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

				//fill ground
				chunkData.setRegion(x, baseHeight - 32 - Math.abs(noise), z, x + 1, groundHeight, z + 1, Material.DIRT);

				//set top decal block(int)(leavesNoise * forestEdgeBilerpValue)
				chunkData.setBlock(x, groundHeight, z, top);

				//generate grass and flowers based on noise
				if (detailNoise >= 2 || detailNoise <= 0) {
					double n = random.nextDouble();

					Material deco = Material.GRASS;

					if (0 <= n && n < 0.02) {
						deco = flowerTypes[random.nextInt(flowerTypes.length - 1)];
					}
					else if (0.05 <= n && n < 0.3) {

						//Bisected bs = (Bisected)Material.TALL_GRASS.createBlockData();
						//bs.getHalf(Bisected.Half.TOP);
						deco = Material.TALL_GRASS;
					}

					chunkData.setBlock(x, groundHeight + 1, z, deco);
				}

				//generate leaves on a height and with a thickness that are both noise-based
				int leavesMidPoint = groundHeight + leavesBaseHeight + leavesNoiseBig;

				if (leavesNoise > 0) {

					int lowerLeafEnd = leavesMidPoint - (int)(leavesNoise * forestEdgeBilerpValue) ;

					int upperLeafEnd =  leavesMidPoint + (int)((leavesNoise + Math.abs(leavesNoiseBig) + random.nextInt(2)) * forestEdgeBilerpValue);

					//create leaves
					//chunkData.setRegion(x, lowerLeafEnd, z, x + 1, upperLeafEnd, z + 1, Material.OAK_LEAVES);
					//chunkData.setRegion(x, lowerLeafEnd, z, x + 1, upperLeafEnd, z + 1, Bukkit.createBlockData("minecraft:oak_leaves[persistent=true]"));

					//create trunks
					if(random.nextDouble() <= 0.01) {
						chunkData.setRegion(x, groundHeight, z, x + 1, groundHeight + (int)(((leavesBaseHeight + leavesNoiseBig) * forestEdgeBilerpValue)) - 2, z + 1, Material.OAK_LOG);
					}
				}

				//demonstrate bilinear interpolation on forests edges for debugging etc
				/*if (isForestEdge == true) {
					chunkData.setBlock(x,  112 + (int)(forestEdgeBilerpValue * 16), z, Material.BEDROCK);
				}*/


				//generate tree roots
				int rootsOffset = 32;
				for (int i = 0; i < 12; i ++) {
					int n = (int) (Math.pow(Math.abs(rootsGenerator.noise(chunkX * 16 + x + i * rootsOffset, chunkZ * 16 + z + i * rootsOffset, 0.3D, 0.5D, true)) * 70 + 1, 2));
					if (n == 1) {
						chunkData.setBlock(x, groundHeight + (int) (leavesNoise / 1.8) - 2, z, Material.STRIPPED_OAK_WOOD);
						break;	
					}
				}

				//generate random debris underground
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

	@Override
	public void populate(Chunk chunk) {
		Random random = this.createRandom(chunkX, chunkZ);
		
		int isForest[][] = isForestEdge(chunkX, chunkZ, world);

		int isForestSum = 0;

		for (int xOffset = 0; xOffset <= 1; xOffset++) {
			for (int zOffset = 0; zOffset <= 1; zOffset++) {
				isForestSum += isForest[xOffset][zOffset];
			}
		}

		boolean isForestEdge = (isForestSum != 4);

		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		generator.setScale(0.025D);

		SimplexOctaveGenerator detailGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		detailGenerator.setScale(0.3D);

		SimplexOctaveGenerator leavesGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		leavesGenerator.setScale(0.08D);

		SimplexOctaveGenerator leavesGeneratorBig = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		leavesGeneratorBig.setScale(0.02D);	
		
		for(int x = 0; x <= 15; x++) {
			for(int z = 0; z <= 15; z++) {
				
				//Calculate Noise values for each x-z Point
				double largeNoise = NoiseGen.largeNoise(chunkX * 16 + x, chunkZ * 16 + z, world);
				int noise = (int) (generator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.2D, 0.4D) * 3);
				int detailNoise = (int) (detailGenerator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.2D, 0.4D) * 3);
				int leavesNoise = (int) (leavesGenerator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.5D, 0.2D) * 4);
				int leavesNoiseBig = (int) (leavesGeneratorBig.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.5D, 0.5D, true) * 5);

				boolean doTrunkGeneration = false;

				//enable trunk generation if leaves are thick enough
				if (leavesNoiseBig >= 1) {
					doTrunkGeneration = true;
				}
				//TODO: increase tree height to at least 16
				int leavesBaseHeight = 8;

				int groundHeight = baseHeight + noise;

				//cache bilinear interpolation with edges value for x-z Point

				double forestEdgeBilerpValue = 1;

				//interpolate ground with forest edges
				if (isForestEdge) {
					forestEdgeBilerpValue = bilerp(
							(double)isForest[0][0],
							(double)isForest[0][1],
							(double)isForest[1][0],
							(double)isForest[1][1],
							(double)z / 16,
							(double)x / 16
							);

					groundHeight = baseHeight + (int) Math.round(noise * forestEdgeBilerpValue);
				}

				//generate leaves on a height and with a thickness that are both noise-based
				int leavesMidPoint = groundHeight + leavesBaseHeight + leavesNoiseBig;

				if (leavesNoise > 0) {

					int lowerLeafEnd = leavesMidPoint - (int)(leavesNoise * forestEdgeBilerpValue) ;

					int upperLeafEnd =  leavesMidPoint + (int)((leavesNoise + Math.abs(leavesNoiseBig) + random.nextInt(2)) * forestEdgeBilerpValue);
					
					for(int y = lowerLeafEnd; y < upperLeafEnd; y++) {
						Block block = chunk.getBlock(x, y, z);
						block.setType(Material.OAK_LEAVES, false);
						Leaves leaf = (Leaves) block.getBlockData();
				        leaf.setPersistent(true);
				        block.setBlockData(leaf, false);
					}
				}
			}
		}
	}
}
