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
		DESCRIPTION("description", ""),
		ARROW_INFORMATION("arrow.information", "&6<player> &7est maintenant à <heal> &4❤&7."),
		UNTAG_MESSAGE("untag.message", "&7Le joueur &6<player> &7n'est plus en combat."),
		UNTAG_ERROR("untag.error", "&cErreur : Le joueur <player> n'est pas en combat."),
		UNTAGALL_MESSAGE("untagall.message", "&7Tous les joueurs ne sont plus en combat"),
		
		// Custom Death Message Player
		DEATHMESSAGE_PLAYER_ATTACK("deathMessage.player.Attack", "&6<player> &7s'est fait tuer par &6<killer>&7 avec <item>."),
		DEATHMESSAGE_PLAYER_DECAPITATION("deathMessage.player.decapitation", "&6<player> &7a été décapité par &6<killer>&7 avec <item>."),
		DEATHMESSAGE_PLAYER_EXPLOSIVE("deathMessage.player.explosive", "&6<player> &7est mort dans une explosion déclenchée par &6<killer>&7."),
		DEATHMESSAGE_PLAYER_FALL("deathMessage.player.fall", "&6<player> a fait une terrible chute&7 a cause de &6<killer>."),
		DEATHMESSAGE_PLAYER_FIRE("deathMessage.player.fire", "&6<player> &7a été réduit en cendre par &6<killer>&7."),
		DEATHMESSAGE_PLAYER_MAGIC("deathMessage.player.magic", "&6<player> &7a été tué par une potion de dégat lancé par &6<killer>&7."),
		DEATHMESSAGE_PLAYER_PROJECTILE("deathMessage.player.projectile", "&6<player> &7est mort d'une fléche tiré par &6<killer>&7."),
		DEATHMESSAGE_PLAYER_THORNS("deathMessage.player.thorns", "&6<player> &7a été tué en blessant &6<killer>&7."),
		
		// Custom Death Message Entity
		DEATHMESSAGE_ENTITY_ATTACK("deathMessage.entity.attack", "&6<player> &7s'est fait tuer par &6un(e) <monster>&7."),
		DEATHMESSAGE_ENTITY_CONTACT("deathMessage.entity.contact", "&6<player> &7s'est fait éclaser par un(e) <monster>."),
		DEATHMESSAGE_ENTITY_EXPLOSIVE("deathMessage.entity.explosive", "&6<player> &7s'est fait tuer dans l'explosion d'&6un(e) <monster>&7."),
		DEATHMESSAGE_ENTITY_FALL("deathMessage.entity.fall", "&6un(e) <monster> &7a poussé &6<player> et &6<player> a fait une terrible chute&7."),
		DEATHMESSAGE_ENTITY_MAGIC("deathMessage.entity.magic", "&6<player> &7a été tué par une potion de dégat lancé par &6un(e) <monster>&7."),
		DEATHMESSAGE_ENTITY_FIRE("deathMessage.entity.fire", "&6<player> &7a été réduit en cendre par &6un(e) <monster>&7."),
		DEATHMESSAGE_ENTITY_THORNS("deathMessage.entity.thorns", "&6<player> &7s'est fait tuer par &6un(e) <monster>&7."),
		DEATHMESSAGE_ENTITY_PROJECTILE("deathMessage.entity.projectile", "&6<player> &7est mort d'une flèche tiré par &6un(e) <monster>&7."),
		DEATHMESSAGE_ENTITY_WITHER("deathMessage.entity.wither", "&6<player> &7est mort atrophié par &6un(e) <monster>&7."),
		
		// Custom Death Message Naturally
		DEATHMESSAGE_NATURALLY_CONTACT("deathMessage.naturally.contact", "&6<player> &7s'est fait piquer à mort."),
		DEATHMESSAGE_NATURALLY_DROWN("deathMessage.naturally.drown", "&6<player> &7s'est noyé."),
		DEATHMESSAGE_NATURALLY_EXPLOSIVE("deathMessage.naturally.explosive", "&6<player> &7est mort dans une explosion."),
		DEATHMESSAGE_NATURALLY_FALL("deathMessage.naturally.fall", "&6<player> &7a fait une chute mortelle."),
		DEATHMESSAGE_NATURALLY_FIRE("deathMessage.naturally.fire", "&6<player> &7est mort carbonisé."),
		DEATHMESSAGE_NATURALLY_GENERIC("deathMessage.naturally.generic", "&6<player> &7s'est suicidé."),
		DEATHMESSAGE_NATURALLY_HUNGER("deathMessage.naturally.hunger", "&6<player> &7est mort de faim."),
		DEATHMESSAGE_NATURALLY_LIGHTNING("deathMessage.naturally.lightning", "&6<player> &7est mort foudroyé."),
		DEATHMESSAGE_NATURALLY_MAGIC("deathMessage.naturally.magic", "&6<player> &7s'est fait tué par une potion de dégat II."),
		DEATHMESSAGE_NATURALLY_PROJECTILE("deathMessage.naturally.projectile", "&6<player> &7s'est fait tué par une flèche."),
		DEATHMESSAGE_NATURALLY_SUFFOCATE("deathMessage.naturally.suffocate", "&6<player> &7est mort étouffé."),
		DEATHMESSAGE_NATURALLY_VOID("deathMessage.naturally.void", "&6<player> &7est tombé dans le néant.");
		
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
