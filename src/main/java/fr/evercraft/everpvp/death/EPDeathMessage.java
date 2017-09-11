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
package fr.evercraft.everpvp.death;

import java.util.Optional;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
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
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import fr.evercraft.everapi.message.EMessageSender;
import fr.evercraft.everapi.plugin.EChat;
import fr.evercraft.everapi.server.player.EPlayer;
import fr.evercraft.everpvp.EverPVP;
import fr.evercraft.everpvp.EPMessage.EPMessages;

public class EPDeathMessage {
	private EverPVP plugin;
	
	public EPDeathMessage(EverPVP plugin) {
		this.plugin = plugin;
	}
	
	@Listener
	public void onPlayerDeath(DestructEntityEvent.Death event) {
		if (event.getTargetEntity() instanceof Player){
			event.clearMessage();
			EPlayer victim = this.plugin.getEServer().getEPlayer((Player)event.getTargetEntity());
			Optional<IndirectEntityDamageSource> optIndirectEntity = event.getCause().first(IndirectEntityDamageSource.class);
			if (optIndirectEntity.isPresent()){
				IndirectEntityDamageSource damageSource = optIndirectEntity.get();
				sendDeathMessage(damageSource, victim);
			} else {
				Optional<BlockDamageSource> optBlockDamage = event.getCause().first(BlockDamageSource.class);
				if (optBlockDamage.isPresent()){
					BlockDamageSource damageSource = optBlockDamage.get();
					sendDeathMessage(damageSource, victim);
				} else {
					Optional<FallingBlockDamageSource> optFallingBlock = event.getCause().first(FallingBlockDamageSource.class);
					if (optFallingBlock.isPresent()){
						FallingBlockDamageSource damageSource = optFallingBlock.get();
						sendDeathMessage(damageSource, victim);
					} else {
						Optional<EntityDamageSource> optEntityDamage = event.getCause().first(EntityDamageSource.class);
						if (optEntityDamage.isPresent()){
							EntityDamageSource damageSource = optEntityDamage.get();
							sendDeathMessage(damageSource, victim);
						} else {
							Optional<DamageSource> optDamage = event.getCause().first(DamageSource.class);
							if (optDamage.isPresent()){
								DamageSource damageSource = optDamage.get();
								sendDeathMessage(damageSource, victim);
							}
						}
					}
				}
			}
		}
	}
	
	private void sendDeathMessage(IndirectEntityDamageSource damageSource, EPlayer victim){
		DamageType type = damageSource.getType();
		this.plugin.getELogger().debug("Cause : IndirectEntityDamageSource; Type : " + type);
		EPMessages message = null;
		// Le killer est un joueur
		if (damageSource.getIndirectSource() instanceof Player){
			EPlayer killer = this.plugin.getEServer().getEPlayer((Player) damageSource.getIndirectSource());
			if (victim != killer){
				if (type.equals(DamageTypes.ATTACK)){
					message = EPMessages.INDIRECT_DAMAGE_PLAYER_ATTACK;	
				} else if (type.equals(DamageTypes.MAGIC)){
					message = EPMessages.INDIRECT_DAMAGE_PLAYER_MAGIC;
				}
				
				if (message != null) {
					this.message(message, victim, killer)
						.sendAll(this.plugin.getEServer().getOnlineEPlayers());
				}
			} else {
				if (type.equals(DamageTypes.ATTACK)){
					message = EPMessages.INDIRECT_DAMAGE_SUICIDE_ATTACK;	
				} else if (type.equals(DamageTypes.MAGIC)){
					message = EPMessages.INDIRECT_DAMAGE_SUICIDE_MAGIC;
				}
				
				if (message != null) {
					this.message(message, victim)
						.sendTo(victim);
				}
			}
		// Le killer est une créature
		} else {
			Entity monster = damageSource.getSource();
			if (type.equals(DamageTypes.ATTACK)){
				message = EPMessages.INDIRECT_DAMAGE_ENTITY_ATTACK;
			} else if (type.equals(DamageTypes.MAGIC)){
				message = EPMessages.INDIRECT_DAMAGE_ENTITY_MAGIC;
			}
			
			if (message != null) {
				this.message(message, victim, monster)
					.sendTo(victim);
			}
		}
	}
	
	private void sendDeathMessage(BlockDamageSource damageSource, EPlayer victim){
		DamageType type = damageSource.getType();
		this.plugin.getELogger().debug("Cause : BlockDamageSource; Type : " + type);
		EPMessages message = null;
		this.plugin.getEServer().broadcast("Type : " + type);
		if (type.equals(DamageTypes.CONTACT)){
			message = EPMessages.BLOCK_DAMAGE_CONTACT;
		} else if (type.equals(DamageTypes.FIRE)){
			message = EPMessages.BLOCK_DAMAGE_FIRE;
		}
		
		if (message != null) {
			this.message(message, victim)
				.sendTo(victim);
		} else {
			// Log
		}
	}
	
	private void sendDeathMessage(FallingBlockDamageSource damageSource, EPlayer victim){
		DamageType type = damageSource.getType();
		this.plugin.getELogger().debug("Cause : FallingBlockDamageSource; Type : " + type);
		EPMessages message = null;
		if (type.equals(DamageTypes.CONTACT)){
			message = EPMessages.FALLING_DAMAGE_CONTACT;
		}
		
		if (message != null) {
			this.message(message, victim)
				.sendTo(victim);
		} else {
			// Log
		}
	}
	
	private void sendDeathMessage(EntityDamageSource damageSource, EPlayer victim){
		DamageType type = damageSource.getType();
		this.plugin.getELogger().debug("Cause : EntityDamageSource; Type : " + type);
		EPMessages message = null;
		// Le killer est un joueur
		if (damageSource.getSource() instanceof Player){
			EPlayer killer = this.plugin.getEServer().getEPlayer((Player) damageSource.getSource());
			if (!victim.equals(killer)){
				if (type.equals(DamageTypes.ATTACK)){
					if (killer.getItemInMainHand().isPresent()){
						message = EPMessages.ENTITY_DAMAGE_PLAYER_ATTACK;
					} else {
						message = EPMessages.ENTITY_DAMAGE_PLAYER_ATTACK_NO_ITEM;
					}
				} else if (type.equals(DamageTypes.EXPLOSIVE)){
					message = EPMessages.ENTITY_DAMAGE_PLAYER_EXPLOSIVE;
				} else if (type.equals(DamageTypes.FIRE)){
					message = EPMessages.ENTITY_DAMAGE_PLAYER_FIRE;
				} else if (type.equals(DamageTypes.MAGIC)){
					message = EPMessages.ENTITY_DAMAGE_PLAYER_MAGIC;
				}

				if (message != null) {
					this.message(message, victim, killer)
						.sendAll(this.plugin.getEServer().getOnlineEPlayers());
				} else {
					// Log
				}
			} else {
				if (type.equals(DamageTypes.EXPLOSIVE)){
					message = EPMessages.ENTITY_DAMAGE_SUICIDE_EXPLOSIVE;
				}
				
				if (message != null) {
					this.message(message, victim)
						.sendTo(victim);
				} else {
					// Log
				}
			}
		// Le killer est une créature
		} else {
			Entity monster = damageSource.getSource();
			if (type.equals(DamageTypes.ATTACK)){
				message = EPMessages.ENTITY_DAMAGE_ENTITY_ATTACK;
			} else if (type.equals(DamageTypes.EXPLOSIVE)){
				message = EPMessages.ENTITY_DAMAGE_ENTITY_EXPLOSIVE;
			} else if (type.equals(DamageTypes.FIRE)){
				message = EPMessages.ENTITY_DAMAGE_ENTITY_FIRE;
			} else if (type.equals(DamageTypes.MAGIC)){
				message = EPMessages.ENTITY_DAMAGE_ENTITY_MAGIC;
			}

			if (message != null) {
				this.message(message, victim, monster)
					.sendTo(victim);
			}
		}
	}
	
	private void sendDeathMessage(DamageSource damageSource, EPlayer victim){
		DamageType type = damageSource.getType();
		this.plugin.getELogger().debug("Cause : DamageSource; Type : " + type);
		EPMessages message = null;
		if (type.equals(DamageTypes.CONTACT)){
			message = EPMessages.DAMAGE_CONTACT;
		} else if (type.equals(DamageTypes.DROWN)){
			message = EPMessages.DAMAGE_DROWN;
		} else if (type.equals(DamageTypes.EXPLOSIVE)){
			message = EPMessages.DAMAGE_EXPLOSIVE;
		} else if (type.equals(DamageTypes.FALL)){
			message = EPMessages.DAMAGE_FALL;
		} else if (type.equals(DamageTypes.FIRE)){
			message = EPMessages.DAMAGE_FIRE;
		} else if (type.equals(DamageTypes.GENERIC)){
			message = EPMessages.DAMAGE_GENERIC;
		} else if (type.equals(DamageTypes.HUNGER)){
			message = EPMessages.DAMAGE_HUNGER;
		} else if (type.equals(DamageTypes.MAGIC)){
			message = EPMessages.DAMAGE_MAGIC;
		} else if (type.equals(DamageTypes.SUFFOCATE)){
			message = EPMessages.DAMAGE_SUFFOCATE;
		} else if (type.equals(DamageTypes.VOID)){
			message = EPMessages.DAMAGE_VOID;
		}
		
		if (message != null) {
			this.message(message, victim)
				.sendTo(victim);
		} else {
			this.plugin.getELogger().debug("Erreur : Cause : DamageSource; Type : " + type);
		}
	}
	
	private EMessageSender message(EPMessages message, EPlayer victim){
		return message.sender()
				.replace("{victim}", getButtonVictim(victim));
	}
	
	private EMessageSender message(EPMessages message, EPlayer victim, EPlayer killer){
		if (killer.getItemInHand(HandTypes.MAIN_HAND).isPresent()){
			return message.sender()
					.replace("{victim}", getButtonVictim(victim))
					.replace("{killer}", getButtonKiller(killer))
					.replace("{item}", EChat.getButtomItem(killer.getItemInHand(HandTypes.MAIN_HAND).get(), EPMessages.ITEM_COLOR.getColor()));
		} else {
			return message.sender()
					.replace("{victim}", getButtonVictim(victim))
					.replace("{killer}", getButtonKiller(killer));
		}
	}
	
	private EMessageSender message(EPMessages message, EPlayer victim, Entity monster){
		return message.sender()
				.replace("{victim}", getButtonVictim(victim))
				.replace("{monster}", getButtonMonster(monster));
	}
	
	private Text getButtonKiller(final EPlayer killer){
		return EPMessages.KILLER_NAME.getFormat().toText("{killer}", killer.getDisplayName()).toBuilder()
				.onHover(TextActions.showText(EPMessages.KILLER_DESCRIPTION_HOVER.getFormat().toText(killer.getReplaces())))
				.onClick(TextActions.suggestCommand("/msg " + killer.getDisplayName()))
				.build();
	}
	
	private Text getButtonVictim(final EPlayer victim){
		return EPMessages.VICTIM_NAME.getFormat().toText("{victim}", victim.getDisplayName()).toBuilder()
				.onHover(TextActions.showText(EPMessages.VICTIM_DESCRIPTION_HOVER.getFormat().toText(victim.getReplaces())))
				.onClick(TextActions.suggestCommand("/msg " + victim.getDisplayName()))
				.build();
	}
	
	private Text getButtonMonster(final Entity monster){
		return EPMessages.MONSTER_NAME.getFormat().toText("{monster}", monster.getType().getName()).toBuilder()
				.onHover(TextActions.showText(EPMessages.MONSTER_NAME.getFormat()
						.toText("{heal}", String.valueOf(monster.get(Keys.HEALTH)))))
				.build();
	}

}
