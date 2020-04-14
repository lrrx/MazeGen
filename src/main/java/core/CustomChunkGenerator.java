package core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import generators.*;

@SuppressWarnings("unused")
public class CustomChunkGenerator extends ChunkGenerator {

	//toggle verbose logging
	private boolean debugEnabled = false;

	//height of ground
	private int baseHeight = 16;

	//height of normal walls
	private int wallHeight = 16;

	//main maze material
	private Material baseMaterial = Material.BEDROCK;

	//toggle highways
	boolean highwaysEnabled = false;

	//size of the empty spawn area
	private int spawnSize = 2;

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		//maze populator hook
		return  Arrays.asList((BlockPopulator)new MazePopulator(this.baseHeight, this.wallHeight, this.baseMaterial, world));
	}

	public ChunkData generateXWall(ChunkData chunkData) {
		chunkData.setRegion(0, baseHeight + 1, 0, 1, baseHeight + wallHeight + 1, 7, baseMaterial);
		chunkData.setRegion(0, baseHeight + 1, 10, 1, baseHeight + wallHeight + 1, 16, baseMaterial);
		return chunkData;
	}

	public ChunkData generateZWall(ChunkData chunkData) {
		chunkData.setRegion(0, baseHeight + 1, 0, 7, baseHeight + wallHeight + 1, 1, baseMaterial);
		chunkData.setRegion(10, baseHeight + 1, 0, 16, baseHeight + wallHeight + 1, 1, baseMaterial);
		return chunkData;
	}

	public ChunkData closeXWall(ChunkData chunkData) {
		chunkData.setRegion(0, baseHeight + 1, 7, 0 + 1, baseHeight + wallHeight - 1, 9 + 1, baseMaterial);
		return chunkData;
	}

	public ChunkData closeZWall(ChunkData chunkData) {
		chunkData.setRegion(7, baseHeight + 1, 0, 9 + 1, baseHeight + wallHeight - 1, 0 + 1, baseMaterial);
		return chunkData;
	}

	public ChunkData generateWalls(int chunkX, int chunkZ, Random random, World world, ChunkData chunkData) {

		boolean omitX = random.nextInt(1000) <= 60;
		boolean omitZ = random.nextInt(1000) <= 60;

		if(chunkX % 256 == 0 && chunkZ % 256 == 0 && highwaysEnabled) {
			//no walls
		}
		else if(chunkX % 256 == 0 && highwaysEnabled) {
			chunkData = generateXWall(chunkData);
		}
		else if(chunkZ % 256 == 0 && highwaysEnabled) {
			chunkData = generateZWall(chunkData);
		}
		else {
			if (!(omitX)) {
				chunkData = generateXWall(chunkData);
			}
			if (!(omitZ)) {
				chunkData = generateZWall(chunkData);
			}
		}

		//close off some walls
		if(chunkX % 256 == 0 && chunkZ % 256 == 0 && highwaysEnabled) {
			//no closed walls
		}
		else if (chunkX % 256 == 0 && highwaysEnabled) {
			if (!(chunkZ % 32 == 0)) {
				chunkData = closeXWall(chunkData);
			}
		}
		else if (chunkZ % 256 == 0 && highwaysEnabled) {
			if (!(chunkX % 32 == 0)) {
				chunkData = closeZWall(chunkData);
			}
		}
		else if (((chunkX > 0 && chunkX % 256 == 1) || (chunkX < 0 && ((chunkX - 1)% 256) == 0)) && highwaysEnabled) {
			if (!(chunkZ % 32 == 0)) {
				chunkData = closeXWall(chunkData);
			}
			if(random.nextBoolean() == true) {
				chunkData = closeZWall(chunkData);
			}
		}
		else if (((chunkZ > 0 && chunkZ % 256 == 1) || (chunkZ < 0 && ((chunkZ - 1)% 256) == 0)) && highwaysEnabled) {
			if (!(chunkX % 32 == 0)) {
				chunkData = closeZWall(chunkData);
			}
			if(random.nextBoolean() == true) {
				chunkData = closeXWall(chunkData);
			}
		}
		else if(random.nextBoolean() == true) {
			if(random.nextBoolean() == true && (!omitX)) {
				chunkData = closeXWall(chunkData);
			}
			else if ((!omitZ)) {
				chunkData = closeZWall(chunkData);
			}
		}

		return chunkData;
	}

	public ChunkData generatePylon (ChunkData chunkData, boolean doUnderground) {
		int width = 2;
		int bottomZ = baseHeight;
		int topZ = baseHeight + 64;
		
		if (doUnderground) {
			bottomZ = 0;
		}
		
		chunkData.setRegion(8 - width, bottomZ, 8 - width, 9 + width, topZ + 1, 9 + width, baseMaterial);
		return chunkData;
	}

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {

		//get chunkData reference for chunk
		ChunkData chunkData = createChunkData(world);

		Material m = Material.BEDROCK;

		//for noise debugging
		/*for(int x = 0; x <= 15; x++) {
			for(int z = 0; z <= 15; z++) {
				double chunkNoise = NoiseGen.noise(chunkX * 16 + x, chunkZ * 16 + z, world);
				double chunkLargeNoise = NoiseGen.largeNoise(chunkX * 16 + x, chunkZ * 16 + z, world);
				m = Material.BEDROCK;
				if (GeneratorChooser.isPlainsChunk(chunkX * 16 + x, chunkZ * 16 + x, chunkNoise, chunkLargeNoise, world)) {
					m = Material.GRANITE;
				}
				else if (GeneratorChooser.isMazeChunk(chunkX * 16 + x, chunkZ * 16 + z, chunkNoise, world)) {
					m = Material.COAL_BLOCK;
				}
				else if (GeneratorChooser.isInnerForest(chunkX * 16 + x, chunkZ * 16 + z, chunkNoise, world)) {
					m = Material.COARSE_DIRT;
				}
				else if (GeneratorChooser.isForest(chunkX * 16 + x, chunkZ * 16 + z, chunkNoise, world)) {
					m = Material.GRASS_BLOCK;
				}

				chunkData.setBlock(x, 100, z, m);
				for(int y = 0; y <= 255; y++) {
					biomeGrid.setBiome(chunkX * 16 + x, y, chunkZ * 16 + z, Biome.SWAMP);
				}
			}
		}*/


		for(int x = 0; x <= 15; x++) {
			for(int z = 0; z <= 15; z++) {
				for(int y = 0; y <= 255; y++) {
					biomeGrid.setBiome(chunkX * 16 + x, y, chunkZ * 16 + z, Biome.SWAMP);
				}
			}
		}

		//prepare chunkNoise for use in this chunk -> store it in variable to only have one call to SimplexOctaveGenerator to save performance
		double chunkNoise = NoiseGen.noise(chunkX * 16, chunkZ * 16, world);

		double chunkLargeNoise = NoiseGen.largeNoise(chunkX * 16, chunkZ * 16, world);

		//use chunkNoise as the seed for random generation in this chunk
		random = new Random((long) (chunkNoise * 2147483647D));

		boolean doGroundPregeneration = !(GeneratorChooser.isForest(chunkX * 16, chunkZ * 16, chunkNoise, world) || GeneratorChooser.isPlainsChunk(chunkX, chunkZ, chunkNoise, chunkLargeNoise, world));

		boolean doWallPostgeneration = !(GeneratorChooser.isForest(chunkX * 16, chunkZ * 16, chunkNoise, world)
				|| GeneratorChooser.isPlainsChunk(chunkX, chunkZ, chunkNoise, chunkLargeNoise, world))
				&& !(Math.abs(chunkX) <= spawnSize && Math.abs(chunkZ) <= spawnSize);

		//Generate Ground
		if (doGroundPregeneration){
			chunkData.setRegion(0, 0, 0, 16, baseHeight + 1, 16, baseMaterial);
		}

		long startNanos = System.nanoTime();

		if (debugEnabled) {
			System.out.println("chunkX: " + chunkX + "chunkZ: " + chunkZ + "Noise: " + chunkNoise);
		}

		//choose a chunk generator
		ChunkGen cg = GeneratorChooser.getChunkGen(chunkX, chunkZ, highwaysEnabled, spawnSize, chunkNoise, chunkLargeNoise, world);

		//let cg generate into chunkData
		chunkData = cg.generate(chunkData);

		if (debugEnabled) {
			System.out.println("Nanos: " + (System.nanoTime() - startNanos) + "\t Type: " + cg.getName());
		}

		//generate walls, but not within forests or near spawn
		if (doWallPostgeneration){
			chunkData = generateWalls(chunkX, chunkZ, random, world, chunkData);
		}

		//generate pylons
		if ((chunkX % 32 == 0 && chunkZ % 4 == 0)
				|| (chunkZ % 32 == 0 && chunkX % 4 == 0)) {
			chunkData = generatePylon(chunkData, !doGroundPregeneration);
		}

		//make sure the void is closed off
		chunkData.setRegion(0, 0, 0, 16, 8, 16, baseMaterial);

		//finally, return the generated chunkData
		return chunkData;
	}

	@Override
	public boolean isParallelCapable() {
		return true;
	}

	@Override
	public boolean shouldGenerateCaves() {
		return false;
	}

	@Override
	public boolean shouldGenerateDecorations() {
		return false;
	}

	@Override
	public boolean shouldGenerateMobs() {
		return true;
	}

	public boolean shouldGenerateStructures() {
		return false;
	}

	@Nullable
	public Location getFixedSpawnLocation​(@Nonnull World world, @Nonnull Random random) {
		return new Location(world, 0, baseHeight + 1, 0);
	}
}	