package generators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import core.ChunkGen;
import core.ChunkType;
import core.GeneratorChooser;
import core.NoiseGen;

public class PlainsChunkGen extends ChunkGen{
	public PlainsChunkGen(World world) {
		super("Plains", ChunkType.NEUTRAL, false, 1, world);
	}

	private static int[][] isPlainsEdge(int chunkX, int chunkZ, World world) {
		int[][] isPlains = {
				{1, 1},
				{1, 1}
		};

		for (int x = 0; x <= 1; x++) {
			for (int z = 0; z <= 1; z++) {
				int xOffset = -1 + 2 * x;
				int zOffset = -1 + 2 * z;

				if(!GeneratorChooser.isPlainsChunk(world, chunkX + xOffset, chunkZ + zOffset)) {
					isPlains[x][z] = 0;
				}
				else if (!GeneratorChooser.isPlainsChunk(world, chunkX + xOffset, chunkZ)) {
					isPlains[x][z] = 0;
				}
				else if(!GeneratorChooser.isPlainsChunk(world, chunkX, chunkZ + zOffset)) {
					isPlains[x][z] = 0;
				}
			}
		}

		return isPlains;
	}
	
	private double terrainTransformFunction(double x) {
		return ((8096 * Math.pow((x - 1) , 8) - 32 ));
	}

	@Override
	public ChunkData generate(ChunkData chunkData, int chunkX, int chunkZ) {
		Random random = this.createRandom(chunkX, chunkZ);

		int isPlains[][] = isPlainsEdge(chunkX, chunkZ, world);

		int isPlainsSum = 0;

		for (int xOffset = 0; xOffset <= 1; xOffset++) {
			for (int zOffset = 0; zOffset <= 1; zOffset++) {
				isPlainsSum += isPlains[xOffset][zOffset];
			}
		}

		boolean isPlainsEdge = (isPlainsSum != 4);

		SimplexOctaveGenerator cracksGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 4);
		cracksGenerator.setScale(0.001D);

		SimplexOctaveGenerator bumpsGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 6);
		bumpsGenerator.setScale(0.03D);

		SimplexOctaveGenerator detailGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 4);
		detailGenerator.setScale(0.3D);

		for(int x = 0; x <= 16; x++) {
			for(int z = 0; z <= 16; z++) {
				boolean generateCrack = false;

				int noise = (int) (terrainTransformFunction(NoiseGen.largeNoise(chunkX * 16 + x, chunkZ * 16 + z, world))); //=-16
				int bumpsNoise = (int) (bumpsGenerator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.5D, 0.5D, true) * 10);

				int cracksNoise;

				int cracksOffset = 8096;
				for (int i = 0; i < 4; i ++) {
					int n = (int) (Math.pow(Math.abs(cracksGenerator.noise(chunkX * 16 + x + i * cracksOffset, chunkZ * 16 + z + i * cracksOffset, 0.5D, 0.5D, true)) * 200 + 1, 2));
					if (n == 1) {
						cracksNoise = n;
						generateCrack = true;
						break;	
					}
				}

				int detailNoise = (int) (detailGenerator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.2D, 0.4D, true) * 4);
				Material top = Material.SANDSTONE;

				int groundHeight = noise;// + bumpsNoise;

				if (generateCrack) {
					groundHeight = noise - Math.abs(bumpsNoise) - ((int) (Math.abs(detailNoise) / 2)) - 5;
				}
				else if (bumpsNoise >= 6 && noise <= -16) {
					groundHeight = noise - bumpsNoise + 5;
				}

				double plainsEdgeBilerpValue = bilerp(
						(double)isPlains[0][0],
						(double)isPlains[0][1],
						(double)isPlains[1][0],
						(double)isPlains[1][1],
						(double)z / 16,
						(double)x / 16
				);
				
				if (isPlainsEdge) {
					groundHeight = (int) Math.round(groundHeight * plainsEdgeBilerpValue);
				}
				
				if (detailNoise >= 2) {
					double d = random.nextDouble();

					if (0.0 <= d && d <= 0.3) {
						top = Material.SMOOTH_SANDSTONE;
					}
					else if (0.3 < d && d <= 0.5) {
						top = Material.SAND;
					}
					else if (0.5 < d && d <= 0.7) {
						top = Material.CUT_SANDSTONE;
					}
					else if (0.7 < d && d <= 1.0) {
						if (random.nextDouble() >= 0.7) {
							top = Material.RED_SANDSTONE;
						}
						else {
							top = Material.SAND;
						}
						if (random.nextDouble() >= 0.95) {
							chunkData.setRegion(x, baseHeight + groundHeight + 1, z, x + 1, baseHeight + groundHeight + 1 + random.nextInt(5) + 4, z + 1, top);
						}
					}
				}
				
				chunkData.setRegion(x, baseHeight - 8 - Math.abs(groundHeight), z, x + 1, baseHeight + groundHeight, z + 1, Material.SANDSTONE);
				chunkData.setBlock(x, baseHeight + groundHeight, z, top);
			}
		}
		return chunkData;
	}
}