package generators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class SwampChunkGen extends ChunkGen{
	public SwampChunkGen(World world, int chunkX, int chunkZ) {
		super("Empty Room", ChunkType.NEUTRAL, false, 1, chunkX, chunkZ, world);
	}
	
	@Override
	public ChunkData generate(ChunkData chunkData) {
		Random random = createRandom(chunkX, chunkZ);
		chunkData.setRegion(1, baseHeight - 1, 1, 15, baseHeight, 16, Material.DIRT);
		
		for(int x = 1; x <= 16; x++) {
			for(int z = 1; z <= 16; z++) {
				if(random.nextDouble() <= 0.3) {
					chunkData.setBlock(x, baseHeight, z, Material.DIRT);
					if(random.nextDouble() <= 0.9) {
						chunkData.setBlock(x, baseHeight + 1, z, Material.GRASS);
					}
				}
				else { 
					chunkData.setBlock(x, baseHeight, z, Material.WATER);
				}
			}
		}
		return chunkData;
	}
	
	@Override
	public void populate(Chunk chunk) {
	}
}
