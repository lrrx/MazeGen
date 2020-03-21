package generators;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import core.ChunkGen;
import core.ChunkType;

public class LootChunkGen extends ChunkGen{
	public LootChunkGen(World world, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
		super("Loot", ChunkType.LOOT, false, 1, chunkX, chunkZ, world, biomeGrid);
	}

	@Override
	public ChunkData generate(ChunkData chunkData) {
		chunkData.setRegion(4, baseHeight + 1, 4, 13, baseHeight + 2, 13, Material.STONE_BRICKS);
		chunkData.setRegion(7, baseHeight + 1, 7, 10, baseHeight + 2, 10, Material.CHISELED_STONE_BRICKS);
		chunkData.setRegion(6, baseHeight + 2, 6, 11, baseHeight + 6, 11, Material.STONE_BRICKS);
		chunkData.setRegion(7, baseHeight + 2, 7, 10, baseHeight + 5, 10, Material.AIR);
		chunkData.setRegion(8, baseHeight + 2, 6, 9, baseHeight + 4, 7, Material.AIR);
		chunkData.setRegion(10, baseHeight + 2, 8, 11, baseHeight + 4, 9, Material.AIR);
		chunkData.setRegion(8, baseHeight + 2, 10, 9, baseHeight + 4, 11, Material.AIR);
		return chunkData;
	}
}
