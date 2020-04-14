package core;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class NoiseGen {
	//normal-scale chunk location based perlin noise
	public static double noise(int chunkX, int chunkZ, World world) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		generator.setScale(0.005D / 16);
		return generator.noise(chunkX * 16 , chunkZ * 16 , 0.5D, 0.2D, true);
	}

	//large-scale chunk location based perlin noise
	public static double largeNoise(int chunkX, int chunkZ, World world) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 2);
		generator.setScale(0.0008D / 16);
		return generator.noise(chunkX * 16 , chunkZ * 16 , 0.4D, 0.1D, true);
	}

}
