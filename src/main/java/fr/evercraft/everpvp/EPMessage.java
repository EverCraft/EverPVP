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

import com.google.common.base.Preconditions;

import fr.evercraft.everapi.message.EMessageBuilder;
import fr.evercraft.everapi.message.EMessageFormat;
import fr.evercraft.everapi.message.format.EFormatString;
import fr.evercraft.everapi.plugin.file.EMessage;
import fr.evercraft.everapi.plugin.file.EnumMessage;

public class EPMessage extends EMessage<EverPVP> {

	public EPMessage(EverPVP plugin) {
		super(plugin, EPMessages.values());
	}

	public enum EPMessages implements EnumMessage {
		PREFIX("[&4Ever&6&lPVP&f] "),
		DESCRIPTION("Commandes pour EverPVP"),
		ARROW_INFORMATION("&6{player} &7est maintenant à {heal} &4❤&7."),
		ITEM_COLOR("&b"),
		KILLER_DESCRIPTION_HOVER(
				"&7Ping : &6{PING} ms[RT]"
				+ "&7Point(s) de vie : &6{HEALTH}/{MAX_HEALTH}[RT]"
				+ "&7Victime(s) : &6{KILLS}[RT]"
				+ "&7Mort(s) : &6{DEATHS}[RT]"
				+ "&7Série de victime : &6{KILLSTREAKS}"),
		KILLER_NAME("&6{killer}"),
		VICTIM_DESCRIPTION_HOVER(
				"&7Ping : &6{PING} ms[RT]"
				+ "&7Victime(s) : &6{KILLS}[RT]"
				+ "&7Mort(s) : &6{DEATHS}"),
		VICTIM_NAME("&6{victim}"),
		MONSTER_DESCRIPTION_HOVER(
				"&7Coeur(s) restant(s) : &6{HEAL}"),
		MONSTER_NAME("&6{monster}"),
		
		// Untag
		UNTAG_DESCRIPTION("Retire un joueur du combat."),
		UNTAG_MESSAGE("&7Le joueur &6{player} &7n'est plus en combat."),
		UNTAG_ERROR("&cErreur : Le joueur {player} n'est pas en combat."),
		UNTAGALL_MESSAGE("&7Tous les joueurs ne sont plus en combat"),

		// EntityDamageSource Player
		ENTITY_DAMAGE_PLAYER_DISCONNECT("&6{victim} &7s'est fait tuer en déconnectant en combat."),
		ENTITY_DAMAGE_PLAYER_ATTACK("&6{victim} &7s'est fait tuer par &6{killer}&7 avec un(e) &b[{item}&b]&7."),
		ENTITY_DAMAGE_PLAYER_ATTACK_NO_ITEM("&6{victim} &7s'est fait tuer par &6{killer}&7."),
		ENTITY_DAMAGE_PLAYER_EXPLOSIVE("&6{victim} &7est mort dans une explosion déclenchée par &6{killer}&7."),
		ENTITY_DAMAGE_PLAYER_FIRE("&6{victim} &7a été réduit en cendre par &6{killer}&7."),
		ENTITY_DAMAGE_PLAYER_MAGIC("&6{victim} &7a été tué en blessant &6{killer}&7."),
		
		// EntityDamageSource Monstre
		ENTITY_DAMAGE_ENTITY_ATTACK("&6{victim} &7s'est fait tuer par &6un(e) {monster}&7."),
		ENTITY_DAMAGE_ENTITY_EXPLOSIVE("&6{victim} &7s'est fait tuer dans l'explosion d'&6un(e) {monster}&7."),
		ENTITY_DAMAGE_ENTITY_MAGIC("&6{victim} &7a été tué en blessant &6un(e){monster}&7."),
		ENTITY_DAMAGE_ENTITY_FIRE("&6{victim} &7a été réduit en cendre par &6un(e) {monster}&7."),
		
		// EntityDamageSource Monstre
		ENTITY_DAMAGE_SUICIDE_EXPLOSIVE("&6{victim} &7s'est suicidé dans une explosion."),
		
		// DamageSource
		DAMAGE_CONTACT("&6{victim} &7a suffoqué dans un mur."),
		DAMAGE_FALL("&6{victim} &7a fait une terrible chute."),
		DAMAGE_DROWN("&6{victim} &7s'est noyé."),
		DAMAGE_EXPLOSIVE("&6{victim} &7est mort dans une explosion."),
		DAMAGE_FIRE("&6{victim} &7est mort carbonisé."),
		DAMAGE_GENERIC("&6{victim} &7s'est suicidé."),
		DAMAGE_HUNGER("&6{victim} &7est mort de faim."),
		DAMAGE_LIGHTNING("&6{victim} &7est mort foudroyé."),
		DAMAGE_MAGIC("&6{victim} &7s'est fait tué par une potion."),
		DAMAGE_MAGMA("&6{victim} &7a découvert que le sol était en lave."),
		DAMAGE_PROJECTILE("&6{victim} &7s'est fait tué par une flèche."),
		DAMAGE_SUFFOCATE("&6{victim} &7a suffoqué dans un mur."),
		DAMAGE_VOID("&6{victim} &7est tombé dans le néant."),
		
		//FallingDamage
		FALLING_DAMAGE_CONTACT("&6{victim} &7s'est fait écraser par une enclume."),
		
		//BlockDamage
		BLOCK_DAMAGE_CONTACT("&6{victim} &7s'est fait piquer à mort."),
		BLOCK_DAMAGE_FIRE("&6{victim} &7est mort carbonisé."),
			
		// Indirect Damag Player
		INDIRECT_DAMAGE_PLAYER_ATTACK("&6{victim} &7est mort d'une flèche tirée par &6{killer}&7 avec un(e) &b[{item}&b]&7."),
		INDIRECT_DAMAGE_PLAYER_MAGIC("&6{victim} &7a été tué par une potion lancé par &6{killer}&7."),
		
		// Indirect Damage Monstre
		INDIRECT_DAMAGE_ENTITY_ATTACK("&6{victim} &7est mort d'une flèche tirée par un(e) &6{monster}&7."),
		INDIRECT_DAMAGE_ENTITY_MAGIC("&6{victim} &7a été tué par une potion lancé par un(e) &6{monster}&7."),
		
		// Indirect Damage Monstre
		INDIRECT_DAMAGE_SUICIDE_ATTACK("&6{victim} &7s'est suicidé avec une flèche."),
		INDIRECT_DAMAGE_SUICIDE_MAGIC("&6{victim} &7s'est suicidé avec une potion de dégat."),
		
		PERMISSIONS_COMMANDS_EXECUTE(""),
		PERMISSIONS_COMMANDS_HELP(""),
		PERMISSIONS_COMMANDS_RELOAD(""),
		PERMISSIONS_COMMANDS_UNTAG(""),
		PERMISSIONS_ARROW("");
		
		private final String path;
	    private final EMessageBuilder french;
	    private final EMessageBuilder english;
	    private EMessageFormat message;
	    private EMessageBuilder builder;
	    
	    private EPMessages(final String french) {   	
	    	this(EMessageFormat.builder().chat(new EFormatString(french), true));
	    }
	    
	    private EPMessages(final String french, final String english) {   	
	    	this(EMessageFormat.builder().chat(new EFormatString(french), true), 
	    		EMessageFormat.builder().chat(new EFormatString(english), true));
	    }
	    
	    private EPMessages(final EMessageBuilder french) {   	
	    	this(french, french);
	    }
	    
	    private EPMessages(final EMessageBuilder french, final EMessageBuilder english) {
	    	Preconditions.checkNotNull(french, "Le message '" + this.name() + "' n'est pas définit");
	    	
	    	this.path = this.resolvePath();	    	
	    	this.french = french;
	    	this.english = english;
	    	this.message = french.build();
	    }

	    public String getName() {
			return this.name();
		}
	    
		public String getPath() {
			return this.path;
		}

		public EMessageBuilder getFrench() {
			return this.french;
		}

		public EMessageBuilder getEnglish() {
			return this.english;
		}
		
		public EMessageFormat getMessage() {
			return this.message;
		}
		
		public EMessageBuilder getBuilder() {
			return this.builder;
		}
		
		public void set(EMessageBuilder message) {
			this.message = message.build();
			this.builder = message;
		}
	}
	
	@Override
	public EnumMessage getPrefix() {
		return EPMessages.PREFIX;
	}
}
