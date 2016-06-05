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

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;

import fr.evercraft.everapi.services.pvp.event.FightEvent;


public class EPListener {
	private EverPVP plugin;
	
	public EPListener(EverPVP plugin) {
		this.plugin = plugin;
	}
	
	@Listener
	public void onEntityDeath(DamageEntityEvent event) {
		Optional<EntityDamageSource> optDamageSource = event.getCause().first(EntityDamageSource.class);
	    if(optDamageSource.isPresent()) {
	        Entity entity = optDamageSource.get().getSource();
	        Entity victim = event.getTargetEntity();
	        if(entity instanceof Player && victim instanceof Player) {
	        	this.plugin.getEServer().broadcast("test 1");
	        	this.plugin.getService().add(entity.getUniqueId(), victim.getUniqueId(), false);
	        	this.plugin.getService().add(victim.getUniqueId(), entity.getUniqueId(), true);
	        }	
	    }
	}
	
	@Listener
	public void onPlayerFight(FightEvent.Start event) {
		this.plugin.getEServer().broadcast("test 2");
	}
}
