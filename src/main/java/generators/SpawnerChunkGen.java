package generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import core.ChunkGen;
import core.ChunkType;

public class SpawnerChunkGen extends ChunkGen{
	public SpawnerChunkGen(World world, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
		super("Spawner Room", ChunkType.DUNGEON, false, 1, chunkX, chunkZ, world, biomeGrid);
	}

	@Override
	public ChunkData generate(ChunkData chunkData) {
		return chunkData;
	}
}
