package tk.sirtwinkles.spicedtea.entities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import tk.sirtwinkles.spicedtea.Globals;
import tk.sirtwinkles.spicedtea.components.Component;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;

@SuppressWarnings({ "unchecked", "rawtypes" })
public final class EntityFactory {
	
	static class ComponentConfig {
		Class template;
		Class renderer;
		String[] deps;
	}

	private static HashMap<String, ComponentConfig> componentConfig = new HashMap<String, ComponentConfig>();
	private static HashMap<String, Integer> instanceCount = new HashMap<String, Integer>();

	public static Entity buildEntity(String name) {
		String jsonConfig = Globals.assets.get("data/config/characters/" + name
				+ ".json");
		OrderedMap<String, Object> root = (OrderedMap<String, Object>) Globals.json
				.parse(jsonConfig);
		String trueName = (String) root.get("name");
		if (((String)root.get("instance.type")).equalsIgnoreCase("multiinstance")) {
			String lowerCaseName = name.toLowerCase();
			if (instanceCount.get(lowerCaseName) == null) {
				instanceCount.put(lowerCaseName, 0);
			}
			trueName += instanceCount.get(lowerCaseName);
			instanceCount.put(lowerCaseName, instanceCount.get(lowerCaseName) + 1);
		}
		Entity tr = new Entity(trueName);
		Array<Object> comps = (Array<Object>) root.get("components");

		for (Object c : comps) {
			OrderedMap<String, Object> component = (OrderedMap<String, Object>) c;
			String assetType = ((String) component.get("type")).toLowerCase();
			findAndAddComponent(comps, assetType, tr);
			ComponentConfig comp = componentConfig.get(assetType);
			for (String s : comp.deps) {
				if (tr.getComponent(s) == null) {
					findAndAddComponent(comps, s, tr);
				}
			}
		}

		return tr;
	}

	private static void findAndAddComponent(Array<Object> componentList,
			String name, Entity addTo) {
		for (Object c : componentList) {
			OrderedMap<String, Object> configuration = (OrderedMap<String, Object>) c;
			String componentType = ((String) configuration.get("type"))
					.toLowerCase();
			if (componentConfig.containsKey(componentType)) {
				ComponentConfig comp = componentConfig.get(componentType);
				addTo.addComponent(constructComponent(comp, configuration));
			} else {
				throw new IllegalArgumentException("Unknown component type: "
						+ componentType);
			}
		}
	}
	
	private static Component constructComponent(ComponentConfig cfg,
			OrderedMap<String, Object> cmpCfg) {
		Component tr = null;
		try {
			Constructor constructer = cfg.template
					.getConstructor(OrderedMap.class);
			if (constructer == null) {
				System.err
						.println("Unable to find a constructer with approprite argument.");
				Gdx.app.exit();
			}
			tr = (Component) constructer.newInstance(cmpCfg);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			e.getCause().printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			System.err.println("Known constructors:");
			for (Constructor c : cfg.template.getConstructors()) {
				System.err.println("Constructor " + c);
				int i = 0; 
				for (Class cls : c.getParameterTypes()) {
					System.err.println("\tArg:" + i + ":" + cls.getName());
					++i;
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return tr;
	}

	public static void parseCompoents() throws ClassNotFoundException {
		final String basePath = "data/config/component/";
		String searchPath = basePath;
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			searchPath = "./bin/" + basePath;
		}
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		System.out.println("Begin parsing components");
		for (FileHandle f : Gdx.files.internal(searchPath).list()) {
			if (!f.extension().equalsIgnoreCase("json")) {
				continue;
			}
			String cfgString = Globals.assets.get(basePath + f.name());
			Globals.assets.unload(basePath + f.name());
			OrderedMap<String, Object> config = (OrderedMap<String, Object>) Globals.json
					.parse(cfgString);

			String componentName = ((String) config.get("type")).toLowerCase();

			System.out.println("parsing component: " + componentName);

			String className = (String) config.get("class");

			ComponentConfig myConfig = new ComponentConfig();

			Class template = loader.loadClass(className);
			Array<Object> deps = (Array<Object>) config.get("depends");
			myConfig.deps = new String[deps.size];
			for (int i = 0; i < deps.size; ++i) {
				myConfig.deps[i] = ((String) deps.get(i)).toLowerCase();
			}

			myConfig.template = template;
			componentConfig.put(componentName, myConfig);
		}
	}

	/**
	 * This class should never be instantiated.
	 */
	private EntityFactory() {
	}
}
