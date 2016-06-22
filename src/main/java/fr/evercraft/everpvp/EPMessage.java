package fr.evercraft.everpvp;

import java.util.Arrays;
import java.util.List;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;

import com.google.common.base.Preconditions;

import fr.evercraft.everapi.plugin.EChat;
import fr.evercraft.everapi.plugin.file.EMessage;
import fr.evercraft.everapi.plugin.file.EnumMessage;

public class EPMessage extends EMessage {

	public EPMessage(EverPVP plugin) {
		super(plugin, EPMessages.values());
	}

	public enum EPMessages implements EnumMessage {
		PREFIX("prefix", "[&4Ever&6&lPVP&f] "),
		DESCRIPTION("description", "Commandes pour EverPVP"),
		ARROW_INFORMATION("arrow.information", "&6<player> &7est maintenant à <heal> &4❤&7."),
		
		UNTAG_DESCRIPTION("untag.description", "Retire un joueur du combat."),
		UNTAG_MESSAGE("untag.message", "&7Le joueur &6<player> &7n'est plus en combat."),
		UNTAG_ERROR("untag.error", "&cErreur : Le joueur <player> n'est pas en combat."),
		UNTAGALL_MESSAGE("untagall.message", "&7Tous les joueurs ne sont plus en combat"),
		
		// EntityDamageSource Player
		ENTITY_DAMAGE_PLAYER_ATTACK("entityDamage.player.Attack", "&6<player> &7s'est fait tuer par &6<killer>&7 avec <item>."),
		ENTITY_DAMAGE_PLAYER_EXPLOSIVE("entityDamage.player.explosive", "&6<player> &7est mort dans une explosion déclenchée par &6<killer>&7."),
		ENTITY_DAMAGE_PLAYER_FIRE("entityDamage.player.fire", "&6<player> &7a été réduit en cendre par &6<killer>&7."),
		ENTITY_DAMAGE_PLAYER_MAGIC("entityDamage.player.thorns", "&6<player> &7a été tué en blessant &6<killer>&7."),
		
		// EntityDamageSource Monstre
		ENTITY_DAMAGE_ENTITY_ATTACK("entityDamage.entity.attack", "&6<player> &7s'est fait tuer par &6un(e) <monster>&7."),
		ENTITY_DAMAGE_ENTITY_EXPLOSIVE("entityDamage.entity.explosive", "&6<player> &7s'est fait tuer dans l'explosion d'&6un(e) <monster>&7."),
		ENTITY_DAMAGE_ENTITY_MAGIC("entityDamage.entity.thorns", "&6<player> &7a été tué en blessant &6un(e)<monster>&7."),
		ENTITY_DAMAGE_ENTITY_FIRE("deathMessage.entity.fire", "&6<player> &7a été réduit en cendre par &6un(e) <monster>&7."),
		
		// EntityDamageSource Monstre
		ENTITY_DAMAGE_SUICIDE_EXPLOSIVE("entityDamage.suicide.explosive", "&6<player> &7s'est suicidé dans une explosion."),
		
		// DamageSource
		DAMAGE_CONTACT("damage.suffocate", "&6<player> &7a suffoqué dans un mur."),
		DAMAGE_FALL("damage.fall", "&6<player> &7a fait une terrible chute."),
		DAMAGE_DROWN("damage.drown", "&6<player> &7s'est noyé."),
		DAMAGE_EXPLOSIVE("damage.explosive", "&6<player> &7est mort dans une explosion."),
		DAMAGE_FIRE("damage.fire", "&6<player> &7est mort carbonisé."),
		DAMAGE_GENERIC("damage.generic", "&6<player> &7s'est suicidé."),
		DAMAGE_HUNGER("damage.hunger", "&6<player> &7est mort de faim."),
		DAMAGE_LIGHTNING("damage.lightning", "&6<player> &7est mort foudroyé."),
		DAMAGE_MAGIC("damage.magic", "&6<player> &7s'est fait tué par une potion."),
		DAMAGE_PROJECTILE("damage.projectile", "&6<player> &7s'est fait tué par une flèche."),
		DAMAGE_SUFFOCATE("damage.suffocate", "&6<player> &7a suffoqué dans un mur."),
		DAMAGE_VOID("damage.void", "&6<player> &7est tombé dans le néant."),
		
		//FallingDamage
		FALLING_DAMAGE_CONTACT("fallingDamage.contact", "&6<player> &7s'est fait écraser par un(e) <entity>."),
		
		//BlockDamage
		BLOCK_DAMAGE_CONTACT("blockDamage.contact", "&6<player> &7s'est fait piquer à mort."),
		BLOCK_DAMAGE_FIRE("blockDamage.fire", "&6<player> &7est mort carbonisé."),
		
		
		// Indirect Damag Player
		INDIRECT_DAMAGE_PLAYER_ATTACK("indirectDamage.player.attack", "&6<player> &7est mort d'une flèche tirée par &6<killer>&7."),
		INDIRECT_DAMAGE_PLAYER_MAGIC("indirectDamage.player.magic", "&6<player> &7a été tué par une potion lancé par &6<killer>&7."),
		
		// Indirect Damage Monstre
		INDIRECT_DAMAGE_ENTITY_ATTACK("indirectDamage.entity.attack", "&6<player> &7est mort d'une flèche tirée par un(e) &6<monster>&7."),
		INDIRECT_DAMAGE_ENTITY_MAGIC("indirectDamage.entity.magic", "&6<player> &7a été tué par une potion lancé par un(e) &6<monster>&7."),
		
		// Indirect Damage Monstre
		INDIRECT_DAMAGE_SUICIDE_ATTACK("indirectDamage.suicide.attack", "&6<player> &7s'est suicidé avec une flèche."),
		INDIRECT_DAMAGE_SUICIDE_MAGIC("indirectDamage.suicide.magic", "&6<player> &7s'est suicidé avec une potion de dégat.");
		
		private final String path;
	    private final Object french;
	    private final Object english;
	    private Object message;
	    
	    private EPMessages(final String path, final Object french) {   	
	    	this(path, french, french);
	    }
	    
	    private EPMessages(final String path, final Object french, final Object english) {
	    	Preconditions.checkNotNull(french, "Le message '" + this.name() + "' n'est pas définit");
	    	
	    	this.path = path;	    	
	    	this.french = french;
	    	this.english = english;
	    	this.message = french;
	    }

	    public String getName() {
			return this.name();
		}
	    
		public String getPath() {
			return this.path;
		}

		public Object getFrench() {
			return this.french;
		}

		public Object getEnglish() {
			return this.english;
		}
		
		public String get() {
			if(this.message instanceof String) {
				return (String) this.message;
			}
			return this.message.toString();
		}
			
		@SuppressWarnings("unchecked")
		public List<String> getList() {
			if(this.message instanceof List) {
				return (List<String>) this.message;
			}
			return Arrays.asList(this.message.toString());
		}
		
		public void set(Object message) {
			this.message = message;
		}

		public Text getText() {
			return EChat.of(this.get());
		}
		
		public TextColor getColor() {
			return EChat.getTextColor(this.get());
		}
	}
}