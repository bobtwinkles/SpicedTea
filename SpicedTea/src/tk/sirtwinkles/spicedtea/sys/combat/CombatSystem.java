package tk.sirtwinkles.spicedtea.sys.combat;

import java.util.LinkedList;

import static tk.sirtwinkles.spicedtea.MathUtils.abs;
import static tk.sirtwinkles.spicedtea.MathUtils.random;
import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.components.HealthComponent;
import tk.sirtwinkles.spicedtea.components.PositionComponent;
import tk.sirtwinkles.spicedtea.components.XPComponent;
import tk.sirtwinkles.spicedtea.state.PlayingState;
import tk.sirtwinkles.spicedtea.sys.System;

public class CombatSystem extends System {
	private LinkedList<Attack> attacks;

	public CombatSystem() {
		super("combat");
		this.attacks = new LinkedList<Attack>();
	}

	@Override
	public void run(GameSpicedTea game, PlayingState play) {
		while (attacks.size() > 0) {
			Attack attack = attacks.removeFirst();
			final int weaponPower = 4; // Arbitrary, should be from weapons, but timez =(
			final XPComponent instigatorXP = (XPComponent)(attack.instigator.getComponent("xp"));
			final XPComponent targetXP = (XPComponent) attack.target.getComponent("xp");
			final PositionComponent instigatorPos = (PositionComponent) attack.instigator.getComponent("position");
			final PositionComponent targetPos = (PositionComponent) attack.target.getComponent("position");
			
			if (abs(instigatorPos.x - targetPos.x) + abs(instigatorPos.y - targetPos.y) <= 1) {
				final double damageFactor = 
						(Math.sin((Math.PI * instigatorXP.level) / (XPComponent.MAX_LEVEL * 2)) + 1 - (targetXP.level / (float)XPComponent.MAX_LEVEL)) / 2 + random.nextDouble() * 0.1;
				int dmg = (int)(weaponPower * damageFactor * damageFactor);
				HealthComponent hc = (HealthComponent) attack.target.getComponent("health");
				hc.damage(dmg);
				((XPComponent)attack.instigator.getComponent("xp")).xp += dmg;
				((XPComponent)attack.instigator.getComponent("xp")).update(game, play);
				//java.lang.System.out.println(attack.instigator.getID() + " hit " + attack.target.getID() + " for " + dmg);
			}
		}
	}

	public void queueAttack(Attack attack) {
		this.attacks.addLast(attack);
	}
}