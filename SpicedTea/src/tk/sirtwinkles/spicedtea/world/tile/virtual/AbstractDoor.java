package tk.sirtwinkles.spicedtea.world.tile.virtual;

import tk.sirtwinkles.spicedtea.Globals;
import tk.sirtwinkles.spicedtea.components.PlayerDriverComponent;
import tk.sirtwinkles.spicedtea.components.PositionComponent;
import tk.sirtwinkles.spicedtea.entities.Entity;
import tk.sirtwinkles.spicedtea.state.TextDisplayState;
import tk.sirtwinkles.spicedtea.world.Level;
import tk.sirtwinkles.spicedtea.world.Point;
import tk.sirtwinkles.spicedtea.world.tile.Tile;
import tk.sirtwinkles.spicedtea.world.tile.TileClass;

public abstract class AbstractDoor extends Tile {
	static boolean doorOpened;
	boolean open;
	int openX, openY;

	public AbstractDoor(String id, int closedX, int closedY, int openX,
			int openY, int x, int y) {
		super(id, closedX, closedY, x, y);
		this.open = false;
		this.openX = openX;
		this.openY = openY;
	}

	public void open() {
		this.open = true;
		imgX = openX;
		imgY = openY;
	}

	@Override
	public void onEntityStep(Entity step, Level in) {
		if (step.getID().equalsIgnoreCase("player")) {
			this.open();
			PositionComponent pc = (PositionComponent) step.getComponent("position");
			in.floodFillVisiblity(pc.x + 0, pc.y + 1);
			in.floodFillVisiblity(pc.x + 0, pc.y - 1);
			in.floodFillVisiblity(pc.x + 1, pc.y + 0);
			in.floodFillVisiblity(pc.x - 1, pc.y + 0);
			if (!doorOpened) {
				Globals.ps.requestStateChange(new TextDisplayState("first_open_door", Globals.ps));
				doorOpened = true;
			}
		}
	}
	
	@Override
	public boolean isPassable(Entity ent, Level in) {
		return ent.getID().equalsIgnoreCase("player") || open;
	}
	
	@Override
	public boolean isPathable(Entity ent, Level in) {
		if (!open && ent.getID().equalsIgnoreCase("player")) {
			PlayerDriverComponent pd = (PlayerDriverComponent) ent.getComponent("player.driver");
			Point goal = pd.getGoal();
			if (goal.x == this.x && goal.y == this.y) {
				return true;
			}
		}
		//We don't care about staying locked if all of our surroundings are already visible.
		if (in.isVisible(x + 0, y + 1) &&
				in.isVisible(x + 1, y + 0) &&
				in.isVisible(x + 0, y - 1) &&
				in.isVisible(x - 1, y + 0)) {
			return true;
		}
		return open;
	}
	
	@Override
	public TileClass getTileClass() {
		return TileClass.DOOR;
	}
}
