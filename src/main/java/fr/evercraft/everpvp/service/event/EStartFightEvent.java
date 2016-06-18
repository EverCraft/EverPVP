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
 * This file is part of EverAPI.
 *
 * EverAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EverAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EverAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
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