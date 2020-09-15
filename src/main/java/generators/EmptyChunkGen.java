package generators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class EmptyChunkGen extends ChunkGen{
	public EmptyChunkGen(World world) {
		super("Empty Room", ChunkType.NEUTRAL, false, 1, world);
	}
	
	@Override
	public ChunkData generate(ChunkData chunkData, int chunkX, int chunkZ) {
		Random random = createRandom(chunkX, chunkZ);
		for(int i = 0; i <= 5 + random.nextInt(10); i++) {
			chunkData.setBlock(1 + random.nextInt(15), baseHeight, 1 + random.nextInt(15), Material.OBSIDIAN);
		}
		return chunkData;
	}
	
	@Override
	public void populate(Chunk chunk) {}
}
