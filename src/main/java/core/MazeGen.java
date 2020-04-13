package core;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class MazeGen extends JavaPlugin{
	@Override
    public void onEnable() {
		getLogger().info("MazeGen was enabled!");
        // TODO Insert logic to be performed when the plugin is enabled
    }
    
	
	
    @Override
    public void onDisable() {
    	getLogger().info("MazeGen was disabled!");
        // TODO Insert logic to be performed when the plugin is disabled
    }
    
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
    	System.out.println("Overriding world generator");
        return new CustomChunkGenerator();
    }
}
