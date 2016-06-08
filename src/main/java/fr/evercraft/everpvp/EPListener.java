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

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.arrow.Arrow;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;

import fr.evercraft.everapi.server.player.EPlayer;
import fr.evercraft.everapi.services.pvp.event.FightEvent;
import fr.evercraft.everpvp.EPMessage.EPMessages;


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
	        Entity targetEntity = event.getTargetEntity();
	        // Event FightEvent.Start
	        if(entity instanceof Player && targetEntity instanceof Player) {
	        	this.plugin.getService().add(entity.getUniqueId(), targetEntity.getUniqueId(), false);
	        	this.plugin.getService().add(targetEntity.getUniqueId(), entity.getUniqueId(), true);
	        }
	        
	        if(entity instanceof Arrow){
	        	ProjectileSource proj = ((Arrow)entity).getShooter();
	        	if (proj instanceof Player){
	        		Optional<EPlayer> optShooter = this.plugin.getEServer().getEPlayer(((Player)proj));
	        		Optional<EPlayer> optVictim = this.plugin.getEServer().getEPlayer(targetEntity.getUniqueId());
	        		if(optShooter.isPresent() && optVictim.isPresent()){
	        			EPlayer shooter = optShooter.get();
	        			EPlayer victim = optVictim.get();
	        			Double heal = (victim.get(Keys.HEALTH).get() - event.getFinalDamage()) /2;
	        			shooter.sendMessage(EPMessages.PREFIX.get() + EPMessages.ARROW_INFORMATION.get()
	        					.replaceAll("<player>", victim.getDisplayName())
	        					.replaceAll("<heal>", heal.toString()));
	        		}
	        	}
	        }
	    }
	}
	
	@Listener
	public void onPlayerFight(FightEvent.Start event) {
		this.plugin.getEServer().broadcast("test 2");
	}
}
