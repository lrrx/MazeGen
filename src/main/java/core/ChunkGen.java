package core;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class ChunkGen {
	protected Material[] flowerTypes = {
		Material.ALLIUM,
		Material.AZURE_BLUET,
		Material.BLUE_ORCHID,
		Material.CORNFLOWER,
		Material.DANDELION,
		Material.LILAC,
		Material.LILY_OF_THE_VALLEY,
		Material.ORANGE_TULIP,
		Material.OXEYE_DAISY,
		Material.PEONY,
		Material.PINK_TULIP,
		Material.POPPY
	};
	
	private String name;
	private ChunkType type;
	private boolean isMultiChunk;
	private int size; //1 unless the generator covers multiple chunks
	private static Biome biome;
	
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
	
	protected static int baseHeight = 96;
	protected static int wallHeight = 16;
	protected static Material baseMaterial = Material.BEDROCK;
	protected static int spawnSize = 3;
	
	public static int getBaseHeight() {
		return baseHeight;
	}

	public static int getWallHeight() {
		return wallHeight;
	}

	public static Material getBaseMaterial() {
		return baseMaterial;
	}
	
	public static int getSpawnSize() {
		return spawnSize;
	}
	
	public static Biome getChunkBiome() {
		return biome;
	}
	
	public ChunkGen(String name, ChunkType type, boolean isMultiChunk, int size, World world) {
		this.name = name;
		this.type = type;
		this.isMultiChunk = isMultiChunk;
		this.size = size;
		
		this.world = world;
	}
	
	protected static double lerp(double y0, double y1, double x) {
		return y0 + x * (y1 - y0);
	}
	
	protected static double bilerp(double y0, double y1, double y2, double y3, double x, double z) {
		return lerp(lerp(y0, y1, x), lerp(y2, y3, x), z);
	}
	
	protected Random createRandom(int chunkX, int chunkZ) {
		return new Random((long) (NoiseGen.noise(chunkX, chunkZ, world) * 2147483647D));
	}
	
	protected void fillChunkRegionPopulate(int x1, int y1, int z1, int x2, int y2, int z2, Material material, Chunk chunk) {
		for(int x = x1; x <= x2; x++) {
			for(int y = y1; y <= y2; y++) {
				for(int z = z1; z <= z2; z++) {
					this.world.getBlockAt(chunk.getBlock(x, y, z).getLocation()).setType(material);
				}
			}
		}
	}

	protected void setChunkBlockPopulate(int x, int y, int z, Material material, Chunk chunk) {
		world.getBlockAt(chunk.getBlock(x, y, z).getLocation()).setType(material);
	}
	
	protected void createPersistentLeaf(int x, int y, int z, Material material, Chunk chunk) {
		Block block = chunk.getBlock(x, y, z);
		block.setType(material, false);
		Leaves leaf = (Leaves) block.getBlockData();
        leaf.setPersistent(true);
        block.setBlockData(leaf, false);
	}
	
	
	public ChunkData generate(ChunkData chunkData, int chunkX, int chunkZ) {
		return chunkData;
	}

	public void populate(Chunk chunk) {}
}
