package core;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexOctaveGenerator;


public class MazePopulator extends BlockPopulator {

	//enable or disable verbose logging
	@SuppressWarnings("unused")
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

	@Override
	public void populate(World world, Random random, Chunk chunk) {

		//prepare chunkNoise for use in this chunk -> store it in variable to only have one call to SimplexOctaveGenerator to save performance
		chunkNoise = noise(chunk.getX(), chunk.getZ(), world);

		//use chunkNoise as the seed for random generation in this chunk
		random = new Random((long) (chunkNoise * 2147483647D));

		world.getBlockAt(chunk.getBlock(8, 255, 8).getLocation()).setType(Material.GLOWSTONE);
		
		//store random structure generator in sg
		ChunkGen cg = GeneratorChooser.getChunkGen(chunk.getX(), chunk.getZ(), highwaysEnabled, spawnSize, chunkNoise, world);

		//let the chosen structure generate run
		cg.populate(chunk);
	}
}
