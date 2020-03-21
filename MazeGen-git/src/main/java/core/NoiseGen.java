package core;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class NoiseGen {
	//normal-scale chunk location based perlin noise
	public static double noise(int chunkX, int chunkZ, World world) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 4);
		generator.setScale(0.005D);
		return generator.noise(chunkX * 16 , chunkZ * 16 , 0.2D, 0.4D);
	}

	//large-scale chunk location based perlin noise
	public static double largeNoise(int chunkX, int chunkZ, World world) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 4);
		generator.setScale(0.001D);
		return generator.noise(chunkX * 16 , chunkZ * 16 , 0.2D, 0.4D);
	}

}
