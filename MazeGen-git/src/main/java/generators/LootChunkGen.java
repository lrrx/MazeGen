package generators;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import core.ChunkGen;
import core.ChunkType;

public class LootChunkGen extends ChunkGen{
	public LootChunkGen(World world, int chunkX, int chunkZ) {
		super("Loot", ChunkType.LOOT, false, 1, chunkX, chunkZ, world);
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

	@Override
	public void populate(Chunk chunk) {
		//System.out.println("Generating LootRoom");
		Random random = this.createRandom(chunkX, chunkZ);
		Map<Material, Double> lootChanceMap = new HashMap<Material, Double>();

		lootChanceMap.put(Material.BONE, 0.578);
		lootChanceMap.put(Material.GUNPOWDER, 0.578);
		lootChanceMap.put(Material.ROTTEN_FLESH, 0.578);
		lootChanceMap.put(Material.STRING, 0.578);

		lootChanceMap.put(Material.WHEAT, 0.341);
		lootChanceMap.put(Material.BREAD, 0.341);

		lootChanceMap.put(Material.NAME_TAG, 0.283);
		lootChanceMap.put(Material.SADDLE, 0.283);

		lootChanceMap.put(Material.COAL, 0.266);
		lootChanceMap.put(Material.REDSTONE, 0.266);

		lootChanceMap.put(Material.MUSIC_DISC_13, 0.218);
		lootChanceMap.put(Material.MUSIC_DISC_CAT, 0.218);
		lootChanceMap.put(Material.IRON_HORSE_ARMOR, 0.218);
		lootChanceMap.put(Material.GOLDEN_APPLE, 0.218);

		lootChanceMap.put(Material.BEETROOT_SEEDS, 0.185);
		lootChanceMap.put(Material.MELON_SEEDS, 0.185);
		lootChanceMap.put(Material.PUMPKIN_SEEDS, 0.185);
		lootChanceMap.put(Material.IRON_INGOT, 0.185);
		lootChanceMap.put(Material.BUCKET, 0.185);

		lootChanceMap.put(Material.ENCHANTED_BOOK, 0.149);
		lootChanceMap.put(Material.GOLDEN_HORSE_ARMOR, 0.149);

		lootChanceMap.put(Material.GOLD_INGOT, 0.096);
		lootChanceMap.put(Material.DIAMOND_HORSE_ARMOR, 0.077);
		lootChanceMap.put(Material.ENCHANTED_GOLDEN_APPLE, 0.031);

		Object[] lootMaterials = lootChanceMap.keySet().toArray();

		/*fillChunkRegion(4, baseHeight + 2, 4, 4, baseHeight + 2, 12, Material.STONE_BRICK_WALL, chunk);
		fillChunkRegion(12, baseHeight + 2, 4, 12, baseHeight + 2, 12, Material.STONE_BRICK_WALL, chunk);
		fillChunkRegion(4, baseHeight + 2, 4, 12, baseHeight + 2, 4, Material.STONE_BRICK_WALL, chunk);
		fillChunkRegion(4, baseHeight + 2, 12, 12, baseHeight + 2, 12, Material.STONE_BRICK_WALL, chunk);

		setChunkBlock(4, baseHeight + 2, 8, Material.AIR, chunk);
		setChunkBlock(12, baseHeight + 2, 8, Material.AIR, chunk);
		setChunkBlock(8, baseHeight + 2, 4, Material.AIR, chunk);
		setChunkBlock(8, baseHeight + 2, 12, Material.AIR, chunk);*/

		Block b = world.getBlockAt(chunk.getBlock(8, baseHeight + 2, 8).getLocation());
		b.setType(Material.CHEST);
		/*fillChunkRegion_p(7, baseHeight - 2, 7, 9, baseHeight + 0, 9, Material.AIR, chunk);
		setChunkBlock_p(8, baseHeight - 2, 8, baseMaterial, chunk);
		setChunkBlock_p(8, baseHeight - 1, 8, Material.POWERED_RAIL, chunk);*/

		/*Block dispenserBlock = world.getBlockAt(chunk.getBlock(8, baseHeight + 0, 8).getLocation());
		dispenserBlock.setType(Material.DISPENSER);
		Directional directional = (Directional) dispenserBlock;
		directional.setFacing(BlockFace.DOWN);
		Container disp = (Container) dispenserBlock.getState();
		disp.getInventory().setItem(0, new ItemStack(Material.TNT_MINECART, 1));*/

		/*for(int i = 0; i <= 3; i++) {
			world.spawnEntity(chunk.getBlock(8, baseHeight - 1, 8).getLocation(), EntityType.MINECART_TNT);
		}*/
		Chest chest = (Chest) b.getState();
		Inventory inventory = chest.getBlockInventory();
		//inventory.clear();

		for(int i = 0; i < inventory.getSize(); i++) {
			int randomIndex = random.nextInt(lootMaterials.length);
			Material currentMaterial = (Material) lootMaterials[randomIndex];
			if(lootChanceMap.get(currentMaterial) >= random.nextDouble() / 2) {
				inventory.setItem(i, new ItemStack(currentMaterial, random.nextInt((int) (lootChanceMap.get(currentMaterial) * 16))));
			}
		}
	}
}
