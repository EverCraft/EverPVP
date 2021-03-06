/*
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
package fr.evercraft.everpvp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import fr.evercraft.everapi.event.FightEvent;
import fr.evercraft.everapi.server.player.EPlayer;
import fr.evercraft.everapi.services.PVPService;
import fr.evercraft.everpvp.EverPVP;

public class EPVPService implements PVPService{
	private final ConcurrentHashMap<UUID, Long> players;
	private final ConcurrentHashMap<UUID, Long> players_end;
	private final EverPVP plugin;
	private long cooldown; 
	
	public EPVPService(final EverPVP plugin){
		this.plugin = plugin;
		this.players = new ConcurrentHashMap<UUID, Long>();
		this.players_end = new ConcurrentHashMap<UUID, Long>();
		this.cooldown = 0;
		
		this.reload();
	}
	
	public void reload() {
		this.unTagAll();
		
		// Start
		this.cooldown = this.plugin.getConfigs().getCooldown() * 1000;
	}
	
	@Override
	public void unTagAll() {
		for (Entry<UUID, Long> player_uuid : this.players.entrySet()) {
			Optional<EPlayer> player = this.plugin.getEServer().getEPlayer(player_uuid.getKey());
			if (player.isPresent()) {
				// BossBar
				this.plugin.getManagerBossBar().getFight().remove(player.get());
				// Event
				this.plugin.getManagerEvent().fightStop(player.get(), FightEvent.Stop.Reason.PLUGIN);
			}
		}
		
		this.players.clear();
		this.players_end.clear();
	}
	
	@Override
	public boolean isFight(UUID uuid) {
		return players.containsKey(uuid);
	}

	@Override
	public Optional<Long> getTime(UUID player_uuid) {
		if (this.isFight(player_uuid)){
			return Optional.of(this.players.get(player_uuid));
		}
		return Optional.empty();
	}
	
	public boolean add(UUID player_uuid, UUID other_uuid, boolean victim) {
		long time = System.currentTimeMillis() + this.cooldown;
		// Si le joueur est pas encore en combat
		if (!this.isFight(player_uuid)) {
			Optional<EPlayer> player = this.plugin.getEServer().getEPlayer(player_uuid);
			if (player.isPresent()) {
				// BossBar
				this.plugin.getManagerBossBar().getFight().send(player.get(), time);
				// Event
				this.plugin.getManagerEvent().fightStart(player.get(), other_uuid, victim);
			}
			// Task
			this.plugin.getTask().reload();
		}
		this.players.put(player_uuid, time);
		return true;
	}
	
	public boolean remove(UUID player_uuid, FightEvent.Stop.Reason reason){
		// Si le joueur n'était pas de combat
		if (this.players.remove(player_uuid) != null) {
			Optional<EPlayer> player = this.plugin.getEServer().getEPlayer(player_uuid);
			if (player.isPresent()) {
				// BossBar
				this.plugin.getManagerEvent().fightStop(player.get(), reason);
				// Event
				this.plugin.getManagerBossBar().getEndFight().add(player.get());
			}
			// Task
			this.plugin.getTask().reload();
			return true;
		} else {
			return false;
		}
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
		for (Entry<UUID, Long> player_uuid : this.players.entrySet()) {
			if (player_uuid.getValue() <= time) {
				players.add(player_uuid.getKey());
			} else {
				Optional<EPlayer> player = this.plugin.getEServer().getEPlayer(player_uuid.getKey());
				if (player.isPresent()) {
					// BossBar
					this.plugin.getManagerBossBar().getFight().send(player.get(), player_uuid.getValue());
				}
			}
		}
		
		if (!players.isEmpty()) {
			this.plugin.getGame().getScheduler().createTaskBuilder()
					.execute(() -> updateSync(players))
					.submit(this.plugin);
		}
	}
	
	/**
	 * Mise à jour Sync
	 */
	private void updateSync(final List<UUID> players) {
		for (UUID player : players) {
			this.remove(player, FightEvent.Stop.Reason.TIME);
		}
	}
}
