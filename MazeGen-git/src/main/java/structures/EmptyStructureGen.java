package structures;

import org.bukkit.Chunk;
import org.bukkit.World;

import core.ChunkType;
import core.StructureGen;

public class EmptyStructureGen extends StructureGen {

	public EmptyStructureGen(World world) {
		super("Empty Room", ChunkType.NEUTRAL, false, 1, world);
	}
	
	@Override
	public void populate(Chunk chunk) {}

}
