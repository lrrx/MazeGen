package core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
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
		return Arrays.asList((BlockPopulator)new MazePopulator(this.baseHeight, this.wallHeight, this.baseMaterial, world));
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

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {

		//get chunkData reference for chunk
		ChunkData chunkData = createChunkData(world);

		//prepare chunkNoise for use in this chunk -> store it in variable to only have one call to SimplexOctaveGenerator to save performance
		double chunkNoise = NoiseGen.noise(chunkX, chunkZ, world);

		//generate higher walls in certain areas based on chunkNoise
		if (((int) Math.abs(NoiseGen.largeNoise(chunkX, chunkZ, world) * 8D)) >= 7) {
			wallHeight =  16 + 16;
		}
		
		//use chunkNoise as the seed for random generation in this chunk
		random = new Random((long) (chunkNoise * 2147483647D));

		//Generate Ground
		chunkData.setRegion(0, 0, 0, 16, baseHeight + 1, 16, baseMaterial);

		long startNanos = System.nanoTime();
		
		if (debugEnabled) {
			System.out.println("chunkX: " + chunkX + "chunkZ: " + chunkZ + "Noise: " + chunkNoise);
		}

		//choose a chunk generator
		ChunkGen cg = GeneratorChooser.getChunkGen(chunkX, chunkZ, highwaysEnabled, spawnSize, chunkNoise, world);
		
		//let cg generate into chunkData
		chunkData = cg.generate(chunkData);
		
		if (debugEnabled) {
			System.out.println("Nanos: " + (System.nanoTime() - startNanos) + "\t Type: " + cg.getName());
		}

		//generate walls, but not within forests or near spawn
		if ((((int) (chunkNoise * 4D)) < 2) && !(Math.abs(chunkX) <= spawnSize && Math.abs(chunkZ) <= spawnSize)){
			chunkData = generateWalls(chunkX, chunkZ, random, world, chunkData);
		}
		
		//finally, return the generated chunkData
		return chunkData;
	}
}	