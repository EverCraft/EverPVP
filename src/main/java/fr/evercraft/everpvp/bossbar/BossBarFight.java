/**
 * This file is part of EverPVP.
 *
 * EverPVP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EverPVP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EverPVP.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * This file is part of EverInformations.
 *
 * EverInformations is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EverInformations is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EverInformations.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.evercraft.everpvp.bossbar;

import java.util.Optional;

import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.boss.BossBarOverlay;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.text.Text;

import fr.evercraft.everapi.server.player.EPlayer;
import fr.evercraft.everapi.services.priority.PriorityService;
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
		
		Optional<ServerBossBar> bossbar = player.getBossBar(this.priority);
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
			return player.addBossBar(this.priority, ServerBossBar.builder()
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
		return player.removeBossBar(this.priority);
	}
	
}
