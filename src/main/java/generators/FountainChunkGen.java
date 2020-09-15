package generators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class FountainChunkGen extends ChunkGen{
	public FountainChunkGen(World world) {
		super("Fountain Room", ChunkType.GARDEN, false, 1, world);
	}
	
	@Override
	public ChunkData generate(ChunkData chunkData, int chunkX, int chunkZ) {
		chunkData.setRegion(1, baseHeight - 4, 1, 15 + 1, baseHeight, 15 + 1, Material.DIRT);
		chunkData.setRegion(1, baseHeight, 1, 15 + 1, baseHeight + 1, 15 + 1, Material.GRASS_BLOCK);
		chunkData.setRegion(1, baseHeight, 8, 15 + 1, baseHeight + 1, 8 + 1, Material.GRASS_PATH);
		chunkData.setRegion(8, baseHeight, 1, 8 + 1, baseHeight + 1, 15 + 1, Material.GRASS_PATH);
		chunkData.setRegion(7, baseHeight, 7, 9 + 1, baseHeight + 1, 9 + 1, Material.WATER);
		return chunkData;
	}
	
	@Override
	public void populate(Chunk chunk) {
		Random random = this.createRandom(chunkX, chunkZ);
    	for(int i = 0; i <= random.nextInt(8) + 4; i++) {
    		int treeX = 1 + random.nextInt(14);
    		int treeZ = 1 + random.nextInt(14);
    		if((treeX != 8) && (treeZ != 8)) {
    			if(random.nextInt(3) == 0) {
    				world.generateTree(chunk.getBlock(treeX, baseHeight + 1, treeZ).getLocation(), TreeType.BIG_TREE);
    			}
    			else {
    				world.generateTree(chunk.getBlock(treeX, baseHeight + 1, treeZ).getLocation(), TreeType.TREE);
    			}
    		}
    	}
	}
}
