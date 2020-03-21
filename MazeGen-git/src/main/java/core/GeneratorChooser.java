package core;

import org.bukkit.World;

import generators.EmptyChunkGen;
import generators.EnchantmentShrineChunkGen;
import generators.ForestChunkGen;
import generators.FountainChunkGen;
import generators.LargeTowerChunkGen;
import generators.LavaChunkGen;
import generators.LavaHolesChunkGen;
import generators.LavaParkourChunkGen;
import generators.LootChunkGen;
import generators.MazeChunkGen;
import generators.PillarsChunkGen;
import generators.SpawnerChunkGen;
import generators.StoneMineChunkGen;
import generators.WaterChunkGen;
import generators.WaterHolesChunkGen;

public class GeneratorChooser {
	static boolean debugEnabled = false;

	public static boolean isForestNear(int chunkX, int chunkZ, World world) {
		for(int x = -1; x <= 1; x++) {
			for(int z = -1; z <= 1; z++) {
				if (((int) (NoiseGen.noise(chunkX + x, chunkZ + z, world) * 4D)) == 2) {
					if (debugEnabled) {
						System.out.println("Forest is Near! chunkX: " + chunkX + " chunkZ: " + chunkZ + " x: " + x + " z: " + z);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public static ChunkGen getChunkGen(int chunkX, int chunkZ, boolean highwaysEnabled, int spawnSize, double chunkNoise, World world) {
		ChunkGen cg = null;

		//generate random integer for room generation choice
		int n = Math.abs(((int) (chunkNoise * 2147483647D)) % 1300);

		//generate empty rooms for highways
		if((chunkX % 256 == 0 || chunkZ % 256 == 0) && highwaysEnabled) {
			cg = new EmptyChunkGen(world, chunkX, chunkZ);
		}
		//generate empty rooms near spawn
		else if(Math.abs(chunkX) <= spawnSize && Math.abs(chunkZ) <= spawnSize) {
			cg = new EmptyChunkGen(world, chunkX, chunkZ);
		}
		//use chunkNoise to decide what type of room (Maze/Forest) to generate
		else if (((int) (chunkNoise * 4D)) == 1 || ((int) (chunkNoise * 4D)) == -1) {
			cg = new MazeChunkGen(world, chunkX, chunkZ);
		}
		else if (((int) (chunkNoise * 4D)) >= 2) {
			cg = new ForestChunkGen(world, chunkX, chunkZ);
		}
		//only generate LavaChunk if there is no forest close to it
		else if(1 <= n && n <= 30) {
			if (!isForestNear(chunkX, chunkZ, world)){
				cg = new LavaChunkGen(world, chunkX, chunkZ);
			}
		}
		//only generate LavaParkourChunk if there is no forest close to it
		else if (101 <= n && n <= 130) {
			if (!isForestNear(chunkX, chunkZ, world)){
				cg = new LavaParkourChunkGen(world, chunkX, chunkZ);
			}
		}
		else if (201 <= n && n <= 220) {
			cg = new StoneMineChunkGen(world, chunkX, chunkZ);
		}
		else if (301 <= n && n <= 350) {
			cg = new PillarsChunkGen(world, chunkX, chunkZ);
		}
		else if (401 <= n && n <= 410) {
			cg = new EnchantmentShrineChunkGen(world, chunkX, chunkZ);
		}
		else if (501 <= n && n <= 530) {
			cg = new FountainChunkGen(world, chunkX, chunkZ);
		}
		else if(601 <= n && n <= 630) {
			cg = new SpawnerChunkGen(world, chunkX, chunkZ);
		}
		else if (701 <= n && n <= 730) {
			cg = new LootChunkGen(world, chunkX, chunkZ);
		}
		else if (801 <= n && n <= 815) {
			cg = new LargeTowerChunkGen(world, chunkX, chunkZ);
		}
		else if (901 <= n && n <= 930) {
			cg = new WaterChunkGen(world, chunkX, chunkZ);
		}
		else if (1001 <= n && n <= 1010) {
			cg = new WaterHolesChunkGen(world, chunkX, chunkZ);
		}
		else if (1101 <= n && n <= 1130) {
			cg = new LavaHolesChunkGen(world, chunkX, chunkZ);
		}
		else {
			cg = new EmptyChunkGen(world, chunkX, chunkZ);
		}
		return cg;
	}
}
