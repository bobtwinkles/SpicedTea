package tk.sirtwinkles.spicedtea.state;

import java.util.LinkedList;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.Globals;
import tk.sirtwinkles.spicedtea.input.InputQueue;
import tk.sirtwinkles.spicedtea.input.TouchEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.TimeUtils;

public class TextDisplayState implements GameState {
	private static OrderedMap<String, Object> texts;
	private static OrthographicCamera cam = new OrthographicCamera();
	public static BitmapFont bmf = new BitmapFont(true); 

	private String textKey;
	private GameState tr;
	private int offset;
	private int maxOffset;
	private Texture gui;
	private boolean inputEvent;
	private long startTime;
	private boolean continuous;

	public TextDisplayState(String textKey, GameState tr) {
		this.textKey = textKey;
		this.inputEvent = false;
		this.tr = tr;
		this.gui = Globals.assets.get("data/gui.png");
		this.startTime = TimeUtils.millis();
		this.continuous = Gdx.graphics.isContinuousRendering();
		Gdx.graphics.setContinuousRendering(false);
	}

	@Override
	public void onEnterState(GameSpicedTea game) {
		if (!Globals.assets.isLoaded("data/config/texts.json")) {
			Globals.assets.load("data/config/texts.json", String.class);
		}
		Gdx.gl10.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
	}

	@Override
	public void onLeaveState(GameSpicedTea game) {
		//Reset continuous rendering status.
		Gdx.graphics.setContinuousRendering(continuous);
	}

	@Override
	public boolean switchState(GameSpicedTea game) {
		return 
				//We have seen input
				inputEvent &&
				//We are at the end of the text
				offset >= maxOffset &&
				//We actually know the max offset
				maxOffset != 0 &&
				//And we have been on this screen for at least 3 seconds.
				TimeUtils.millis() - startTime > 3000;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(GameSpicedTea game) {
		if (texts != null) {
			SpriteBatch sb = game.getContext().getBatch();
			int swidth = game.getContext().getWidth();
			int sheight = game.getContext().getHeight();
			cam.setToOrtho(true, swidth, sheight);
			cam.update();
			sb.setProjectionMatrix(cam.projection);
			sb.setTransformMatrix(cam.view);

			Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);

			sb.begin();

			final int width = Globals.GUI_DRAW_SIZE * 9;
			final int height = Globals.GUI_DRAW_SIZE * 6;
			final int xbase = (int) ((swidth / 2) - (width / 2));
			final int ybase = (int) ((sheight / 2) - (height / 2));

			// Draw corners.
			drawTile(sb, xbase - Globals.GUI_DRAW_SIZE, ybase
					- Globals.GUI_DRAW_SIZE, 0, 0);
			drawTile(sb, xbase - Globals.GUI_DRAW_SIZE, ybase + height, 0, 2);
			drawTile(sb, xbase + width, ybase - Globals.GUI_DRAW_SIZE, 1, 3);
			drawTile(sb, xbase + width, ybase + height, 1, 5);
			// Draw vertical thingies
			for (int y = 0; y < height; y += Globals.GUI_DRAW_SIZE) {
				drawTile(sb, xbase - Globals.GUI_DRAW_SIZE, ybase + y, 0, 1);
				drawTile(sb, xbase + width, ybase + y, 1, 4);
			}
			// Draw horizontal thingies
			for (int x = 0; x < width; x += Globals.GUI_DRAW_SIZE) {
				if (x != width - Globals.GUI_DRAW_SIZE) {
					drawTile(sb, xbase + x, ybase - Globals.GUI_DRAW_SIZE, 1, 0);
					drawTile(sb, xbase + x, ybase + height, 1, 2);
				} else {
					drawTile(sb, xbase + x, ybase - Globals.GUI_DRAW_SIZE, 0, 3);
					drawTile(sb, xbase + x, ybase + height, 0, 5);
				}
			}
			// Draw center
			for (int x = 0; x < width; x += Globals.GUI_DRAW_SIZE) {
				for (int y = 0; y < height; y += Globals.GUI_DRAW_SIZE) {
					if (x != width - Globals.GUI_DRAW_SIZE) {
						drawTile(sb, xbase + x, ybase + y, 1, 1);
					} else {
						drawTile(sb, xbase + x, ybase + y, 0, 4);
					}
				}
			}

			// Draw text
			Array<String> str = (Array<String>) texts.get(textKey);
			for (int i = 0; i < str.size; ++i) {
				if (i * 20 - offset < 0) {
					continue;
				}
				if (i * 20 - offset > height - 10) {
					continue;
				}
				bmf.draw(sb, str.get(i), xbase + 10, i * 20 + ybase - offset);
			}

			this.maxOffset = str.size * 20 - height;

			// Draw scroll handle
			drawTile(
					sb,
					xbase + width - 40,
					ybase
							+ (int) (offset * 5.0 * Globals.GUI_DRAW_SIZE / (maxOffset)),
					2, 3);

			sb.end();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void tick(GameSpicedTea game) {
		if (texts == null) {
			if (Globals.assets.isLoaded("data/config/texts.json")) {
				texts = (OrderedMap<String, Object>) Globals.json
						.parse((String) Globals.assets
								.get("data/config/texts.json"));
			} else {
				Globals.assets.update();
			}
		}

		InputQueue iq = game.getInput();
		LinkedList<TouchEvent> touchEvents = iq.getTouchEvents();
		if (touchEvents.size() != 0) {
			while (touchEvents.size() > 0) {
				TouchEvent evnt = touchEvents.removeFirst();

				final int swidth = game.getContext().getWidth();
				final int sheight = game.getContext().getHeight();

				final int width = Globals.GUI_DRAW_SIZE * 9;
				final int height = Globals.GUI_DRAW_SIZE * 6;
				final int xbase = (int) ((swidth / 2) - (width / 2));
				final int ybase = (int) ((sheight / 2) - (height / 2));

				if (xbase + width - Globals.GUI_DRAW_SIZE < evnt.getX()
						&& evnt.getX() < xbase + width) {
					offset = (int) ((evnt.getY() - ybase - (Globals.GUI_DRAW_SIZE / 2)) * ((maxOffset) / (5.0 * Globals.GUI_DRAW_SIZE)));
				}
				if (offset >= maxOffset) {
					if (evnt.getType() == TouchEvent.EventType.PRESSED) {
						inputEvent = true;
					}
				}
			}
		}

		iq.clearQueues();
		if (offset > maxOffset) {
			offset = maxOffset;
		}
		if (offset < 0) {
			offset = 0;
		}
	}

	@Override
	public GameState getNextState(GameSpicedTea game) {
		return tr;
	}

	private void drawTile(SpriteBatch batch, int x, int y, int tx, int ty) {
		batch.draw(gui, x, y, Globals.GUI_DRAW_SIZE, Globals.GUI_DRAW_SIZE, tx
				* Globals.GUI_TILE_SIZE, ty * Globals.GUI_TILE_SIZE,
				Globals.GUI_TILE_SIZE, Globals.GUI_TILE_SIZE, false, true);
	}

}
