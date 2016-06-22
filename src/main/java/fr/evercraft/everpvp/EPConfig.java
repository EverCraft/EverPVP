package fr.evercraft.everpvp;

import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlay;
import org.spongepowered.api.boss.BossBarOverlays;

import fr.evercraft.everapi.plugin.file.EConfig;
import fr.evercraft.everapi.plugin.file.EMessage;
import fr.evercraft.everapi.sponge.UtilsBossBar;

public class EPConfig extends EConfig {

	public EPConfig(EverPVP plugin) {
		super(plugin);
	}
	
	public void reload() {
		super.reload();
		this.plugin.getLogger().setDebug(this.isDebug());
	}

	public void loadDefault() {
		addDefault("debug", false, "Displays plugin performance in the logs");
		addDefault("language", EMessage.FRENCH, "Select language messages", "Examples : ", "  French : FR_fr", "  English : EN_en");
		addDefault("cooldown-fight", 45);
		
		// BossBar : Fight
		addDefault("bossbar.fight.message", "&4Vous êtes en combat pour encore <time> !");
		addDefault("bossbar.fight.color", BossBarColors.RED.getName());
		addDefault("bossbar.fight.overlay", BossBarOverlays.PROGRESS.getName());
		addDefault("bossbar.fight.darkenSky", false);
		addDefault("bossbar.fight.playEndBossMusic", false);
		addDefault("bossbar.fight.createFog", false);
		
		// BossBar : EndFight
		addDefault("bossbar.end-fight.stay", 15);
		addDefault("bossbar.end-fight.percent", 100);
		addDefault("bossbar.end-fight.message", "&aVous n'êtes plus en combat !");
		addDefault("bossbar.end-fight.color", BossBarColors.GREEN.getName());
		addDefault("bossbar.end-fight.overlay", BossBarOverlays.PROGRESS.getName());
		addDefault("bossbar.end-fight.darkenSky", false);
		addDefault("bossbar.end-fight.playEndBossMusic", false);
		addDefault("bossbar.end-fight.createFog", false);
	}

	public long getCooldown() {
		return this.get("cooldown-fight").getLong(45);
	}
	
	/*
	 * BossBar Fight
	 */
	
	public String getBossBarFightMessage() {
		return this.plugin.getChat().replace(this.get("bossbar.fight.message").getString(""));
	}
	
	public BossBarColor getBossBarFightColor() {
		return UtilsBossBar.getColor(this.get("bossbar.fight.color").getString("")).orElse(BossBarColors.RED);
	}
	
	public BossBarOverlay getBossBarFightOverlay() {
		return UtilsBossBar.getOverlay(this.get("bossbar.fight.overlay").getString("")).orElse(BossBarOverlays.PROGRESS);
	}
	
	public boolean getBossBarFightDarkenSky() {
		return this.get("bossbar.fight.darkenSky").getBoolean(false);
	}
	
	public boolean getBossBarFightPlayEndBossMusic() {
		return this.get("bossbar.fight.playEndBossMusic").getBoolean(false);
	}

	public boolean getBossBarFightCreateFog() {
		return this.get("bossbar.fight.createFog").getBoolean(false);
	}
	
	/*
	 * BossBar EndFight
	 */
	
	public long getBossBarEndFightStay() {
		return this.get("bossbar.end-fight.stay").getLong(15);
	}
	
	public long getBossBarEndFightPercent() {
		return this.get("bossbar.end-fight.percent").getLong(100);
	}
	
	public String getBossBarEndFightMessage() {
		return this.plugin.getChat().replace(this.get("bossbar.end-fight.message").getString(""));
	}
	
	public BossBarColor getBossBarEndFightColor() {
		return UtilsBossBar.getColor(this.get("bossbar.end-fight.color").getString("")).orElse(BossBarColors.WHITE);
	}
	
	public BossBarOverlay getBossBarEndFightOverlay() {
		return UtilsBossBar.getOverlay(this.get("bossbar.end-fight.overlay").getString("")).orElse(BossBarOverlays.PROGRESS);
	}
	
	public boolean getBossBarEndFightDarkenSky() {
		return this.get("bossbar.end-fight.darkenSky").getBoolean(false);
	}
	
	public boolean getBossBarEndFightPlayEndBossMusic() {
		return this.get("bossbar.end-fight.playEndBossMusic").getBoolean(false);
	}

	public boolean getBossBarEndFightCreateFog() {
		return this.get("bossbar.end-fight.createFog").getBoolean(false);
	}
}