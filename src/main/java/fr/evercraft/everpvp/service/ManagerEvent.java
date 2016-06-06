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

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.event.cause.Cause;

import fr.evercraft.everapi.server.player.EPlayer;
import fr.evercraft.everapi.services.pvp.event.EStartFightEvent;
import fr.evercraft.everapi.services.pvp.event.EStopFightEvent;
import fr.evercraft.everpvp.EverPVP;

public class ManagerEvent {
	private final EverPVP plugin;
	
	public ManagerEvent(EverPVP plugin) {
		this.plugin = plugin;
	}
	
	public void fightStart(EPlayer player, UUID other_uuid, boolean victim) {
		Optional<EPlayer> other = this.plugin.getEServer().getEPlayer(other_uuid);
		if(other.isPresent()) {
			this.fightStart(player, other.get(), victim);
		}
	}
	
	public void fightStart(EPlayer player, EPlayer other, boolean victim) {
		this.plugin.getLogger().debug("Event FightEvent.Start : (UUID='" + player.getUniqueId() + "';other='" + other.getUniqueId() + "';victim='" + victim + "')");
		this.plugin.getGame().getEventManager().post(new EStartFightEvent(player, other, victim, Cause.source(this.plugin).build()));
	}
	
	public void fightStop(UUID player_uuid) {
		Optional<EPlayer> player = this.plugin.getEServer().getEPlayer(player_uuid);
		if(player.isPresent()) {
			this.fightStop(player.get());
		}
	}

	public void fightStop(EPlayer player) {
		this.plugin.getLogger().debug("Event FightEvent.Stop : (UUID='" + player.getUniqueId() + "')");
		this.plugin.getGame().getEventManager().post(new EStopFightEvent(player, Cause.source(this.plugin).build()));
	}
}