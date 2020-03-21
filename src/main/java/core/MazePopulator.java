package core;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import generators.EmptyChunkGen;
import structures.*;

public class MazePopulator extends BlockPopulator {

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
	
	//used to store World reference
	private World world;

	public MazePopulator(int baseHeight, int wallHeight, Material baseMaterial, World world) {
		this.baseHeight = baseHeight;
		this.wallHeight = wallHeight;
		this.baseMaterial = baseMaterial;
		this.world = world;
	}

	public void fillChunkRegion(int x1, int y1, int z1, int x2, int y2, int z2, Material material, Chunk chunk) {
		for(int x = x1; x <= x2; x++) {
			for(int y = y1; y <= y2; y++) {
				for(int z = z1; z <= z2; z++) {
					this.world.getBlockAt(chunk.getBlock(x, y, z).getLocation()).setType(material);
				}
			}
		}
	}

	public double noise(int chunkX, int chunkZ, World world) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 4);
		generator.setScale(0.005D);
		return generator.noise(chunkX * 16 , chunkZ * 16 , 0.2D, 0.4D);
	}

	public StructureGen getRandomStructureGenerator(Chunk chunk, World world) {

		//random number for room structure choice
		int n = Math.abs(((int) (chunkNoise * 2147483647D)) % 1300);

		//no structure as default choice
		StructureGen sg = new EmptyStructureGen(world);;

		//generate empty rooms for highways
		if((chunk.getX() % 256 == 0 || chunk.getZ() % 256 == 0) && highwaysEnabled) {
			//
		}
		else if (((int) (chunkNoise * 4D)) == 1 || ((int) (chunkNoise * 4D)) == -1) {
			//
		}
		//generate empty rooms near spawn
		else if(Math.abs(chunk.getX()) <= spawnSize && Math.abs(chunk.getZ()) <= spawnSize) {
			//
		}
		//use chunkNoise to decide when to populate a forest chunk
		else if (((int) (chunkNoise * 4D)) == 2) {
			sg = new ForestStructureGen(world);
		}
		else if (((int) (chunkNoise * 4D)) >= 3) {
			sg = new InnerForestStructureGen(world);
		}
		else if(1 <= n && n <= 30) {
			//chunkData = generateLavaFloor(chunkData);
		}
		else if (101 <= n && n <= 130) {
			//chunkData = generateLavaParkour(chunkData);
		}
		else if (201 <= n && n <= 220) {
			//chunkData = generateStoneRoom(chunkData);
		}
		else if (301 <= n && n <= 350) {
			//chunkData = generatePillarRoom(chunkData);
		}
		else if (401 <= n && n <= 410) {
			//chunkData = generateEnchantmentRoom(chunkData);
		}
		else if (501 <= n && n <= 530) {
			sg = new FountainStructureGen(world);
		}
		else if(601 <= n && n <= 630) {
			sg = new SpawnerStructureGen(world);
		}
		else if (701 <= n && n <= 730) {
			sg = new LootStructureGen(world);
		}
		else if (801 <= n && n <= 830) {
			//
		}
		else if (801 <= n && n <= 815) {
		}
		else if (901 <= n && n <= 930) {
		}
		else if (1001 <= n && n <= 1010) {
		}
		else if (1101 <= n && n <= 1130) {
		}
		else {
			//EmptyStructureGen
		}
		return sg;
	}

	@Override
	public void populate(World world, Random random, Chunk chunk) {

		//prepare chunkNoise for use in this chunk -> store it in variable to only have one call to SimplexOctaveGenerator to save performance
		chunkNoise = noise(chunk.getX(), chunk.getZ(), world);

		//use chunkNoise as the seed for random generation in this chunk
		random = new Random((long) (chunkNoise * 2147483647D));

		world.getBlockAt(chunk.getBlock(8, 255, 8).getLocation()).setType(Material.GLOWSTONE);
		
		//store random structure generator in sg
		StructureGen sg = getRandomStructureGenerator(chunk, world);

		//let the chosen structure generate run
		sg.populate(chunk);
	}
}
