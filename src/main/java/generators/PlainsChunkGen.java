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
import core.NoiseGen;

public class PlainsChunkGen extends ChunkGen{
	public PlainsChunkGen(World world, int chunkX, int chunkZ) {
		super("Forest", ChunkType.FOREST, false, 1, chunkX, chunkZ, world);
	}

	public double terrainTransformFunction(double x) {
		return ((1024 * Math.pow((x - 1) , 6) - 24 ));
	}
	
	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = this.createRandom(chunkX, chunkZ);

		//generate landscape
		
		//replace 
		//SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 4);
		//generator.setScale(0.005D);
		
		SimplexOctaveGenerator bumpsGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 2);
		bumpsGenerator.setScale(0.03D);
		
		SimplexOctaveGenerator detailGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 4);
		detailGenerator.setScale(0.3D);

		for(int x = 0; x <= 16; x++) {
			for(int z = 0; z <= 16; z++) {
				int noise = (int) (terrainTransformFunction(NoiseGen.largeNoise(chunkX * 16 + x, chunkZ * 16 + z, world)));
				int bumpsNoise = (int) (bumpsGenerator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.5D, 0.5D, true) * 2);
				int detailNoise = (int) (detailGenerator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.2D, 0.4D, true) * 3);
				Material top = Material.SANDSTONE;;

				int groundHeight = noise + bumpsNoise;
				
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
							top = Material.RED_SAND;
						}
						if (random.nextDouble() >= 0.95) {
							chunkData.setRegion(x, baseHeight + groundHeight + 1, z, x + 1, baseHeight + groundHeight + random.nextInt(5) + 4, z + 1, top);
						}
					}
				}


				chunkData.setRegion(x, baseHeight - 8 - Math.abs(groundHeight), z, x + 1, baseHeight + groundHeight + 1, z + 1, Material.SANDSTONE);
				chunkData.setBlock(x, baseHeight + noise, z, top);
			}
		}

		return chunkData;
	}
}
