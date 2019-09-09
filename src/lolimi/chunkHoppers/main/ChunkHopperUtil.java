package lolimi.chunkHoppers.main;

import org.bukkit.Location;
import org.bukkit.block.Hopper;

public class ChunkHopperUtil {
	
	Hopper h;
	byte rawData;
	Location loc;
	Location facing;
	
	
	@SuppressWarnings("deprecation")
	public ChunkHopperUtil(Hopper hopper) {
		h = hopper;
		rawData = h.getRawData();
		loc = h.getLocation();
		facing = loc.clone();
		switch (rawData) {
		case 0: case 8:
			facing.add(0, -1, 0);
			break;

		case 2: case 10:
			facing.add(0, 0 , -1);
			break;
			
		case 3: case 11:
			facing.add(0, 0 , 1);
			break;
			
		case 4: case 12:
			facing.add(-1, 0 , 0);
			break;
			
		case 5: case 13:
			facing.add(1, 0 , 0);
			break;
		}
	}
	
	public Location getFacing() {
		return facing;
	}

}
