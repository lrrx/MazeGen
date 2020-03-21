package structures;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.TreeType;
import org.bukkit.World;

import core.ChunkType;
import core.StructureGen;

public class FountainStructureGen extends StructureGen {

	public FountainStructureGen(World world) {
		super("Fountain Room", ChunkType.GARDEN, false, 1, world);
	}
	
	@Override
	public void populate(Chunk chunk) {
		Random random = this.createRandom_p(chunk);
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
