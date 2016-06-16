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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.boss.BossBarOverlay;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.text.Text;

import fr.evercraft.everapi.server.player.EPlayer;
import fr.evercraft.everapi.services.PriorityService;
import fr.evercraft.everpvp.EverPVP;

public class BossBarEndFight {
	
	private final EverPVP plugin;
	
	private int priority;
	private long stay;
	private long percent;

	private String message;
	private BossBarColor color;
	private BossBarOverlay overlay;
	private boolean darkenSky;
	private boolean playEndBossMusic;
	private boolean createFog;
	
	private final ConcurrentHashMap<UUID, Long> players;
	
	public BossBarEndFight(final EverPVP plugin) {
		this.plugin = plugin;
		
		this.players = new ConcurrentHashMap<UUID, Long>();
		
		this.reload();
	}
		
	public void reload() {
		// Stop
		for(Entry<UUID, Long> player_uuid : this.players.entrySet()) {
			Optional<EPlayer> player = this.plugin.getEServer().getEPlayer(player_uuid.getKey());
			if(player.isPresent()) {
				player.get().removeBossBar(ManagerBossBar.IDENTIFIER);
			}
		}
		this.players.clear();
		
		// Start
		this.priority = PriorityService.DEFAULT;
		if(this.plugin.getEverAPI().getManagerService().getPriority().isPresent()) {
			this.priority = this.plugin.getEverAPI().getManagerService().getPriority().get().getBossBar(ManagerBossBar.IDENTIFIER);
		}
		
		this.stay = this.plugin.getConfigs().getBossBarEndFightStay();
		this.percent = this.plugin.getConfigs().getBossBarEndFightPercent() / 100;
		this.message = this.plugin.getConfigs().getBossBarEndFightMessage();
		this.color = this.plugin.getConfigs().getBossBarEndFightColor();
		this.overlay = this.plugin.getConfigs().getBossBarEndFightOverlay();
		this.darkenSky = this.plugin.getConfigs().getBossBarEndFightDarkenSky();
		this.playEndBossMusic = this.plugin.getConfigs().getBossBarEndFightPlayEndBossMusic();
		this.createFog = this.plugin.getConfigs().getBossBarEndFightCreateFog();
	}
	
	private boolean send(EPlayer player) {
		Text text = player.replaceVariable(this.message);
		
		Optional<ServerBossBar> bossbar = player.getBossBar(ManagerBossBar.IDENTIFIER);
		if(bossbar.isPresent()) {
			bossbar.get().setName(text);
			bossbar.get().setPercent(this.percent);
			bossbar.get().setColor(this.color);
			bossbar.get().setOverlay(this.overlay);
			bossbar.get().setDarkenSky(this.darkenSky);
			bossbar.get().setPlayEndBossMusic(this.playEndBossMusic);
			bossbar.get().setCreateFog(this.createFog);
			return true;
		} else {
			return player.addBossBar(ManagerBossBar.IDENTIFIER, this.priority, ServerBossBar.builder()
					.name(text)
					.percent(this.percent)
					.color(this.color)
					.overlay(this.overlay)
					.darkenSky(this.darkenSky)
					.playEndBossMusic(this.playEndBossMusic)
					.createFog(this.createFog)
					.build());
		}
	}

	/**
	 * Ajoute la BossBar à un joueur
	 * @param player Le joueur
	 * @return True si la BossBar est bien ajouté
	 */
	public boolean add(EPlayer player){
		if (!this.players.containsKey(player.getUniqueId())) {
			this.send(player);
			this.plugin.getTask().reload();
		}
		this.players.put(player.getUniqueId(), System.currentTimeMillis() + (this.stay * 1000));
		return true;
	}
	
	/**
	 * Supprime la BossBar
	 * @param player Le joueur
	 * @return True si la BossBar est bien supprimé
	 */
	public boolean remove(EPlayer player) {
		if (this.players.remove(player.getUniqueId()) != null){
			player.removeBossBar(ManagerBossBar.IDENTIFIER);
			this.plugin.getTask().reload();
			return true;
		}
		return false;
	}
	
	/*
	 * Task : Mise à jour
	 */
	
	/**
	 * Savoir si il faut mettre à jour
	 * @return True si il faut mettre à jour
	 */
	public boolean update() {
		return !this.players.isEmpty();
	}
	
	/**
	 * Mise à jour Async
	 */
	public void updateAsync() {
		final List<UUID> players = new ArrayList<UUID>();
		long time = System.currentTimeMillis();
		for(Entry<UUID, Long> player : this.players.entrySet()) {
			if(player.getValue() <= time) {
				players.add(player.getKey());
			}
		}
		
		this.plugin.getGame().getScheduler().createTaskBuilder()
				.execute(() -> updateSync(players))
				.submit(this.plugin);
	}
	
	/**
	 * Mise à jour Sync
	 */
	private void updateSync(final List<UUID> players) {
		for(UUID player_uuid : players) {
			Optional<EPlayer> player = this.plugin.getEServer().getEPlayer(player_uuid);
			if(player.isPresent()) {
				this.remove(player.get());
			}
		}
	}
}
