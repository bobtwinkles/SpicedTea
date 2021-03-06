package tk.sirtwinkles.spicedtea.world.gen.dungeon.carve.room;

import java.util.ArrayList;

import tk.sirtwinkles.spicedtea.world.Level;

public final class RoomPopulator {
	
	public static void populateRooms(int level, ArrayList<Room> rooms, int[][] data, Level in) {
		for (Room r : rooms) {
			if (r.feature != null) {
				RoomFeature f = RoomFeatureFactory.getFeature(r.feature);
				if (f != null) {
					f.generate(r, data, in);
				} else {
					System.out.println(r.feature + " had no deffinition =(");
				}
			} else {
				RoomFeature f = RoomFeatureFactory.getFeature("generic");
				f.generate(r, data, in);
			}
		}
	}

	private RoomPopulator() {} //Never instantiate.
}
