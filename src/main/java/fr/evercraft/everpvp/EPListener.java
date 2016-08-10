/*
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
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import fr.evercraft.everapi.server.player.EPlayer;
import fr.evercraft.everpvp.EPMessage.EPMessages;


public class EPListener {
	private EverPVP plugin;
	private boolean disconnectedInFight;
	
	public EPListener(EverPVP plugin) {
		this.plugin = plugin;
		reload();
	}
	
	private void reload() {
		disconnectedInFight = this.plugin.getConfigs().get("disconnected-in-fight").getBoolean();
	}

	@Listener
	public void onEntityDamage(DamageEntityEvent event) {
		Optional<EntityDamageSource> optDamageSource = event.getCause().first(EntityDamageSource.class);
	    if(optDamageSource.isPresent() && !event.willCauseDeath()) {
	    	Entity entity = optDamageSource.get().getSource();
	        Entity targetEntity = event.getTargetEntity();
	        if(targetEntity instanceof Player){
	        	Player victim = (Player) targetEntity;
		        if(entity instanceof Projectile){
		        	ProjectileSource projectile = ((Projectile)entity).getShooter();
		        	if (projectile instanceof Player){
		        		Optional<EPlayer> optShooter = this.plugin.getEServer().getEPlayer(((Player) projectile).getUniqueId());
			        	if(optShooter.isPresent()){
			        		EPlayer shooter = optShooter.get();
			        		if(!shooter.equals(victim)){
					        	this.plugin.getService().add(shooter.getUniqueId(), victim.getUniqueId(), false);
					        	this.plugin.getService().add(victim.getUniqueId(), shooter.getUniqueId(), true);
			        			if(shooter.hasPermission(EPPermissions.ARROW.get()) && entity instanceof Arrow){
						        	Double heal = (victim.get(Keys.HEALTH).get() - event.getFinalDamage());
				        			shooter.sendMessage(EPMessages.PREFIX.get() + EPMessages.ARROW_INFORMATION.get()
				        					.replaceAll("<player>", victim.getName())
				        					.replaceAll("<heal>", heal.toString()));
			        			}
			        		}
			        	}
		        	}
		        } else if(entity instanceof Player) {
	        		this.plugin.getService().add(entity.getUniqueId(), targetEntity.getUniqueId(), false);
		        	this.plugin.getService().add(targetEntity.getUniqueId(), entity.getUniqueId(), true);
		        }
	        }
	    }
	}
	
	@Listener
	public void onPlayerDisconnected(ClientConnectionEvent.Disconnect event) {
		Optional<EPlayer> optEPlayer = this.plugin.getEServer().getEPlayer(event.getTargetEntity());
		if(optEPlayer.isPresent()){
			EPlayer player = optEPlayer.get();
			// Le joueur déconnecte en étant en combat
			if(this.plugin.getService().isFight(player.getUniqueId())){
				this.plugin.getService().remove(player.getUniqueId());
				if(getDisconnect()){
					// Le joueur vient de déconnecter en combat
					player.setHealth(0);
					this.plugin.getEServer().broadcast(EPMessages.PREFIX.get() + EPMessages.ENTITY_DAMAGE_PLAYER_DISCONNECT.get()
							.replaceAll("<victim>", player.getDisplayName()));
					this.plugin.getArmorStand().spawnArmorStand(player);
				}
			}
		}
	}
	
	@Listener
	public void onPlayerDeath(DestructEntityEvent event) {
		if(event.getTargetEntity() instanceof Player){
			Optional<DamageSource> optDamageSource = event.getCause().first(DamageSource.class);
			Player victim = (Player) event.getTargetEntity();
			if (optDamageSource.isPresent()) {
				// Fin du FightEvent
				this.plugin.getService().remove(victim.getUniqueId());
			}
			this.plugin.getArmorStand().spawnArmorStand(victim);
		}
	}
	
	private boolean getDisconnect(){
		return this.disconnectedInFight;
	}
}
