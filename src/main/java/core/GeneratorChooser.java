package core;

import org.bukkit.Material;
import org.bukkit.World;

import generators.*;

public class GeneratorChooser {
	static boolean debugEnabled = false;

	public static boolean isForest(int chunkX, int chunkZ, double chunkNoise, World world) {

		if(chunkNoise > 0.6) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean isPlainsChunk(int chunkX, int chunkZ, double chunkNoise, double chunkLargeNoise, World world) {

		if(chunkNoise > -0.6 && chunkLargeNoise > 0.5) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean isInnerForest(int chunkX, int chunkZ, double chunkNoise, World world) {

		if(chunkNoise > 0.7) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean isMazeChunk(int chunkX, int chunkZ, double chunkNoise, World world) {
		if (0.4 < chunkNoise && chunkNoise <= 0.6) {
			return true;
		}
		else if (-0.4 < chunkNoise && chunkNoise <= -0.2) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean isForestNear(int chunkX, int chunkZ, World world) {
		for(int x = -1; x <= 1; x++) {
			for(int z = -1; z <= 1; z++) {
				if (isForest(chunkX, chunkZ, NoiseGen.noise(chunkX + x, chunkZ + z, world), world)
						|| isInnerForest(chunkX, chunkZ, NoiseGen.noise(chunkX + x, chunkZ + z, world), world)) {
					if (debugEnabled) {
						System.out.println("Forest is Near! chunkX: " + chunkX + " chunkZ: " + chunkZ + " x: " + x + " z: " + z);
					}
					return true;
				}
			}
		}
		return false;
	}

	public static ChunkGen getChunkGen(int chunkX, int chunkZ, boolean highwaysEnabled, int spawnSize, double chunkNoise, double chunkLargeNoise, World world) {

		ChunkGen cg = new EmptyChunkGen(world, chunkX, chunkZ);
		//generate random integer for room generation choice
		int n = Math.abs(((int) (chunkNoise * 2147483647D)) % 1400);

		//System.out.println("___________________________n: " + n);

		//generate empty rooms for highways
		if((chunkX % 256 == 0 || chunkZ % 256 == 0) && highwaysEnabled) {
			cg = new EmptyChunkGen(world, chunkX, chunkZ);
		}
		//generate empty rooms near spawn
		else if(Math.abs(chunkX) <= spawnSize && Math.abs(chunkZ) <= spawnSize) {
			cg = new EmptyChunkGen(world, chunkX, chunkZ);
		}
		else if (isPlainsChunk(chunkX, chunkZ, chunkNoise, chunkLargeNoise, world)) {
			cg = new PlainsChunkGen(world, chunkX, chunkZ);
		}
		//use chunkNoise to decide what type of room (Maze/Forest) to generate
		else if(isMazeChunk(chunkX, chunkZ, chunkNoise, world)) {
			cg = new MazeChunkGen(world, chunkX, chunkZ);
		}
		else if (isInnerForest(chunkX, chunkZ, chunkNoise, world)) {
			cg = new InnerForestChunkGen(world, chunkX, chunkZ);
		}
		else if (isForest(chunkX, chunkZ, chunkNoise, world)) {
			cg = new ForestChunkGen(world, chunkX, chunkZ);
		}
		//only generate LavaChunk if there is no forest close to it
		else if(1 <= n && n <= 30) {
			if (!isForestNear(chunkX, chunkZ, world)) {
				cg = new LavaChunkGen(world, chunkX, chunkZ);
			}
		}
		//only generate LavaParkourChunk if there is no forest close to it
		else if (101 <= n && n <= 130) {
			if (!isForestNear(chunkX, chunkZ, world)) {
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
		else if (1201 <= n && n <= 1230) {
			cg = new MeadowChunkGen(world, chunkX, chunkZ);
		}
		else if (1301 <= n && n <= 1330) {
			cg = new DesertChunkGen(world, chunkX, chunkZ);
		}
		else {
			cg = new EmptyChunkGen(world, chunkX, chunkZ);
		}
		return cg;
	}
}
