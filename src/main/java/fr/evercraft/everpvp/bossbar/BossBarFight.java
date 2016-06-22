package fr.evercraft.everpvp.bossbar;

import java.util.Optional;

import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.boss.BossBarOverlay;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.text.Text;

import fr.evercraft.everapi.server.player.EPlayer;
import fr.evercraft.everapi.services.PriorityService;
import fr.evercraft.everpvp.EverPVP;

public class BossBarFight {
	
	private final EverPVP plugin;
	
	private int priority;
	private float cooldown;

	private String message;
	private BossBarColor color;
	private BossBarOverlay overlay;
	private boolean darkenSky;
	private boolean playEndBossMusic;
	private boolean createFog;
	
	public BossBarFight(final EverPVP plugin) {
		this.plugin = plugin;
		
		this.reload();
	}
		
	public void reload() {
		this.cooldown = this.plugin.getConfigs().getCooldown() * 1000;
		
		this.priority = PriorityService.DEFAULT;
		if(this.plugin.getEverAPI().getManagerService().getPriority().isPresent()) {
			this.priority = this.plugin.getEverAPI().getManagerService().getPriority().get().getBossBar(ManagerBossBar.IDENTIFIER);
		}
		
		this.plugin.getLogger().warn("Priority : " + this.priority);
		
		this.message = this.plugin.getConfigs().getBossBarFightMessage();
		this.color = this.plugin.getConfigs().getBossBarFightColor();
		this.overlay = this.plugin.getConfigs().getBossBarFightOverlay();
		this.darkenSky = this.plugin.getConfigs().getBossBarFightDarkenSky();
		this.playEndBossMusic = this.plugin.getConfigs().getBossBarFightPlayEndBossMusic();
		this.createFog = this.plugin.getConfigs().getBossBarFightCreateFog();
	}
	
	/**
	 * Ajoute la BossBar à un joueur
	 * @param player Le joueur
	 * @param time Le temps restant
	 * @return True si la BossBar est bien ajouté
	 */
	public boolean send(EPlayer player, long time) {
		Text text = player.replaceVariable(this.message.replaceAll("<time>", this.plugin.getEverAPI().getManagerUtils().getDate().formatDateDiff(time)));
		float percent = Math.max(0, Math.min(1, (time - System.currentTimeMillis()) / this.cooldown));
		
		Optional<ServerBossBar> bossbar = player.getBossBar(ManagerBossBar.IDENTIFIER);
		if(bossbar.isPresent()) {
			bossbar.get().setName(text);
			bossbar.get().setPercent(percent);
			bossbar.get().setColor(this.color);
			bossbar.get().setOverlay(this.overlay);
			bossbar.get().setDarkenSky(this.darkenSky);
			bossbar.get().setPlayEndBossMusic(this.playEndBossMusic);
			bossbar.get().setCreateFog(this.createFog);
			return true;
		} else {
			return player.addBossBar(ManagerBossBar.IDENTIFIER, this.priority, ServerBossBar.builder()
					.name(text)
					.percent(percent)
					.color(this.color)
					.overlay(this.overlay)
					.darkenSky(this.darkenSky)
					.playEndBossMusic(this.playEndBossMusic)
					.createFog(this.createFog)
					.build());
		}
	}

	/**
	 * Supprime la BossBar
	 * @param player Le joueur
	 * @return True si la BossBar est bien supprimé
	 */
	public boolean remove(EPlayer player) {
		return player.removeBossBar(ManagerBossBar.IDENTIFIER);
	}
	
}
