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

import fr.evercraft.everapi.plugin.EChat;
import fr.evercraft.everapi.server.player.EPlayer;
import fr.evercraft.everapi.text.ETextBuilder;
import fr.evercraft.everpvp.EverPVP;
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
			Optional<EPlayer> optVictim = this.plugin.getEServer().getEPlayer((Player)event.getTargetEntity());
			if(optVictim.isPresent()){
				EPlayer victim = optVictim.get();
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
	}
	
	private void sendDeathMessage(IndirectEntityDamageSource damageSource, EPlayer victim){
		DamageType type = damageSource.getType();
		this.plugin.getLogger().debug("Cause : IndirectEntityDamageSource; Type : " + type);
		String message = null;
		// Le killer est un joueur
		if(damageSource.getIndirectSource() instanceof Player){
			Optional<EPlayer> optKiller = this.plugin.getEServer().getEPlayer((Player)damageSource.getIndirectSource());
			if(optKiller.isPresent()){
				EPlayer killer = optKiller.get();
				if(victim != killer){
					if(type.equals(DamageTypes.ATTACK)){
						message = EPMessages.INDIRECT_DAMAGE_PLAYER_ATTACK.get();	
					} else if(type.equals(DamageTypes.MAGIC)){
						message = EPMessages.INDIRECT_DAMAGE_PLAYER_MAGIC.get();
					}
					this.plugin.getEServer().getBroadcastChannel().send(
							message(message, victim, killer));
				} else {
					if(type.equals(DamageTypes.ATTACK)){
						message = EPMessages.INDIRECT_DAMAGE_SUICIDE_ATTACK.get();	
					} else if(type.equals(DamageTypes.MAGIC)){
						message = EPMessages.INDIRECT_DAMAGE_SUICIDE_MAGIC.get();
					}
					victim.sendMessage(
							message(message, victim));

				}
			}
		// Le killer est une créature
		} else {
			Entity monster = damageSource.getSource();
			if(type.equals(DamageTypes.ATTACK)){
				message = EPMessages.INDIRECT_DAMAGE_ENTITY_ATTACK.get();
			} else if(type.equals(DamageTypes.MAGIC)){
				message = EPMessages.INDIRECT_DAMAGE_ENTITY_MAGIC.get();
			}
			victim.sendMessage(
					message(message, victim, monster));
		}
	}
	
	private void sendDeathMessage(BlockDamageSource damageSource, EPlayer victim){
		DamageType type = damageSource.getType();
		this.plugin.getLogger().debug("Cause : BlockDamageSource; Type : " + type);
		String message = null;
		this.plugin.getEServer().broadcast("Type : " + type);
		if(type.equals(DamageTypes.CONTACT)){
			message = EPMessages.BLOCK_DAMAGE_CONTACT.get();
		} else if(type.equals(DamageTypes.FIRE)){
			message = EPMessages.BLOCK_DAMAGE_FIRE.get();
		}
		victim.sendMessage(
				message(message, victim));
	}
	
	private void sendDeathMessage(FallingBlockDamageSource damageSource, EPlayer victim){
		DamageType type = damageSource.getType();
		this.plugin.getLogger().debug("Cause : FallingBlockDamageSource; Type : " + type);
		String message = null;
		if(type.equals(DamageTypes.CONTACT)){
			message = EPMessages.FALLING_DAMAGE_CONTACT.get();
		}
		victim.sendMessage(
				message(message, victim));
	}
	
	private void sendDeathMessage(EntityDamageSource damageSource, EPlayer victim){
		DamageType type = damageSource.getType();
		this.plugin.getLogger().debug("Cause : EntityDamageSource; Type : " + type);
		String message = null;
		// Le killer est un joueur
		if(damageSource.getSource() instanceof Player){
			Optional<EPlayer> optKiller = this.plugin.getEServer().getEPlayer((Player)damageSource.getSource());
			if(optKiller.isPresent()){
				EPlayer killer = optKiller.get();
				if(!victim.equals(killer)){
					if(type.equals(DamageTypes.ATTACK)){
						message = EPMessages.ENTITY_DAMAGE_PLAYER_ATTACK.get();
					} else if(type.equals(DamageTypes.EXPLOSIVE)){
						message = EPMessages.ENTITY_DAMAGE_PLAYER_EXPLOSIVE.get();
					} else if(type.equals(DamageTypes.FIRE)){
						message = EPMessages.ENTITY_DAMAGE_PLAYER_FIRE.get();
					} else if(type.equals(DamageTypes.MAGIC)){
						message = EPMessages.ENTITY_DAMAGE_PLAYER_MAGIC.get();
					}
					this.plugin.getEServer().getBroadcastChannel().send(
							message(message, victim, killer));
				} else {
					if(type.equals(DamageTypes.EXPLOSIVE)){
						message = EPMessages.ENTITY_DAMAGE_SUICIDE_EXPLOSIVE.get();
					}
					victim.sendMessage(
							message(message, victim));
				}
			}
		// Le killer est une créature
		} else {
			Entity monster = damageSource.getSource();
			if(type.equals(DamageTypes.ATTACK)){
				message = EPMessages.ENTITY_DAMAGE_ENTITY_ATTACK.get();
			} else if(type.equals(DamageTypes.EXPLOSIVE)){
				message = EPMessages.ENTITY_DAMAGE_ENTITY_EXPLOSIVE.get();
			} else if(type.equals(DamageTypes.FIRE)){
				message = EPMessages.ENTITY_DAMAGE_ENTITY_FIRE.get();
			} else if(type.equals(DamageTypes.MAGIC)){
				message = EPMessages.ENTITY_DAMAGE_ENTITY_MAGIC.get();
			}
			victim.sendMessage(
					message(message, victim, monster));
		}
	}
	
	private void sendDeathMessage(DamageSource damageSource, EPlayer victim){
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
		}
		victim.sendMessage(
				message(message, victim));
	}
	
	private Text message(String message, EPlayer victim){
		return ETextBuilder.toBuilder(EPMessages.PREFIX.get())
		.append(message)
				.replace("<victim>", getButtonVictim(victim))
			.build();
	}
	
	private Text message(String message, EPlayer victim, EPlayer killer){
		if(killer.getItemInHand(HandTypes.MAIN_HAND).isPresent()){
			return ETextBuilder.toBuilder(EPMessages.PREFIX.get())
				.append(message)
					.replace("<victim>", getButtonVictim(victim))
					.replace("<killer>", getButtonKiller(killer))
					.replace("<item>", EChat.getButtomItem(killer.getItemInHand(HandTypes.MAIN_HAND).get(), EChat.getTextColor(EPMessages.ITEM_COLOR.get())))
				.build();
		} else {
			return ETextBuilder.toBuilder(EPMessages.PREFIX.get())
				.append(message)
					.replace("<victim>", getButtonVictim(victim))
					.replace("<killer>", getButtonKiller(killer))
				.build();
		}
	}
	
	private Text message(String message, EPlayer victim, Entity monster){
		return ETextBuilder.toBuilder(EPMessages.PREFIX.get())
		.append(message)
				.replace("<victim>", getButtonVictim(victim))
				.replace("<monster>", getButtonMonster(monster))
			.build();
	}
	
	private Text getButtonKiller(final EPlayer killer){
		return EChat.of(EPMessages.KILLER_NAME.get().replaceAll("<killer>", killer.getDisplayName())).toBuilder()
				.onHover(TextActions.showText(killer.replaceVariable(EPMessages.KILLER_DESCRIPTION_HOVER.get())))
				.build();
	}
	
	private Text getButtonVictim(final EPlayer victim){
		return EChat.of(EPMessages.VICTIM_NAME.get().replaceAll("<victim>", victim.getDisplayName())).toBuilder()
				.onHover(TextActions.showText(victim.replaceVariable(EPMessages.VICTIM_DESCRIPTION_HOVER.get())))
				.build();
	}
	
	private Text getButtonMonster(final Entity monster){
		return EChat.of(EPMessages.MONSTER_NAME.get().replaceAll("<monster>", monster.getType().getName())).toBuilder()
				.onHover(TextActions.showText(EChat.of(EPMessages.MONSTER_NAME.get()
						.replaceAll("<heal>", String.valueOf(monster.get(Keys.HEALTH))))))
				.build();
	}

}