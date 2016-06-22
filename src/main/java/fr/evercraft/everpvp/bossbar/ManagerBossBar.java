package fr.evercraft.everpvp.bossbar;

import fr.evercraft.everpvp.EverPVP;

public class ManagerBossBar {
	public final static String IDENTIFIER = "everpvp";

	private final EverPVP plugin;
	
	private final BossBarFight fight;
	private final BossBarEndFight endFight;
	
	public ManagerBossBar(final EverPVP plugin) {
		this.plugin = plugin;
		
		this.fight = new BossBarFight(this.plugin);
		this.endFight = new BossBarEndFight(this.plugin);
	}
	
	public void reload() {
		this.fight.reload();
		this.endFight.reload();
	}
	
	
	public BossBarFight getFight() {
		return fight;
	}

	public BossBarEndFight getEndFight() {
		return endFight;
	}	
}
