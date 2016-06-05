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
package fr.evercraft.everpvp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.scheduler.Task;

import fr.evercraft.everapi.server.player.EPlayer;
import fr.evercraft.everapi.services.pvp.PVPService;
import fr.evercraft.everpvp.EverPVP;

public class EPVPService implements PVPService{
	private final ConcurrentHashMap<UUID, Long> players;
	private final EverPVP plugin;
	private long cooldown; 
	
	private Task task;
	
	public EPVPService(final EverPVP plugin){
		this.plugin = plugin;
		this.players = new ConcurrentHashMap<UUID, Long>();
		this.cooldown = 0;
		reload();
	}
	
	public void reload() {
		this.players.clear();
		this.cooldown = this.plugin.getConfigs().getCooldown();
		
		for(Entry<UUID, Long> player : this.players.entrySet()) {
			this.plugin.getManagerEvent().fightStop(player.getKey());
		}
	}
	
	@Override
	public boolean isFight(UUID uuid) {
		if(players.containsKey(uuid)){
			return true;
		} else {
			return false;
		}
	}

	public boolean isFight(EPlayer player) {
		if(players.containsKey(player.getUniqueId())){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Optional<Long> getTime(UUID uuid) {
		Long time = null;
		if(players.containsKey(uuid)){
			time = players.get(uuid).longValue();
		}
		return Optional.ofNullable(time);
	}
	
	public boolean add(UUID player_uuid, UUID other_uuid, boolean victim){
		if (!this.players.containsKey(player_uuid)){
			this.plugin.getManagerEvent().fightStart(player_uuid, other_uuid, victim);
			this.taskCheck();
		}
		this.players.put(player_uuid, System.currentTimeMillis() + this.cooldown);
		return true;
	}
	
	public boolean remove(UUID player_uuid){
		if (this.players.remove(player_uuid) != null){
			this.plugin.getManagerEvent().fightStop(player_uuid);
			this.taskCheck();
			return true;
		} else {
			return false;
		}
	}
	
	private void taskCheck() {
		// Si il y a pas de scheduler et qu'il y a des joueurs
		if(this.task == null && !this.players.isEmpty()) {
			this.taskStart();
		// Si il y a un scheduler et qu'il n'y a pas de joueur
		} else if(this.task != null && this.players.isEmpty()) {
			this.taskStop();
		}
	}
	
	private boolean taskStart() {
		if(this.task == null) {
			this.task = this.plugin.getGame().getScheduler().createTaskBuilder()
					.execute(() -> taskAsyncUpdate())
					.delay(60, TimeUnit.MINUTES)
					.interval(60, TimeUnit.MINUTES)
					.async()
					.submit(this.plugin);
			return true;
		}
		return false;
	}
	
	private boolean taskStop() {
		if(this.task != null) {
			this.task.cancel();
			return true;
		}
		return false;
	}
	
	private void taskAsyncUpdate() {
		final List<UUID> players = new ArrayList<UUID>();
		long time = System.currentTimeMillis();
		for(Entry<UUID, Long> player : this.players.entrySet()) {
			if(player.getValue() <= time) {
				players.add(player.getKey());
			}
		}
		
		this.task = this.plugin.getGame().getScheduler().createTaskBuilder()
				.execute(() -> taskSyncUpdate(players))
				.submit(this.plugin);
	}
	
	private void taskSyncUpdate(final List<UUID> players) {
		for(UUID player : players) {
			this.remove(player);
		}
	}
}
