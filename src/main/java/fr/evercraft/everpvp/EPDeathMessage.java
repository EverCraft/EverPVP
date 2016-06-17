package fr.evercraft.everpvp;

import java.util.Optional;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.DamageType;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DestructEntityEvent;

import fr.evercraft.everapi.plugin.EChat;
import fr.evercraft.everpvp.EPMessage.EPMessages;

public class EPDeathMessage {
	private EverPVP plugin;
	
	public EPDeathMessage(EverPVP plugin) {
		this.plugin = plugin;
	}
	
	@Listener
	public void onPlayerDeath(DestructEntityEvent event) {
		if(event.getTargetEntity() instanceof Player){
			Optional<DamageSource> optDamageSource = event.getCause().first(DamageSource.class);
			Player victim = (Player) event.getTargetEntity();
			if (optDamageSource.isPresent()) {
				DamageSource damageSource = optDamageSource.get();
				DamageType type = damageSource.getType();
				// event.clearMessage();
				String message;
				if (damageSource instanceof EntityDamageSource){
					EntityDamageSource entityDamage = (EntityDamageSource) optDamageSource.get();
					// Le tueur est un joueur
					Entity source = entityDamage.getSource();
					Projectile projectile = null;
					if(source instanceof Projectile){
						projectile = (Projectile) source;
						if(((Projectile)source).getShooter() instanceof Entity){
							source = (Entity) ((Projectile)source).getShooter();
						} else {
							source = null;
						}
					}
					if(source instanceof Player){
						Player killer = (Player) source;
						this.plugin.getEServer().broadcast("DamageType : " + type.getName());
						this.plugin.getEServer().broadcast("Source : " + killer.getName());
						this.plugin.getEServer().broadcast("Projectile : " + projectile.getType().getName());
						// Si le joueur est tué avec un projectile
						if(projectile != null){
							if(type.equals(DamageTypes.ATTACK)){
								
							} else if(type.equals(DamageTypes.MAGIC)){
								
							}
						}
						if(type.equals(DamageTypes.ATTACK)){
							message = EPMessages.DEATHMESSAGE_PLAYER_ATTACK.get();
						} else if(type.equals(DamageTypes.EXPLOSIVE)){
							message = EPMessages.DEATHMESSAGE_PLAYER_EXPLOSIVE.get();
						} else if(type.equals(DamageTypes.FALL)){
							message = EPMessages.DEATHMESSAGE_PLAYER_FALL.get();
						} else if(type.equals(DamageTypes.FIRE)){
							message = EPMessages.DEATHMESSAGE_PLAYER_FIRE.get();
						} else if(type.equals(DamageTypes.MAGIC)){
							if(projectile.getType().equals(EntityTypes.SPLASH_POTION)){
								this.plugin.getEServer().broadcast("SPLASH_POTION");
								message = EPMessages.DEATHMESSAGE_PLAYER_MAGIC.get();
							} else {
								this.plugin.getEServer().broadcast("test");
								message = EPMessages.DEATHMESSAGE_PLAYER_MAGIC.get();
							}
						} else if(type.equals(DamageTypes.PROJECTILE)){
							message = EPMessages.DEATHMESSAGE_PLAYER_PROJECTILE.get();
						} else {
							message = ("Erreur " + type.getName());
						}
						this.plugin.getEServer().getBroadcastChannel().send(EChat.of(EPMessages.PREFIX.get() + message
								.replaceAll("<player>", victim.getName())
								.replaceAll("<killer>", killer.getName())));
					// Le tueur est un monstre
					} else {
						this.plugin.getEServer().broadcast("DamageType : " + type.getName());
						this.plugin.getEServer().broadcast("Source : " + source.getType().getName());
						this.plugin.getEServer().broadcast("Projectile : " + projectile);
						if(type.equals(DamageTypes.ATTACK)){
							message = EPMessages.DEATHMESSAGE_ENTITY_ATTACK.get();
						} else if(type.equals(DamageTypes.CONTACT)){
							message = EPMessages.DEATHMESSAGE_ENTITY_CONTACT.get();
						} else if(type.equals(DamageTypes.EXPLOSIVE)){
							message = EPMessages.DEATHMESSAGE_ENTITY_EXPLOSIVE.get();
						} else if(type.equals(DamageTypes.FALL)){
							message = EPMessages.DEATHMESSAGE_ENTITY_FALL.get();
						} else if(type.equals(DamageTypes.FIRE)){
							message = EPMessages.DEATHMESSAGE_ENTITY_FIRE.get();
						} else if(type.equals(DamageTypes.MAGIC)){
							message = EPMessages.DEATHMESSAGE_ENTITY_MAGIC.get();
						} else if(type.equals(DamageTypes.PROJECTILE)){
							message = EPMessages.DEATHMESSAGE_ENTITY_PROJECTILE.get();
						} else {
							message = ("Erreur " + type.getName());
						}
						victim.sendMessage(EChat.of(EPMessages.PREFIX.get() + message
								.replaceAll("<player>", victim.getName())
								.replaceAll("<monster>", source.getType().getName())));
					}
				// Le tueur n'est pas une entité
				} else {
					this.plugin.getEServer().broadcast("DamageType : " + type.getName() + "Source : NULL");
					if(type.equals(DamageTypes.CONTACT)){
						message = EPMessages.DEATHMESSAGE_NATURALLY_CONTACT.get();
					} else if(type.equals(DamageTypes.DROWN)){
						message = EPMessages.DEATHMESSAGE_NATURALLY_DROWN.get();
					} else if(type.equals(DamageTypes.EXPLOSIVE)){
						message = EPMessages.DEATHMESSAGE_NATURALLY_EXPLOSIVE.get();
					} else if(type.equals(DamageTypes.FALL)){
						message = EPMessages.DEATHMESSAGE_NATURALLY_FALL.get();
					} else if(type.equals(DamageTypes.FIRE)){
						message = EPMessages.DEATHMESSAGE_NATURALLY_FIRE.get();
					} else if(type.equals(DamageTypes.GENERIC)){
						message = EPMessages.DEATHMESSAGE_NATURALLY_GENERIC.get();
					} else if(type.equals(DamageTypes.HUNGER)){
						message = EPMessages.DEATHMESSAGE_NATURALLY_HUNGER.get();
					} else if(type.equals(DamageTypes.MAGIC)){
						message = EPMessages.DEATHMESSAGE_NATURALLY_MAGIC.get();
					} else if(type.equals(DamageTypes.PROJECTILE)){
						message = EPMessages.DEATHMESSAGE_NATURALLY_PROJECTILE.get();
					} else if(type.equals(DamageTypes.SUFFOCATE)){
						message = EPMessages.DEATHMESSAGE_NATURALLY_SUFFOCATE.get();
					} else if(type.equals(DamageTypes.VOID)){
						message = EPMessages.DEATHMESSAGE_NATURALLY_VOID.get();
					} else {
						message = ("Erreur " + type.getName());
					}
					victim.sendMessage(EChat.of(EPMessages.PREFIX.get() + message
							.replaceAll("<player>", victim.getName())));
				}
        	}
		}
	}
}
