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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.LiteralText.Builder;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import fr.evercraft.everapi.EAMessage.EAMessages;
import fr.evercraft.everapi.plugin.EChat;
import fr.evercraft.everapi.plugin.ECommand;
import fr.evercraft.everapi.server.player.EPlayer;
import fr.evercraft.everpvp.EPMessage.EPMessages;

public class EPCommand extends ECommand<EverPVP> {
	
	public EPCommand(final EverPVP plugin) {
        super(plugin, "everpvp", "pvp");
    }
	
	public boolean testPermission(final CommandSource source) {
		return source.hasPermission(EPPermissions.HELP.get());
	}

	public Text description(final CommandSource source) {
		return EPMessages.DESCRIPTION.getText();
	}
	
	public List<String> tabCompleter(final CommandSource source, final List<String> args) throws CommandException {
		List<String> suggests = new ArrayList<String>();
		if(args.size() == 1){
			if(source.hasPermission(EPPermissions.HELP.get())){
				suggests.add("help");
			}
			if(source.hasPermission(EPPermissions.RELOAD.get())){
				suggests.add("reload");
			}
		}
		return suggests;
	}

	public Text help(final CommandSource source) {
		boolean help = source.hasPermission(EPPermissions.HELP.get());
		boolean reload = source.hasPermission(EPPermissions.RELOAD.get());

		Builder build;
		if(help || reload){
			build = Text.builder("/everpvp <");
			if(help){
				build = build.append(Text.builder("help").onClick(TextActions.suggestCommand("/everpvp help")).build());
				if(reload){
					build = build.append(Text.builder("|").build());
				}
			}
			if(reload){
				build = build.append(Text.builder("reload").onClick(TextActions.suggestCommand("/everpvp reload")).build());
			}
		} else {
			build = Text.builder("/everpvp").onClick(TextActions.suggestCommand("/everpvp"));
		}
		return build.color(TextColors.RED).build();
	}
	
	public boolean execute(final CommandSource source, final List<String> args) {
		// Résultat de la commande :
		boolean resultat = false;
		
		// HELP
		if(args.size() == 0 || (args.size() == 1 && args.get(0).equals("help"))) {
			// Si il a la permission
			if(source.hasPermission(EPPermissions.HELP.get())){
				resultat = commandHelp(source);
			// Il n'a pas la permission
			} else {
				source.sendMessage(EAMessages.NO_PERMISSION.getText());
			}
		// RELOAD
		} else if(args.size() == 1 && args.get(0).equals("reload")) {
			// Si il a la permission
			if(source.hasPermission(EPPermissions.RELOAD.get())){
				resultat = commandReload(source);
			// Il n'a pas la permission
			} else {
				source.sendMessage(EAMessages.NO_PERMISSION.getText());
			}
			// RELOAD
		} else if(args.size() == 2 && args.get(0).equals("untag")) {
			// Si il a la permission
			if(source.hasPermission(EPPermissions.UNTAG.get())){
				if(args.get(1).equals("*")){
					resultat = commandUntagAll(source);
				} else {
					resultat = commandUntag(source, args.get(1));
				}
			// Il n'a pas la permission
			} else {
				source.sendMessage(EAMessages.NO_PERMISSION.getText());
			}
		// Nombre d'argument incorrect
		} else {
			source.sendMessage(getHelp(source).get());
		}
		return resultat;
	}

	private boolean commandUntag(final CommandSource player, final String arg) {
		Optional<EPlayer> optPlayer = this.plugin.getEServer().getEPlayer(arg);
		// Le joueur existe
		if(optPlayer.isPresent()){
			EPlayer target = optPlayer.get();
			if(this.plugin.getService().isFight(target.getUniqueId())){
				this.plugin.getService().remove(target.getUniqueId());
				player.sendMessage(EChat.of(EPMessages.PREFIX.get() + EPMessages.UNTAG_MESSAGE.get()
						.replaceAll("<player>", target.getDisplayName())));
				return true;
			} else {
				player.sendMessage(EChat.of(EPMessages.PREFIX.get() + EPMessages.UNTAG_ERROR.get()
						.replaceAll("<player>", target.getDisplayName())));
				return false;
			}
		// Le joueur est introuvable
		} else {
			player.sendMessage(EPMessages.PREFIX.getText().concat(EAMessages.PLAYER_NOT_FOUND.getText()));
			return false;
		}
	}
	
	private boolean commandUntagAll(final CommandSource player) {
		this.plugin.getService().reload();
		player.sendMessage(EPMessages.PREFIX.getText().concat(EPMessages.UNTAGALL_MESSAGE.getText()));
		return true;
	}

	private boolean commandReload(final CommandSource player) {
		this.plugin.reload();
		player.sendMessage(EChat.of(EPMessages.PREFIX.get() + EAMessages.RELOAD_COMMAND.get()));
		return true;
	}
	
	private boolean commandHelp(final CommandSource source) {
		return true;
	}
}
