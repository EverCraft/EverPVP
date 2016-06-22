package fr.evercraft.everpvp.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import fr.evercraft.everapi.EAMessage.EAMessages;
import fr.evercraft.everapi.command.ESubCommand;
import fr.evercraft.everapi.plugin.EChat;
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
		return EChat.of(EPMessages.UNTAG_DESCRIPTION.get());
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
		
		if(args.size() == 1) {
			if(args.get(1).equals("*")){
				resultat = commandUntagAll(source);
			} else {
				resultat = commandUntag(source, args.get(0));
			}
		} else {
			source.sendMessage(this.help(source));
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
}
