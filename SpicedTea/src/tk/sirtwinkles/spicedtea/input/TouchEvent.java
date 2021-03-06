package tk.sirtwinkles.spicedtea.input;

public class TouchEvent {
	public enum EventType {
		PRESSED, RELEASED, DRAGGED
	}
	
	private int x, y, pointer;
	private EventType type;
	
	public TouchEvent(int x, int y, int pointer, EventType type) {
		this.x = x;
		this.y = y;
		this.pointer = pointer;
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getPointer() {
		return pointer;
	}
	
	public EventType getType() {
		return type;
	}
}
