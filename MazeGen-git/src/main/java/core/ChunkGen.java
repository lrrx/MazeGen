package core;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

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
	
	protected static int baseHeight = 16;
	protected static int wallHeight = 16;
	protected static Material baseMaterial = Material.BEDROCK;
	
	public ChunkGen(String name, ChunkType type, boolean isMultiChunk, int size, int chunkX, int chunkZ, World world) {
		this.name = name;
		this.type = type;
		this.isMultiChunk = isMultiChunk;
		this.size = size;
		
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.world = world;
	}
	
	public Random createRandom(int chunkX, int chunkZ) {
		return new Random((long) (NoiseGen.noise(chunkX, chunkZ, world) * 2147483647D));
	}
	
	public void fillChunkRegionPopulate(int x1, int y1, int z1, int x2, int y2, int z2, Material material, Chunk chunk) {
		for(int x = x1; x <= x2; x++) {
			for(int y = y1; y <= y2; y++) {
				for(int z = z1; z <= z2; z++) {
					this.world.getBlockAt(chunk.getBlock(x, y, z).getLocation()).setType(material);
				}
			}
		}
	}

	public void setChunkBlockPopulate(int x, int y, int z, Material material, Chunk chunk) {
		world.getBlockAt(chunk.getBlock(x, y, z).getLocation()).setType(material);
	}
	
	public ChunkData generate(ChunkData chunkData) {
		return chunkData;
	}

	public void populate(Chunk chunk) {}
}
