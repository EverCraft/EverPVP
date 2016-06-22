package fr.evercraft.everpvp;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import fr.evercraft.everapi.command.EParentCommand;
import fr.evercraft.everapi.plugin.EChat;
import fr.evercraft.everpvp.EPMessage.EPMessages;

public class EPCommand extends EParentCommand<EverPVP> {
	
	public EPCommand(final EverPVP plugin) {
        super(plugin, "everpvp", "pvp");
    }
	
	@Override
	public boolean testPermission(final CommandSource source) {
		return source.hasPermission(EPPermissions.EVERPVP.get());
	}

	@Override
	public Text description(final CommandSource source) {
		return EChat.of(EPMessages.DESCRIPTION.get());
	}

	@Override
	public boolean testPermissionHelp(final CommandSource source) {
		return source.hasPermission(EPPermissions.HELP.get());
	}
}