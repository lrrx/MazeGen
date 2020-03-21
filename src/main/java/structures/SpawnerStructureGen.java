package structures;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;

import core.ChunkType;
import core.StructureGen;

public class SpawnerStructureGen extends StructureGen {

	public SpawnerStructureGen(World world) {
		super("Spawner Room", ChunkType.DUNGEON, false, 1, world);
	}
	
	@Override
	public void populate(Chunk chunk) {
		Random random = createRandom_p(chunk);
		int spawnerX = random.nextInt(8) + 4;
    	int spawnerZ = random.nextInt(8) + 4;
        
    	fillChunkRegion_p(spawnerX - 2, baseHeight + 1, spawnerZ - 2, spawnerX + 2 , baseHeight + 1, spawnerZ + 2, Material.OBSIDIAN, chunk);
    	fillChunkRegion_p(spawnerX - 1, baseHeight + 2, spawnerZ - 1, spawnerX + 1 , baseHeight + 2, spawnerZ + 1, Material.OBSIDIAN, chunk);
    	Block spawnerBlock = world.getBlockAt(chunk.getBlock(spawnerX, baseHeight + 3, spawnerZ).getLocation());
        spawnerBlock.setType(Material.SPAWNER);
        BlockState blockState = spawnerBlock.getState();
        CreatureSpawner spawner = ((CreatureSpawner)blockState);
        spawner.setSpawnedType(spawnerEntityList.get(random.nextInt(spawnerEntityList.size() - 1)));
        blockState.update();
    	
    	//TODO: add loot chest
	}
}
