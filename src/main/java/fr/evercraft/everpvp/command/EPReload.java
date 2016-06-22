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
package fr.evercraft.everpvp.command;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import fr.evercraft.everapi.EAMessage.EAMessages;
import fr.evercraft.everapi.plugin.EChat;
import fr.evercraft.everapi.plugin.command.ESubCommand;
import fr.evercraft.everpvp.EPCommand;
import fr.evercraft.everpvp.EPMessage.EPMessages;
import fr.evercraft.everpvp.EPPermissions;
import fr.evercraft.everpvp.EverPVP;

public class EPReload extends ESubCommand<EverPVP> {
	public EPReload(final EverPVP plugin, final EPCommand command) {
        super(plugin, command, "reload");
    }
	
	public boolean testPermission(final CommandSource source) {
		return source.hasPermission(EPPermissions.RELOAD.get());
	}

	public Text description(final CommandSource source) {
		return EChat.of(EAMessages.RELOAD_DESCRIPTION.get());
	}
	
	public List<String> subTabCompleter(final CommandSource source, final List<String> args) throws CommandException {
		return new ArrayList<String>();
	}

	public Text help(final CommandSource source) {
		return Text.builder("/" + this.getName())
					.onClick(TextActions.suggestCommand("/" + this.getName()))
					.color(TextColors.RED)
					.build();
	}
	
	public boolean subExecute(final CommandSource source, final List<String> args) {
		// Résultat de la commande :
		boolean resultat = false;
		
		if(args.size() == 0) {
			resultat = commandReload(source);
		} else {
			source.sendMessage(this.help(source));
		}
		return resultat;
	}

	private boolean commandReload(final CommandSource player) {
		this.plugin.reload();
		player.sendMessage(EChat.of(EPMessages.PREFIX.get() + EAMessages.RELOAD_COMMAND.get()));
		return true;
	}
}
