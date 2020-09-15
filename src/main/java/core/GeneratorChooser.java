package core;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;

import generators.*;

public class GeneratorChooser {
	static boolean debugEnabled = false;

	private static int chunkCenterOffset = 7;
	
	private static Map<ChunkGen, Double> ChunkGenChanceMap = new HashMap<ChunkGen, Double>();
	
	
	public static boolean isForest(World world, int chunkX, int chunkZ) {
		
		int forestsOffset = 16384;
		
		for(int i = 0; i < 3 ; i++) {
			double chunkLargeNoise = NoiseGen.largeNoise(chunkCenterOffset + chunkX * 16 + i * forestsOffset, chunkCenterOffset +  chunkZ * 16 + i * forestsOffset, world);
			
			if(chunkLargeNoise < -0.8) {
				return true;
			}
		}
		return false;
	}

	public static boolean isPlainsChunk(World world, int chunkX, int chunkZ) {
		
		double chunkNoise = NoiseGen.noise(chunkCenterOffset + chunkX * 16, chunkCenterOffset + chunkZ * 16, world);
		double chunkLargeNoise = NoiseGen.largeNoise(chunkCenterOffset + chunkX * 16, chunkCenterOffset + chunkZ * 16, world);
		
		if(chunkNoise > -0.7 && chunkLargeNoise > 0.5) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean isMazeChunk(World world, int chunkX, int chunkZ) {
		double chunkNoise = NoiseGen.noise(chunkCenterOffset + chunkX * 16, chunkCenterOffset + chunkZ * 16, world);
		
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
	
	public static boolean isSpawnChunk(World world, int chunkX, int chunkZ) {
		int spawnSize = ChunkGen.getSpawnSize();
		
		if(Math.abs(chunkX) <= spawnSize && Math.abs(chunkZ) <= spawnSize) {
			return true;
		}
		else {
			return false; 
		}
	}

	public static boolean isForestNear(World world, int chunkX, int chunkZ) {
		for(int x = -1; x <= 1; x++) {
			for(int z = -1; z <= 1; z++) {
				if (isForest(world, chunkCenterOffset + chunkX, chunkCenterOffset + chunkZ)) {
					if (debugEnabled) {
						System.out.println("Forest is Near! chunkX: " + chunkX + " chunkZ: " + chunkZ + " x: " + x + " z: " + z);
					}
					return true;
				}
			}
		}
		return false;
	}

	public static ChunkGen getChunkGen(int chunkX, int chunkZ, boolean highwaysEnabled, int spawnSize, World world) {
		
		double chunkNoise = NoiseGen.noise(chunkX * 16, chunkZ * 16, world);
		
		ChunkGen cg = new EmptyChunkGen(world);
		//generate random integer for room generation choice
		int n = Math.abs(((int) (chunkNoise * 2147483647D)) % 1600);

		//System.out.println("___________________________n: " + n);

		//generate empty rooms for highways
		if((chunkX % 256 == 0 || chunkZ % 256 == 0) && highwaysEnabled) {
			cg = new EmptyChunkGen(world);
		}
		//generate empty rooms near spawn
		else if(isSpawnChunk(world, chunkX, chunkZ)) {
			cg = new SpawnChunkGen(world);
		}
		else if (isPlainsChunk(world, chunkX, chunkZ)) {
			cg = new PlainsChunkGen(world);
		}
		//use chunkNoise to decide what type of room (Maze/Forest) to generate
		else if (isForest(world, chunkX, chunkZ)) {
			cg = new ForestChunkGen(world);
		}
		else if(isMazeChunk(world, chunkX, chunkZ)) {
			cg = new MazeChunkGen(world);
		}
		
		//only generate LavaChunk if there is no forest close to it
		else if(1 <= n && n <= 30) {
			if (!isForestNear(world, chunkX, chunkZ)) {
				cg = new LavaChunkGen(world);
			}
		}
		//only generate LavaParkourChunk if there is no forest close to it
		else if (101 <= n && n <= 130) {
			if (!isForestNear(world, chunkX, chunkZ)) {
				cg = new LavaParkourChunkGen(world);
			}
		}
		else if (201 <= n && n <= 220) {
			cg = new StoneMineChunkGen(world);
		}
		else if (301 <= n && n <= 350) {
			cg = new PillarsChunkGen(world);
		}
		else if (401 <= n && n <= 410) {
			cg = new EnchantmentShrineChunkGen(world);
		}
		else if (501 <= n && n <= 530) {
			cg = new FountainChunkGen(world);
		}
		else if(601 <= n && n <= 630) {
			cg = new SpawnerChunkGen(world);
		}
		else if (701 <= n && n <= 730) {
			cg = new LootChunkGen(world);
		}
		else if (801 <= n && n <= 815) {
			cg = new LargeTowerChunkGen(world);
		}
		else if (901 <= n && n <= 930) {
			cg = new WaterChunkGen(world);
		}
		else if (1001 <= n && n <= 1010) {
			cg = new WaterHolesChunkGen(world);
		}
		else if (1101 <= n && n <= 1130) {
			cg = new LavaHolesChunkGen(world);
		}
		else if (1201 <= n && n <= 1230) {
			cg = new MeadowChunkGen(world);
		}
		else if (1301 <= n && n <= 1330) {
			cg = new DesertChunkGen(world);
		}
		else if (1401 <= n && n <= 1430) {
			cg = new SwampChunkGen(world);
		}
		else if (1501 <= n && n <= 1530) {
			cg = new FarmChunkGen(world);
		}
		else {
			//cg = new EmptyChunkGen(world);
		}
		return cg;
	}
}
