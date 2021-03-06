package tk.sirtwinkles.spicedtea.world.gen.dungeon.carve.room;

import java.util.HashMap;

import tk.sirtwinkles.spicedtea.Globals;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.OrderedMap;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class RoomFeatureFactory {
	static HashMap<String, RoomFeature> templates;

	public static RoomFeature getFeature(String name) {
		return templates.get(name.toLowerCase());
	}
	
	public static void buildTemplates() {
		templates = new HashMap<String, RoomFeature>();
		final String basePath = "data/config/rooms/";
		String searchPath = basePath;
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			searchPath = "./bin/" + basePath;
		}
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		System.out.println("Begin parsing rooms");
		for (FileHandle f : Gdx.files.internal(searchPath).list()) {
			if (!f.extension().equalsIgnoreCase("json")) {
				continue;
			}
			String cfgString = Globals.assets.get(basePath + f.name());
			Globals.assets.unload(basePath + f.name());
			OrderedMap<String, Object> config = (OrderedMap<String, Object>) Globals.json
					.parse(cfgString);
			
			String type = ((String) config.get("type")).toLowerCase();
			System.out.println("Loading feature: " + type);
			String className = (String) config.get("generator");
			
			try {
				Class c = loader.loadClass(className);
				if (!RoomFeature.class.isAssignableFrom(c)) {
					throw new Exception("Generator for type " + type + " is not a subclass of RoomFeature");
				}
				templates.put(type, (RoomFeature) c.getConstructor().newInstance());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		System.out.print("Loaded room features: ");
		System.out.println(templates);
	}
	
	private RoomFeatureFactory() {}
}
