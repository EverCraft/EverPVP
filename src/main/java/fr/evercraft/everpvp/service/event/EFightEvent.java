package fr.evercraft.everpvp.service.event;

import org.spongepowered.api.event.cause.Cause;

import fr.evercraft.everapi.event.FightEvent;
import fr.evercraft.everapi.server.player.EPlayer;

public abstract class EFightEvent implements FightEvent {	

	private final Type type;
	private final EPlayer player;
	private final Cause cause;

    public EFightEvent(final Type type, final EPlayer player, final Cause cause) {
    	this.type = type;
    	this.player = player;
    	this.cause = cause;
    }

    public Type getType() {
        return this.type;
    }
    
    public EPlayer getPlayer() {
		return player;
	}
    
    @Override
	public Cause getCause() {
		return this.cause;
	}
}
