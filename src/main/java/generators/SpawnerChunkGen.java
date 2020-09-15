package generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class SpawnerChunkGen extends ChunkGen{
	protected ArrayList<EntityType> spawnerEntityList = new ArrayList<EntityType>(Arrays.asList(
			EntityType.BAT,
			EntityType.BEE,
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
			EntityType.ZOMBIFIED_PIGLIN,
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
	
	public SpawnerChunkGen(World world) {
		super("Spawner Room", ChunkType.DUNGEON, false, 1, world);
	}

	@Override
	public ChunkData generate(ChunkData chunkData, int chunkX, int chunkZ) {
		Random random = createRandom(chunkX, chunkZ);
		
		int spawnerX = random.nextInt(8) + 4;
    	int spawnerZ = random.nextInt(8) + 4;
        
    	chunkData.setRegion(spawnerX - 2, baseHeight + 1, spawnerZ - 2, spawnerX + 3 , baseHeight + 2, spawnerZ + 3, Material.OBSIDIAN);
    	chunkData.setRegion(spawnerX - 1, baseHeight + 2, spawnerZ - 1, spawnerX + 2 , baseHeight + 3, spawnerZ + 2, Material.OBSIDIAN);
		return chunkData;
	}
	
	@Override
	public void populate(Chunk chunk) {
		Random random = createRandom(chunkX, chunkZ);
		
		int spawnerX = random.nextInt(8) + 4;
    	int spawnerZ = random.nextInt(8) + 4;
		
		Block spawnerBlock = world.getBlockAt(chunk.getBlock(spawnerX, baseHeight + 3, spawnerZ).getLocation());
        spawnerBlock.setType(Material.SPAWNER);
        BlockState blockState = spawnerBlock.getState();
        CreatureSpawner spawner = ((CreatureSpawner)blockState);
        spawner.setSpawnedType(spawnerEntityList.get(random.nextInt(spawnerEntityList.size() - 1)));
        blockState.update();
	}
}
