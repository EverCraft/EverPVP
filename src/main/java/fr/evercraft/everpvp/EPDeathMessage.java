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
			String message = null;
			Optional<IndirectEntityDamageSource> optIndirectEntity = event.getCause().first(IndirectEntityDamageSource.class);
			if(optIndirectEntity.isPresent()){
				this.plugin.getEServer().broadcast("Cause : IndirectEntityDamageSource");
				IndirectEntityDamageSource damageSource = optIndirectEntity.get();
				DamageType type = damageSource.getType();
				this.plugin.getEServer().broadcast("Type : " + type);
				this.plugin.getEServer().broadcast("Player : " + damageSource.getIndirectSource());
				// Le killer est un joueur
				if(damageSource.getIndirectSource() instanceof Player){
					Player killer = (Player) damageSource.getIndirectSource();
					if(type.equals(DamageTypes.ATTACK)){
						message = EPMessages.INDIRECT_DAMAGE_PLAYER_ATTACK.get();
					} else if(type.equals(DamageTypes.MAGIC)){
						message = EPMessages.INDIRECT_DAMAGE_PLAYER_MAGIC.get();
					}
					this.plugin.getEServer().getBroadcastChannel().send(EChat.of(EPMessages.PREFIX.get() + message
							.replaceAll("<player>", victim.getName())
							.replaceAll("<killer>", killer.getName())));
				// Le killer est une créature
				} else {
					Entity killer = damageSource.getSource();
					if(type.equals(DamageTypes.ATTACK)){
						message = EPMessages.INDIRECT_DAMAGE_ENTITY_ATTACK.get();
					} else if(type.equals(DamageTypes.MAGIC)){
						message = EPMessages.INDIRECT_DAMAGE_ENTITY_MAGIC.get();
					}
					victim.sendMessage(EChat.of(EPMessages.PREFIX.get() + message
							.replaceAll("<player>", victim.getName())
							.replaceAll("<monster>", killer.getType().getName())));
				}
			} else {
				Optional<BlockDamageSource> optBlockDamage = event.getCause().first(BlockDamageSource.class);
				if(optBlockDamage.isPresent()){
					this.plugin.getEServer().broadcast("Cause : BlockDamageSource");
					BlockDamageSource damageSource = optBlockDamage.get();
					DamageType type = damageSource.getType();
					this.plugin.getEServer().broadcast("Type : " + type);
					if(type.equals(DamageTypes.CONTACT)){
						message = EPMessages.BLOCK_DAMAGE_CONTACT.get();
					} else if(type.equals(DamageTypes.FIRE)){
						message = EPMessages.BLOCK_DAMAGE_FIRE.get();
					} else {
						message = event.getCause().toString();
					}
				} else {
					Optional<FallingBlockDamageSource> optFallingBlock = event.getCause().first(FallingBlockDamageSource.class);
					if(optFallingBlock.isPresent()){
						this.plugin.getEServer().broadcast("Cause : FallingBlockDamageSource");
						FallingBlockDamageSource damageSource = optFallingBlock.get();
						DamageType type = damageSource.getType();
						this.plugin.getEServer().broadcast("Type : " + type);
						if(type.equals(DamageTypes.CONTACT)){
							message = EPMessages.FALLING_DAMAGE_CONTACT.get();
						} else {
							message = event.getCause().toString();
						}
					} else {
						Optional<EntityDamageSource> optEntityDamage = event.getCause().first(EntityDamageSource.class);
						if(optEntityDamage.isPresent()){
							this.plugin.getEServer().broadcast("Cause : EntityDamageSource");
							EntityDamageSource damageSource = optEntityDamage.get();
							DamageType type = damageSource.getType();
							this.plugin.getEServer().broadcast("Type : " + type);
							// Le killer est un joueur
							if(damageSource.getSource() instanceof Player){
								Player killer = (Player) damageSource.getSource();
								if(type.equals(DamageTypes.ATTACK)){
									message = EPMessages.ENTITY_DAMAGE_PLAYER_ATTACK.get();
								} else if(type.equals(DamageTypes.EXPLOSIVE)){
									message = EPMessages.ENTITY_DAMAGE_PLAYER_EXPLOSIVE.get();
								} else if(type.equals(DamageTypes.FIRE)){
									message = EPMessages.ENTITY_DAMAGE_PLAYER_FIRE.get();
								} else if(type.equals(DamageTypes.MAGIC)){
									message = EPMessages.ENTITY_DAMAGE_PLAYER_MAGIC.get();
								} else {
									message = event.getCause().toString();
								}
								this.plugin.getEServer().getBroadcastChannel().send(EChat.of(EPMessages.PREFIX.get() + message
										.replaceAll("<player>", victim.getName())
										.replaceAll("<killer>", killer.getName())));
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
									message = event.getCause().toString();
								}
								victim.sendMessage(EChat.of(EPMessages.PREFIX.get() + message
										.replaceAll("<player>", victim.getName())
										.replaceAll("<monster>", killer.getType().getName())));
							}
						} else {
							Optional<DamageSource> optDamage = event.getCause().first(DamageSource.class);
							if(optDamage.isPresent()){
								this.plugin.getEServer().broadcast("Cause : DamageSource");
								DamageSource damageSource = optEntityDamage.get();
								DamageType type = damageSource.getType();
								this.plugin.getEServer().broadcast("Type : " + type);
								if(type.equals(DamageTypes.DROWN)){
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
									message = event.getCause().toString();
								}
								victim.sendMessage(EChat.of(EPMessages.PREFIX.get() + message
										.replaceAll("<player>", victim.getName())));
							}
						}
					}
				}
			}
		}
	}
}