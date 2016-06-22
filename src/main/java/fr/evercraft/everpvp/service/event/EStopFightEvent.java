package fr.evercraft.everpvp.service.event;

import org.spongepowered.api.event.cause.Cause;

import fr.evercraft.everapi.event.FightEvent;
import fr.evercraft.everapi.server.player.EPlayer;

public class EStopFightEvent extends EFightEvent implements FightEvent.Stop {	
	public EStopFightEvent(final EPlayer player, final Cause cause) {
    	super(Type.STOP, player, cause);
    }
}
