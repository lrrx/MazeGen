package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class StructureGen {
	protected ArrayList<EntityType> spawnerEntityList = new ArrayList<EntityType>(Arrays.asList(
			EntityType.BAT,
			EntityType.BLAZE,
			EntityType.CAT,
			EntityType.CAVE_SPIDER,
			EntityType.CHICKEN,
			EntityType.COW,
			EntityType.CREEPER,
			EntityType.ENDERMAN,
			EntityType.ENDERMITE,
			EntityType.EVOKER,
			EntityType.FOX,
			EntityType.HUSK,
			EntityType.ILLUSIONER,
			EntityType.IRON_GOLEM,
			EntityType.MAGMA_CUBE,
			EntityType.MUSHROOM_COW,
			EntityType.OCELOT,
			EntityType.PARROT,
			EntityType.PHANTOM,
			EntityType.PIG,
			EntityType.PIG_ZOMBIE,
			EntityType.PILLAGER,
			EntityType.POLAR_BEAR,
			EntityType.RABBIT,
			EntityType.RAVAGER,
			EntityType.SHEEP,
			EntityType.SHULKER,
			EntityType.SILVERFISH,
			EntityType.SKELETON,
			EntityType.SLIME,
			EntityType.SNOWMAN,
			EntityType.SPIDER,
			EntityType.STRAY,
			EntityType.TURTLE,
			EntityType.VEX,
			EntityType.VILLAGER,
			EntityType.VINDICATOR,
			EntityType.WANDERING_TRADER,
			EntityType.WITCH,
			EntityType.WITHER_SKELETON,
			EntityType.WOLF,
			EntityType.ZOMBIE,
			EntityType.ZOMBIE_VILLAGER));
	
	private String name;
	private ChunkType type;
	private boolean isMultiChunk;
	private int size; //1 unless the generator covers multiple chunks

	protected World world;

	protected int baseHeight = 16;
	protected int wallHeight = 16;
	protected Material baseMaterial = Material.BEDROCK;

	public StructureGen(String name, ChunkType type, boolean isMultiChunk, int size, World world) {
		this.name = name;
		this.type = type;
		this.isMultiChunk = isMultiChunk;
		this.size = size;

		this.world = world;
	}

	public final Random createRandom_p(Chunk chunk) {
		return new Random((long) (this.noise_p(chunk) * 2147483647D));
	}
	
	public final double noise_p(Chunk chunk) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 4);
		generator.setScale(0.005D);
		return generator.noise(chunk.getX() * 16 , chunk.getZ() * 16 , 0.2D, 0.4D);
	}

	public final double largeNoise_p(Chunk chunk) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 4);
		generator.setScale(0.001D);
		return generator.noise(chunk.getX() * 16 , chunk.getZ() * 16 , 0.2D, 0.4D);
	}

	//avoid using this function for populating, it is inefficient
	public void fillChunkRegion_p(int x1, int y1, int z1, int x2, int y2, int z2, Material material, Chunk chunk) {
		for(int x = x1; x <= x2; x++) {
			for(int y = y1; y <= y2; y++) {
				for(int z = z1; z <= z2; z++) {
					this.world.getBlockAt(chunk.getBlock(x, y, z).getLocation()).setType(material);
				}
			}
		}
	}

	public void setChunkBlock_p(int x, int y, int z, Material material, Chunk chunk) {
		world.getBlockAt(chunk.getBlock(x, y, z).getLocation()).setType(material);
	}

	public void populate(Chunk chunk) {}
}
