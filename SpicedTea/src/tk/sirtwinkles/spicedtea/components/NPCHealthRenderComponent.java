package tk.sirtwinkles.spicedtea.components;

import tk.sirtwinkles.spicedtea.entities.Entity;
import tk.sirtwinkles.spicedtea.sys.render.HealthBarRenderer;

import com.badlogic.gdx.utils.OrderedMap;

public class NPCHealthRenderComponent extends RenderComponent {

	public NPCHealthRenderComponent(OrderedMap<String, Object> config) {
		super(new HealthBarRenderer(), config);
	}

	@Override
	public void setOwner(Entity owner) {
		super.setOwner(owner);
		HealthBarRenderer cpt = (HealthBarRenderer)this.ren;
		cpt.setComponent((HealthComponent) owner.getComponent("health"));
	}
}
