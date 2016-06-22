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

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.DamageType;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.BlockDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.FallingBlockDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.entity.DestructEntityEvent;

import fr.evercraft.everapi.plugin.EChat;
import fr.evercraft.everpvp.EPMessage.EPMessages;

public class EPDeathMessage {
	private EverPVP plugin;
	
	public EPDeathMessage(EverPVP plugin) {
		this.plugin = plugin;
	}
	
	@Listener
	public void onPlayerDeath(DestructEntityEvent.Death event) {
		if(event.getTargetEntity() instanceof Player){
			event.clearMessage();
			Player victim = (Player) event.getTargetEntity();
			Optional<IndirectEntityDamageSource> optIndirectEntity = event.getCause().first(IndirectEntityDamageSource.class);
			if(optIndirectEntity.isPresent()){
				IndirectEntityDamageSource damageSource = optIndirectEntity.get();
				sendDeathMessage(damageSource, victim);
			} else {
				Optional<BlockDamageSource> optBlockDamage = event.getCause().first(BlockDamageSource.class);
				if(optBlockDamage.isPresent()){
					BlockDamageSource damageSource = optBlockDamage.get();
					sendDeathMessage(damageSource, victim);
				} else {
					Optional<FallingBlockDamageSource> optFallingBlock = event.getCause().first(FallingBlockDamageSource.class);
					if(optFallingBlock.isPresent()){
						this.plugin.getLogger().debug("Cause : FallingBlockDamageSource");
						FallingBlockDamageSource damageSource = optFallingBlock.get();
						sendDeathMessage(damageSource, victim);
					} else {
						Optional<EntityDamageSource> optEntityDamage = event.getCause().first(EntityDamageSource.class);
						if(optEntityDamage.isPresent()){
							this.plugin.getLogger().debug("Cause : EntityDamageSource");
							EntityDamageSource damageSource = optEntityDamage.get();
							sendDeathMessage(damageSource, victim);
						} else {
							Optional<DamageSource> optDamage = event.getCause().first(DamageSource.class);
							if(optDamage.isPresent()){
								this.plugin.getLogger().debug("Cause : DamageSource");
								DamageSource damageSource = optDamage.get();
								sendDeathMessage(damageSource, victim);
							}
						}
					}
				}
			}
		}
	}
	
	private void sendDeathMessage(IndirectEntityDamageSource damageSource, Player victim){
		DamageType type = damageSource.getType();
		this.plugin.getLogger().debug("Cause : IndirectEntityDamageSource; Type : " + type);
		String message = null;
		// Le killer est un joueur
		if(damageSource.getIndirectSource() instanceof Player){
			Player killer = (Player) damageSource.getIndirectSource();
			if(victim != killer){
				if(type.equals(DamageTypes.ATTACK)){
					message = EPMessages.INDIRECT_DAMAGE_PLAYER_ATTACK.get();	
				} else if(type.equals(DamageTypes.MAGIC)){
					message = EPMessages.INDIRECT_DAMAGE_PLAYER_MAGIC.get();
				} else {
					message = damageSource.toString();
				}
				this.plugin.getEServer().getBroadcastChannel().send(EChat.of(EPMessages.PREFIX.get() + message
						.replaceAll("<player>", victim.getName())
						.replaceAll("<killer>", killer.getName())));
			} else {
				if(type.equals(DamageTypes.ATTACK)){
					message = EPMessages.INDIRECT_DAMAGE_SUICIDE_ATTACK.get();	
				} else if(type.equals(DamageTypes.MAGIC)){
					message = EPMessages.INDIRECT_DAMAGE_SUICIDE_MAGIC.get();
				} else {
					message = damageSource.toString();
				}
				victim.sendMessage(EChat.of(EPMessages.PREFIX.get() + message
						.replaceAll("<player>", victim.getName())));
			}
		// Le killer est une créature
		} else {
			Entity killer = damageSource.getSource();
			if(type.equals(DamageTypes.ATTACK)){
				message = EPMessages.INDIRECT_DAMAGE_ENTITY_ATTACK.get();
			} else if(type.equals(DamageTypes.MAGIC)){
				message = EPMessages.INDIRECT_DAMAGE_ENTITY_MAGIC.get();
			} else {
				message = damageSource.toString();
			}
			victim.sendMessage(EChat.of(EPMessages.PREFIX.get() + message
					.replaceAll("<player>", victim.getName())
					.replaceAll("<monster>", killer.getType().getName())));
		}
	}
	
	private void sendDeathMessage(BlockDamageSource damageSource, Player victim){
		DamageType type = damageSource.getType();
		this.plugin.getLogger().debug("Cause : BlockDamageSource; Type : " + type);
		String message = null;
		this.plugin.getEServer().broadcast("Type : " + type);
		if(type.equals(DamageTypes.CONTACT)){
			message = EPMessages.BLOCK_DAMAGE_CONTACT.get();
		} else if(type.equals(DamageTypes.FIRE)){
			message = EPMessages.BLOCK_DAMAGE_FIRE.get();
		} else {
			message = damageSource.toString();
		}
		victim.sendMessage(EChat.of(EPMessages.PREFIX.get() + message
				.replaceAll("<player>", victim.getName())));
	}
	
	private void sendDeathMessage(FallingBlockDamageSource damageSource, Player victim){
		DamageType type = damageSource.getType();
		this.plugin.getLogger().debug("Cause : FallingBlockDamageSource; Type : " + type);
		String message = null;
		if(type.equals(DamageTypes.CONTACT)){
			message = EPMessages.FALLING_DAMAGE_CONTACT.get();
		} else {
			message = damageSource.toString();
		}
		victim.sendMessage(EChat.of(EPMessages.PREFIX.get() + message
				.replaceAll("<player>", victim.getName())
				.replaceAll("<entity>", damageSource.getSource().getType().getTranslation().get())));
	}
	
	private void sendDeathMessage(EntityDamageSource damageSource, Player victim){
		DamageType type = damageSource.getType();
		this.plugin.getLogger().debug("Cause : EntityDamageSource; Type : " + type);
		String message = null;
		// Le killer est un joueur
		if(damageSource.getSource() instanceof Player){
			Player killer = (Player) damageSource.getSource();
			if(!victim.equals(killer)){
				if(type.equals(DamageTypes.ATTACK)){
					message = EPMessages.ENTITY_DAMAGE_PLAYER_ATTACK.get();
				} else if(type.equals(DamageTypes.EXPLOSIVE)){
					message = EPMessages.ENTITY_DAMAGE_PLAYER_EXPLOSIVE.get();
				} else if(type.equals(DamageTypes.FIRE)){
					message = EPMessages.ENTITY_DAMAGE_PLAYER_FIRE.get();
				} else if(type.equals(DamageTypes.MAGIC)){
					message = EPMessages.ENTITY_DAMAGE_PLAYER_MAGIC.get();
				} else {
					message = damageSource.toString();
				}
				this.plugin.getEServer().getBroadcastChannel().send(EChat.of(EPMessages.PREFIX.get() + message
						.replaceAll("<player>", victim.getName())
						.replaceAll("<killer>", killer.getName())));
			} else {
				if(type.equals(DamageTypes.EXPLOSIVE)){
					message = EPMessages.ENTITY_DAMAGE_SUICIDE_EXPLOSIVE.get();
				} else {
					message = damageSource.toString();
				}
				victim.sendMessage(EChat.of(EPMessages.PREFIX.get() + message
						.replaceAll("<player>", victim.getName())
						.replaceAll("<monster>", killer.getType().getName())));
			}
		// Le killer est une créature
		} else {
			Entity killer = damageSource.getSource();
			if(type.equals(DamageTypes.ATTACK)){
				message = EPMessages.ENTITY_DAMAGE_ENTITY_ATTACK.get();
			} else if(type.equals(DamageTypes.EXPLOSIVE)){
				message = EPMessages.ENTITY_DAMAGE_ENTITY_EXPLOSIVE.get();
			} else if(type.equals(DamageTypes.FIRE)){
				message = EPMessages.ENTITY_DAMAGE_ENTITY_FIRE.get();
			} else if(type.equals(DamageTypes.MAGIC)){
				message = EPMessages.ENTITY_DAMAGE_ENTITY_MAGIC.get();
			} else {
				message = damageSource.toString();
			}
			victim.sendMessage(EChat.of(EPMessages.PREFIX.get() + message
					.replaceAll("<player>", victim.getName())
					.replaceAll("<monster>", killer.getType().getName())));
		}
	}
	
	private void sendDeathMessage(DamageSource damageSource, Player victim){
		DamageType type = damageSource.getType();
		this.plugin.getLogger().debug("Cause : DamageSource; Type : " + type);
		String message = null;
		if(type.equals(DamageTypes.CONTACT)){
			message = EPMessages.DAMAGE_CONTACT.get();
		} else if(type.equals(DamageTypes.DROWN)){
			message = EPMessages.DAMAGE_DROWN.get();
		} else if(type.equals(DamageTypes.EXPLOSIVE)){
			message = EPMessages.DAMAGE_EXPLOSIVE.get();
		} else if(type.equals(DamageTypes.FALL)){
			message = EPMessages.DAMAGE_FALL.get();
		} else if(type.equals(DamageTypes.FIRE)){
			message = EPMessages.DAMAGE_FIRE.get();
		} else if(type.equals(DamageTypes.GENERIC)){
			message = EPMessages.DAMAGE_GENERIC.get();
		} else if(type.equals(DamageTypes.HUNGER)){
			message = EPMessages.DAMAGE_HUNGER.get();
		} else if(type.equals(DamageTypes.MAGIC)){
			message = EPMessages.DAMAGE_MAGIC.get();
		} else if(type.equals(DamageTypes.SUFFOCATE)){
			message = EPMessages.DAMAGE_SUFFOCATE.get();
		} else if(type.equals(DamageTypes.VOID)){
			message = EPMessages.DAMAGE_VOID.get();
		} else {
			message = damageSource.toString();
		}
		victim.sendMessage(EChat.of(EPMessages.PREFIX.get() + message
				.replaceAll("<player>", victim.getName())));
	}
}