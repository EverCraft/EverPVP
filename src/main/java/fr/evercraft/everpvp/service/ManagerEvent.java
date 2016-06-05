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
	
	public void fightStart(UUID player_uuid, UUID other_uuid, boolean victim) {
		Optional<EPlayer> player = this.plugin.getEServer().getEPlayer(player_uuid);
		Optional<EPlayer> other = this.plugin.getEServer().getEPlayer(other_uuid);
		if(player.isPresent() && other.isPresent()) {
			this.fightStart(player.get(), other.get(), victim);
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
