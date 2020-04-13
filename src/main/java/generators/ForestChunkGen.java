package generators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import core.ChunkGen;
import core.ChunkType;

public class ForestChunkGen extends ChunkGen{
	public ForestChunkGen(World world, int chunkX, int chunkZ) {
		super("Forest", ChunkType.FOREST, false, 1, chunkX, chunkZ, world);
	}

	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = this.createRandom(chunkX, chunkZ);

		//clear ground for landscape
		chunkData.setRegion(0, baseHeight - 8, 0, 16, baseHeight + 1, 16, Material.AIR);

		//generate landscape
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		generator.setScale(0.025D);

		SimplexOctaveGenerator detailGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		detailGenerator.setScale(0.3D);

		for(int x = 0; x <= 16; x++) {
			for(int z = 0; z <= 16; z++) {
				int noise = (int) (generator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.2D, 0.4D) * 3);

				int detailNoise = (int) (detailGenerator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.2D, 0.4D) * 3);
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
							chunkData.setBlock(x, baseHeight + noise + 1, z, top);
						}
					}
				}


				chunkData.setRegion(x, baseHeight - 8 - Math.abs(noise), z, x + 1, baseHeight + noise, z + 1, Material.DIRT);
				chunkData.setBlock(x, baseHeight + noise, z, top);
			}
		}

		return chunkData;
	}
	
	@Override
	public void populate(Chunk chunk) {
		Random random = this.createRandom(chunkX, chunkZ);
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		generator.setScale(0.025D);
		int amount = random.nextInt(3) + 2;  // Amount of trees
		for (int i = 1; i < amount; i++) {
			int X = random.nextInt(15);
			int Z = random.nextInt(15);
			int Y;
			
			int noise = (int) (generator.noise(chunk.getX() * 16 + X, chunk.getZ() * 16 + Z, 0.2D, 0.4D) * 3);
			Y = noise + baseHeight;

			world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.TREE);
		}
	}
}
