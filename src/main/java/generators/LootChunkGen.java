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
	public LootChunkGen(World world) {
		super("Loot", ChunkType.LOOT, false, 1, world);
	}

	@Override
	public ChunkData generate(ChunkData chunkData, int chunkX, int chunkZ) {
		Random random = this.createRandom(chunkX, chunkZ);
		
		int posX = 4 - random.nextInt(8);
		int posZ = 4 - random.nextInt(8);
		
		chunkData.setRegion(4 + posX, baseHeight + 1, 4 + posZ, 13 + posX, baseHeight + 2, 13 + posZ, Material.STONE_BRICKS);
		chunkData.setRegion(7 + posX, baseHeight + 1, 7 + posZ, 10 + posX, baseHeight + 2, 10 + posZ, Material.CHISELED_STONE_BRICKS);
		chunkData.setRegion(6 + posX, baseHeight + 2, 6 + posZ, 11 + posX, baseHeight + 6, 11 + posZ, Material.STONE_BRICKS);
		chunkData.setRegion(7 + posX, baseHeight + 2, 7 + posZ, 10 + posX, baseHeight + 5, 10 + posZ, Material.AIR);
		chunkData.setRegion(8 + posX, baseHeight + 2, 6 + posZ, 9 + posX, baseHeight + 4, 7 + posZ, Material.AIR);
		chunkData.setRegion(10 + posX, baseHeight + 2, 8 + posZ, 11 + posX, baseHeight + 4, 9 + posZ, Material.AIR);
		chunkData.setRegion(8 + posX, baseHeight + 2, 10 + posZ, 9 + posX, baseHeight + 4, 11 + posZ, Material.AIR);
		return chunkData;
	}

	@Override
	public void populate(Chunk chunk) {
		//System.out.println("Generating LootRoom");
		Random random = this.createRandom(chunkX, chunkZ);
		
		int posX = 4 - random.nextInt(8);
		int posZ = 4 - random.nextInt(8);
		
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
		
		Block b = world.getBlockAt(chunk.getBlock(8 + posX, baseHeight + 2, 8 + posZ).getLocation());
		b.setType(Material.CHEST);
		Chest chest = (Chest) b.getState();
		Inventory inventory = chest.getBlockInventory();
		inventory.clear();

		for(int i = 0; i < inventory.getSize(); i++) {
			int randomIndex = random.nextInt(lootMaterials.length);
			Material currentMaterial = (Material) lootMaterials[randomIndex];
			if(lootChanceMap.get(currentMaterial) >= random.nextDouble() / 2) {
				int stackSize = Math.abs((int) (lootChanceMap.get(currentMaterial) * 16));
				if(stackSize > 0) {
					inventory.setItem(i, new ItemStack(currentMaterial, random.nextInt(stackSize)));
				}
			}
		}
	}
}
