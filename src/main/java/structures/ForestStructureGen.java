package structures;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import core.ChunkType;
import core.StructureGen;

public class ForestStructureGen extends StructureGen {

	public ForestStructureGen(World world) {
		super("Forest", ChunkType.FOREST, false, 1, world);
	}

	@Override
	public void populate(Chunk chunk) {
		Random random = this.createRandom_p(chunk);
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		generator.setScale(0.025D);
		int amount = random.nextInt(5) + 5;  // Amount of trees
		for (int i = 1; i < amount; i++) {
			int X = random.nextInt(15);
			int Z = random.nextInt(15);
			int Y;
			int noise = (int) (generator.noise(chunk.getX() * 16 + X, chunk.getZ() * 16 + Z, 0.2D, 0.4D) * 3);
			Y = noise + baseHeight;//for (Y = world.getMaxHeight()-1; chunk.getBlock(X, Y, Z).getType() == Material.AIR; Y--); // Find the highest block of the (X,Z) coordinate chosen.
			int bonusHeight = random.nextInt(8) + 4;

			fillChunkRegion_p(X, Y, Z, X, Y + bonusHeight - 2, Z, Material.OAK_WOOD, chunk);
			setChunkBlock_p(X, Y + bonusHeight - 1, Z, Material.GRASS_BLOCK, chunk);
			world.generateTree(chunk.getBlock(X, Y + bonusHeight, Z).getLocation(), TreeType.BIG_TREE); // The tree type can be changed if you want.
			setChunkBlock_p(X, Y + bonusHeight - 1, Z, Material.OAK_WOOD, chunk);
		}
	}

}
