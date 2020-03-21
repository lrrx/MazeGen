package core;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class ChunkGen {
	private String name;
	private ChunkType type;
	private boolean isMultiChunk;
	private int size; //1 unless the generator covers multiple chunks
	
	public final String getName() {
		return name;
	}

	public final ChunkType getType() {
		return type;
	}

	public final boolean isMultiChunk() {
		return isMultiChunk;
	}

	public final int getSize() {
		return size;
	}

	protected int chunkX;
	protected int chunkZ;
	protected World world;
	private BiomeGrid biomeGrid;
	
	protected int baseHeight = 16;
	protected int wallHeight = 16;
	protected Material baseMaterial = Material.BEDROCK;
	
	public ChunkGen(String name, ChunkType type, boolean isMultiChunk, int size, int chunkX, int chunkZ, World world, BiomeGrid biomeGrid) {
		this.name = name;
		this.type = type;
		this.isMultiChunk = isMultiChunk;
		this.size = size;
		
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.world = world;
		this.biomeGrid = biomeGrid;
	}
	
	public Random createRandom() {
		return new Random((long) (this.noise(chunkX, chunkZ, world) * 2147483647D));
	}
	
	public double noise(int chunkX, int chunkZ, World world) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(), 4);
		generator.setScale(0.005D);
		return generator.noise(chunkX * 16 , chunkZ * 16 , 0.2D, 0.4D);
	}
	
	public double largeNoise(int chunkX, int chunkZ, World world) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 4);
		generator.setScale(0.001D);
		return generator.noise(chunkX * 16 , chunkZ * 16 , 0.2D, 0.4D);
	}
	
	public ChunkData generate(ChunkData chunkData) {
		return chunkData;
	}
}
