package tk.sirtwinkles.spicedtea.world.gen;

import static tk.sirtwinkles.spicedtea.MathUtils.random;

import java.util.HashMap;

import tk.sirtwinkles.spicedtea.Globals;
import tk.sirtwinkles.spicedtea.world.tile.JSONBackgroundTile;
import tk.sirtwinkles.spicedtea.world.tile.JSONDoorTile;
import tk.sirtwinkles.spicedtea.world.tile.JSONFloorTile;
import tk.sirtwinkles.spicedtea.world.tile.JSONStairsTile;
import tk.sirtwinkles.spicedtea.world.tile.JSONWallTile;
import tk.sirtwinkles.spicedtea.world.tile.StairDirection;
import tk.sirtwinkles.spicedtea.world.tile.Tile;

import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;

@SuppressWarnings("unchecked")
public class JSONTileSetProvider implements TileSetProvider {
	class TileDescriptor {
		int x, y, w, h;
	}

	HashMap<String, TileDescriptor> tileDesc;
	String idBase;

	public JSONTileSetProvider(String config) {
		OrderedMap<String, Object> root = (OrderedMap<String, Object>) Globals.json
				.parse(config);
		OrderedMap<String, Object> tiles = (OrderedMap<String, Object>) root
				.get("tiles");
		OrderedMap<String, Object> tileObject;
		this.tileDesc = new HashMap<String, JSONTileSetProvider.TileDescriptor>();
		for (Entry<String, Object> s : tiles.entries()) {
			TileDescriptor temp = new TileDescriptor();
			tileObject = (OrderedMap<String, Object>) s.value;
			temp.x = ((Float) tileObject.get("x")).intValue();
			temp.y = ((Float) tileObject.get("y")).intValue();
			temp.w = ((Float) tileObject.get("width")).intValue();
			temp.h = ((Float) tileObject.get("height")).intValue();
			tileDesc.put(s.key, temp);
		}
		this.idBase = (String) root.get("name");
	}

	@Override
	public Tile getBackgroundTile(int x, int y) {
		TileDescriptor t = tileDesc.get("background");
		int xoff = random.nextInt(t.w);
		int yoff = random.nextInt(t.h);
		return new JSONBackgroundTile(idBase + ".bg", t.x + xoff, t.y + yoff,
				x, y);
	}

	@Override
	public Tile getFloorTile(int x, int y) {
		TileDescriptor t = tileDesc.get("floor");
		int xoff = random.nextInt(t.w);
		int yoff = random.nextInt(t.h);
		return new JSONFloorTile(idBase + ".floor", t.x + xoff, t.y + yoff, x,
				y);
	}

	@Override
	public Tile getWallTile(int x, int y) {
		TileDescriptor t = tileDesc.get("wall");
		int xoff = random.nextInt(t.w);
		int yoff = random.nextInt(t.h);
		return new JSONWallTile(idBase + ".wall", t.x + xoff, t.y + yoff, x, y);
	}

	@Override
	public Tile getDoorTile(int x, int y) {
		TileDescriptor tC = tileDesc.get("doorClosed");
		int xoffC = random.nextInt(tC.w);
		int yoffC = random.nextInt(tC.h);
		TileDescriptor tO = tileDesc.get("doorOpen");
		int xoffO = random.nextInt(tO.w);
		int yoffO = random.nextInt(tO.h);
		return new JSONDoorTile(idBase + ".door", tC.x + xoffC, tC.y + yoffC,
				tO.x + xoffO, tO.y + yoffO, x, y);
	}

	@Override
	public Tile getStair(int x, int y, StairDirection dir) {
		TileDescriptor t = null;
		if (dir == StairDirection.UP) {
			t = tileDesc.get("upstair");
		} else {
			t = tileDesc.get("downstair");
		}
		int xoff = random.nextInt(t.w);
		int yoff = random.nextInt(t.h);
		return new JSONStairsTile(idBase + ".stair" + dir, t.x + xoff, t.y
				+ yoff, x, y, dir);
	}

}
