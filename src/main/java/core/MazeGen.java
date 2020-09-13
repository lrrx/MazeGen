package core;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class MazeGen extends JavaPlugin{
	@Override
    public void onEnable() {
		getLogger().info("MazeGen was enabled!");
    }
	
    @Override
    public void onDisable() {
    	getLogger().info("MazeGen was disabled!");
    }
    
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
    	System.out.println("Overriding world generator");
        return new CustomChunkGenerator();
    }
}
