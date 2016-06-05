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
package fr.evercraft.everpvp;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.event.cause.Cause;

import fr.evercraft.everapi.server.player.EPlayer;
import fr.evercraft.everapi.services.pvp.PVPService;
import fr.evercraft.everapi.services.pvp.event.EStartFightEvent;
import fr.evercraft.everapi.services.pvp.event.EStopFightEvent;

public class EPVPService implements PVPService{
	private final ConcurrentHashMap<UUID, Long> players;
	private final EverPVP plugin;
	private long cooldown; 
	
	public EPVPService(final EverPVP plugin){
		this.plugin = plugin;
		this.players = new ConcurrentHashMap<UUID, Long>();
		this.cooldown = 0;
		reload();
	}
	
	public void reload() {
		this.players.clear();
		this.cooldown = this.plugin.getConfigs().getCooldown();
	}
	
	@Override
	public boolean isFight(UUID uuid) {
		if(players.contains(uuid)){
			return true;
		} else {
			return false;
		}
	}

	public boolean isFight(EPlayer player) {
		if(players.contains(player.getUniqueId())){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Optional<Long> getTime(UUID uuid) {
		Long time = null;
		if(players.contains(uuid)){
			time = players.get(uuid).longValue();
		}
		return Optional.ofNullable(time);
	}
	
	public boolean add(UUID player_uuid, UUID other_uuid, boolean victim){
		if (!this.players.contains(player_uuid)){
			this.plugin.getLogger().debug("FightEvent.Start : (UUID='" + player_uuid + "')");
			this.plugin.getGame().getEventManager().post(new EStartFightEvent(player_uuid, other_uuid, victim, Cause.source(this.plugin).build()));
		}
		this.players.put(uuid.getUniqueId(), System.currentTimeMillis() + this.cooldown);
		return true;
	}
	
	public boolean remove(EPlayer player){
		if (this.players.remove(player.getUniqueId()) != null){
			this.plugin.getLogger().debug("FightEvent.Stop : (UUID='" + player.getUniqueId() + "')");
			this.plugin.getGame().getEventManager().post(new EStopFightEvent(player, Cause.source(this.plugin).build()));
			return true;
		} else {
			return false;
		}
	}
}
