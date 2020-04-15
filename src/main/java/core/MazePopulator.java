package core;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import org.bukkit.generator.BlockPopulator;


public class MazePopulator extends BlockPopulator {

	//enable or disable verbose logging
	@SuppressWarnings("unused")
	private boolean debugEnabled = false;

	//height of ground
	//private int baseHeight = ChunkGen.getBaseHeight();

	//height of normal walls
	//private int wallHeight = ChunkGen.getWallHeight();

	//main maze material
	//private Material baseMaterial = ChunkGen.getBaseMaterial();

	//toggle highways
	boolean highwaysEnabled = false;

	//size of the empty spawn area
	private int spawnSize = ChunkGen.getSpawnSize();
	
	//used to store World reference
	//private World world;

	public MazePopulator(int baseHeight, int wallHeight, Material baseMaterial, World world) {
		/*this.baseHeight = baseHeight;
		this.wallHeight = wallHeight;
		this.baseMaterial = baseMaterial;
		this.world = world;*/
	}

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		
		//use chunkNoise as the seed for random generation in this chunk
		//random = new Random((long) (chunkNoise * 2147483647D));

		world.getBlockAt(chunk.getBlock(8, 255, 8).getLocation()).setType(Material.GLOWSTONE);
		
		//store random chunk generator in cg
		ChunkGen cg = GeneratorChooser.getChunkGen(chunk.getX(), chunk.getZ(), highwaysEnabled, spawnSize, world);

		//populate the chunk
		cg.populate(chunk);
	}
}
