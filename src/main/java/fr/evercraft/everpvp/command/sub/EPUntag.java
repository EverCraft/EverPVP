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
package fr.evercraft.everpvp.command.sub;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import fr.evercraft.everapi.EAMessage.EAMessages;
import fr.evercraft.everapi.event.FightEvent;
import fr.evercraft.everapi.plugin.command.ESubCommand;
import fr.evercraft.everapi.server.player.EPlayer;
import fr.evercraft.everpvp.EPCommand;
import fr.evercraft.everpvp.EPPermissions;
import fr.evercraft.everpvp.EverPVP;
import fr.evercraft.everpvp.EPMessage.EPMessages;

public class EPUntag extends ESubCommand<EverPVP> {
	public EPUntag(final EverPVP plugin, final EPCommand command) {
        super(plugin, command, "untag");
    }
	
	public boolean testPermission(final CommandSource source) {
		return source.hasPermission(EPPermissions.UNTAG.get());
	}

	public Text description(final CommandSource source) {
		return EPMessages.UNTAG_DESCRIPTION.getText();
	}
	
	public Collection<String> tabCompleter(final CommandSource source, final List<String> args) throws CommandException {
		return Arrays.asList();
	}

	public Text help(final CommandSource source) {
		return Text.builder("/" + this.getName())
					.onClick(TextActions.suggestCommand("/" + this.getName()))
					.color(TextColors.RED)
					.build();
	}
	
	public CompletableFuture<Boolean> execute(final CommandSource source, final List<String> args) {
		if (args.size() == 1) {
			if (args.get(1).equals("*")){
				return this.commandUntagAll(source);
			} else {
				return this.commandUntag(source, args.get(0));
			}
		} else {
			source.sendMessage(this.help(source));
		}
		return CompletableFuture.completedFuture(false);
	}

	private CompletableFuture<Boolean> commandUntag(final CommandSource player, final String arg) {
		Optional<EPlayer> optPlayer = this.plugin.getEServer().getEPlayer(arg);
		// Le joueur est introuvable
		if (!optPlayer.isPresent()) {
			EAMessages.PLAYER_NOT_FOUND.sender()
				.prefix(EPMessages.PREFIX)
				.replace("<player>", arg)
				.sendTo(player);
			return CompletableFuture.completedFuture(false);
		}
		
		EPlayer target = optPlayer.get();
		if (!this.plugin.getService().isFight(target.getUniqueId())) {
			EPMessages.UNTAG_ERROR.sender()
				.replace("<player>", target.getDisplayName())
				.sendTo(player);
			return CompletableFuture.completedFuture(false);
		}
		
		this.plugin.getService().remove(target.getUniqueId(), FightEvent.Stop.Reason.COMMAND);
		EPMessages.UNTAG_MESSAGE.sender()
			.replace("<player>", target.getDisplayName())
			.sendTo(player);
		return CompletableFuture.completedFuture(true);
	}
	
	private CompletableFuture<Boolean> commandUntagAll(final CommandSource player) {
		this.plugin.getService().unTagAll();
		EPMessages.UNTAGALL_MESSAGE.sendTo(player);
		return CompletableFuture.completedFuture(true);
	}
}
