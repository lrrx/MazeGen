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

	//enable or disable verbose logging
	private boolean debugEnabled = false;
	
	//height of ground
	private int baseHeight = 16;
	
	//height of normal walls
	private int wallHeight = 16;
	
	//main maze material
	private Material baseMaterial = Material.BEDROCK;
	
	//used to store chunk location based perlin noise
	private double chunkNoise;
	
	//specifies wether highways are enabled
	boolean highwaysEnabled = false;
	
	//specifies the size of the empty spawn area
	private int spawnSize = 2;
	
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		//maze populator hook
		return Arrays.asList((BlockPopulator)new MazePopulator(this.baseHeight, this.wallHeight, this.baseMaterial, world));
	}

	//normal-scale chunk location based perlin noise
	public double noise(int chunkX, int chunkZ, World world) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 4);
		generator.setScale(0.005D);
		return generator.noise(chunkX * 16 , chunkZ * 16 , 0.2D, 0.4D);
	}

	//large-scale chunk location based perlin noise
	public double largeNoise(int chunkX, int chunkZ, World world) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 4);
		generator.setScale(0.001D);
		return generator.noise(chunkX * 16 , chunkZ * 16 , 0.2D, 0.4D);
	}

	public ChunkGen getRandomChunkGenerator(int chunkX, int chunkZ, double chunkNoise2, World world, BiomeGrid biomeGrid) {
		
		ChunkGen cg = null;
		
		//generate random integer for room generation choice
		int n = Math.abs(((int) (chunkNoise2 * 2147483647D)) % 1300);
		
		//generate empty rooms for highways
		if((chunkX % 256 == 0 || chunkZ % 256 == 0) && highwaysEnabled) {
			cg = new EmptyChunkGen(world, chunkX, chunkZ, biomeGrid);
		}
		//generate empty rooms near spawn
		else if(Math.abs(chunkX) <= spawnSize && Math.abs(chunkZ) <= spawnSize) {
			 cg = new EmptyChunkGen(world, chunkX, chunkZ, biomeGrid);
		}
		//use chunkNoise to decide what type of room (Maze/Forest) to generate
		else if (((int) (chunkNoise2 * 4D)) == 1 || ((int) (chunkNoise2 * 4D)) == -1) {
			cg = new MazeChunkGen(world, chunkX, chunkZ, biomeGrid);
		}
		else if (((int) (chunkNoise2 * 4D)) >= 2) {
			cg = new ForestChunkGen(world, chunkX, chunkZ, biomeGrid);
		}
		//only generate LavaChunk if there is no forest close to it
		else if(1 <= n && n <= 30) {
			if (!forestIsNear(chunkX, chunkZ, world)){
				cg = new LavaChunkGen(world, chunkX, chunkZ, biomeGrid);
			}
		}
		//only generate LavaParkourChunk if there is no forest close to it
		else if (101 <= n && n <= 130) {
			if (!forestIsNear(chunkX, chunkZ, world)){
				cg = new LavaParkourChunkGen(world, chunkX, chunkZ, biomeGrid);
			}
		}
		else if (201 <= n && n <= 220) {
			cg = new StoneMineChunkGen(world, chunkX, chunkZ, biomeGrid);
		}
		else if (301 <= n && n <= 350) {
			cg = new PillarsChunkGen(world, chunkX, chunkZ, biomeGrid);
		}
		else if (401 <= n && n <= 410) {
			cg = new EnchantmentShrineChunkGen(world, chunkX, chunkZ, biomeGrid);
		}
		else if (501 <= n && n <= 530) {
			cg = new FountainChunkGen(world, chunkX, chunkZ, biomeGrid);
		}
		else if(601 <= n && n <= 630) {
			cg = new SpawnerChunkGen(world, chunkX, chunkZ, biomeGrid);
		}
		else if (701 <= n && n <= 730) {
			cg = new LootChunkGen(world, chunkX, chunkZ, biomeGrid);
		}
		else if (801 <= n && n <= 815) {
			cg = new LargeTowerChunkGen(world, chunkX, chunkZ, biomeGrid);
		}
		else if (901 <= n && n <= 930) {
			cg = new WaterChunkGen(world, chunkX, chunkZ, biomeGrid);
		}
		else if (1001 <= n && n <= 1010) {
			cg = new WaterHolesChunkGen(world, chunkX, chunkZ, biomeGrid);
		}
		else if (1101 <= n && n <= 1130) {
			cg = new LavaHolesChunkGen(world, chunkX, chunkZ, biomeGrid);
		}
		return cg;
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

	public boolean forestIsNear(int chunkX, int chunkZ, World world) {
		for(int x = -1; x <= 1; x++) {
			for(int z = -1; z <= 1; z++) {
				if (((int) (noise(chunkX + x, chunkZ + z, world) * 4D)) == 2) {
					if (debugEnabled) {
						System.out.println("Forest is Near! chunkX: " + chunkX + " chunkZ: " + chunkZ + " x: " + x + " z: " + z);
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {

		//get chunkData reference for chunk
		ChunkData chunkData = createChunkData(world);

		//prepare chunkNoise for use in this chunk -> store it in variable to only have one call to SimplexOctaveGenerator to save performance
		chunkNoise = noise(chunkX, chunkZ, world);

		//generate higher walls in certain areas based on chunkNoise
		if (((int) Math.abs(largeNoise(chunkX, chunkZ, world) * 8D)) >= 7) {
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
		ChunkGen cg = getRandomChunkGenerator(chunkX, chunkZ, chunkNoise, world, biomeGrid);
		
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