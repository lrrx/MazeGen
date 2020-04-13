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

	@Override
	public void populate(World world, Random random, Chunk chunk) {

		//prepare chunkNoise for use in this chunk -> store it in variable to only have one call to SimplexOctaveGenerator to save performance
		chunkNoise = NoiseGen.noise(chunk.getX(), chunk.getZ(), world);

		//use chunkNoise as the seed for random generation in this chunk
		random = new Random((long) (chunkNoise * 2147483647D));

		world.getBlockAt(chunk.getBlock(8, 255, 8).getLocation()).setType(Material.GLOWSTONE);
		
		//store random chunk generator in cg
		ChunkGen cg = GeneratorChooser.getChunkGen(chunk.getX(), chunk.getZ(), highwaysEnabled, spawnSize, chunkNoise, world);

		//populate the chunk
		cg.populate(chunk);
	}
}
