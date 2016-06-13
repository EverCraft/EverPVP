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
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.entity.projectile.arrow.Arrow;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import fr.evercraft.everapi.plugin.EChat;
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
	        if(targetEntity instanceof Player){
		        if(entity instanceof Player) {
		        	this.plugin.getService().add(entity.getUniqueId(), targetEntity.getUniqueId(), false);
		        	this.plugin.getService().add(targetEntity.getUniqueId(), entity.getUniqueId(), true);
		        }
		        if(entity instanceof Projectile){
		        	ProjectileSource projectile = ((Projectile)entity).getShooter();
		        	if (projectile instanceof Player){
	        			Player shooter = (Player) projectile;
	        			Player victim = (Player) targetEntity;
			        	this.plugin.getService().add(shooter.getUniqueId(), victim.getUniqueId(), false);
			        	this.plugin.getService().add(victim.getUniqueId(), shooter.getUniqueId(), true);
	        			if(shooter.hasPermission(EPPermissions.ARROW.get()) && entity instanceof Arrow){
				        	Double heal = (victim.get(Keys.HEALTH).get() - event.getFinalDamage());
		        			shooter.sendMessage(EChat.of(EPMessages.PREFIX.get() + EPMessages.ARROW_INFORMATION.get()
		        					.replaceAll("<player>", victim.getName())
		        					.replaceAll("<heal>", heal.toString())));
		        		}
		        	}
		        }
	        }
	    }
	}
	
	@Listener
	public void onPlayerFight(FightEvent.Start event) {
		this.plugin.getEServer().broadcast("FightEvent.Start : " + event.getPlayer().getName());
	}
	
	@Listener
	public void onPlayerDisconnected(ClientConnectionEvent.Disconnect event) {
		Player player = event.getTargetEntity();
		this.plugin.getEServer().broadcast("" + player.getName());
		this.plugin.getService().remove(player.getUniqueId());
	}
	
	@Listener
	public void onPlayerDeath(DestructEntityEvent.Death event) {
		if(event.getTargetEntity() instanceof Player){
			Player player = (Player) event.getTargetEntity();
			this.plugin.getEServer().broadcast("" + player.getName());
			this.plugin.getService().remove(player.getUniqueId());
		}
	}
}