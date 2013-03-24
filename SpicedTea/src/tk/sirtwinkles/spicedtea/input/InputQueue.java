package tk.sirtwinkles.spicedtea.input;

import java.util.LinkedList;

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
		touchEvents.add(new TouchEvent(screenX, screenY, pointer, true));
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touchEvents.add(new TouchEvent(screenX, screenY, pointer, false));
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// We probably don't want these.
		return false;
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
	 * Queries the touch event queue for events.
	 * @return The first event, or null if there are no events.
	 */
	public TouchEvent nextTouchEvent() {
		if (touchEvents.size() > 0) {
			return touchEvents.removeFirst();
		} else {
			return null;
		}
	}

	/**
	 * Queries the key event queue for events.
	 * @return If there are events to be processed.
	 */
	public boolean needKeyProcessing() {
		return keyEvents.size() > 0;
	}

	/**
	 * Queries the key event queue for events.
	 * @return The first event, or null if there are no events.
	 */
	public KeyEvent nextKeyEvent() {
		if (keyEvents.size() > 0) {
			return keyEvents.removeFirst();
		} else {
			return null;
		}
	}
}