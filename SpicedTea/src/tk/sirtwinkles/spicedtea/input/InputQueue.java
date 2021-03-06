package tk.sirtwinkles.spicedtea.input;

import java.util.LinkedList;

import tk.sirtwinkles.spicedtea.input.TouchEvent.EventType;

import com.badlogic.gdx.InputProcessor;

public class InputQueue implements InputProcessor {
	private LinkedList<TouchEvent> touchEvents;
	private LinkedList<KeyEvent> keyEvents;
	
	public InputQueue() {
		this.touchEvents = new LinkedList<TouchEvent>();
		this.keyEvents = new LinkedList<KeyEvent>();
	}

	@Override
	public boolean keyDown(int keycode) {
		keyEvents.add(new KeyEvent(keycode, true));
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		keyEvents.add(new KeyEvent(keycode, false));
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false; // We don't want to handle these, just up/down events.
						// Maybe a seperate queue for them if needed in the
						// future.
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touchEvents.add(new TouchEvent(screenX, screenY, pointer, EventType.PRESSED));
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touchEvents.add(new TouchEvent(screenX, screenY, pointer, EventType.RELEASED));
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		touchEvents.add(new TouchEvent(screenX, screenY, pointer, EventType.DRAGGED));
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// We don't need these.
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// Don't need this either (probably)
		return false;
	}

	/**
	 * Queries the touch event queue for events.
	 * @return If there are events to be processed.
	 */
	public boolean needTouchProcessing() {
		return touchEvents.size() > 0;
	}
	
	/**
	 * Gets the touch event queue.
	 * @return The touch event queue.
	 */
	public LinkedList<TouchEvent> getTouchEvents() {
		return touchEvents;
	}

	/**
	 * Queries the key event queue for events.
	 * @return If there are events to be processed.
	 */
	public boolean needKeyProcessing() {
		return keyEvents.size() > 0;
	}
	
	/**
	 * Gets the key event queue.
	 * @return The key event queue.
	 */
	public LinkedList<KeyEvent> getKeyEvents() {
		return keyEvents;
	}

	public void clearQueues() {
		keyEvents.clear();
		touchEvents.clear();
	}
}
