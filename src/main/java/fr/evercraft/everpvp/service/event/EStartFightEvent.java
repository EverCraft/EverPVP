package fr.evercraft.everpvp.service.event;

import org.spongepowered.api.event.cause.Cause;

import fr.evercraft.everapi.event.FightEvent;
import fr.evercraft.everapi.server.player.EPlayer;

public class EStartFightEvent extends EFightEvent implements FightEvent.Start {	
	private final EPlayer other;
	private final boolean victim;
	
	public EStartFightEvent(final EPlayer player, final EPlayer other, boolean victim, final Cause cause) {
    	super(Type.START, player, cause);

    	this.other = other;
    	this.victim = victim;
    }

	public EPlayer getOther() {
		return other;
	}

	public boolean isVictim() {
		return victim;
	}
}
