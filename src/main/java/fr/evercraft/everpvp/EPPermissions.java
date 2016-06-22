package fr.evercraft.everpvp;

import org.spongepowered.api.command.CommandSource;

import com.google.common.base.Preconditions;

import fr.evercraft.everapi.plugin.EnumPermission;

public enum EPPermissions implements EnumPermission {
	ARROW("arrow"),
	EVERPVP("command"),
	HELP("help"),
	RELOAD("reload"),
	UNTAG("untag");
	
	private final static String prefix = "everpvp";
	
	private final String permission;
    
    private EPPermissions(final String permission) {   	
    	Preconditions.checkNotNull(permission, "La permission '" + this.name() + "' n'est pas définit");
    	
    	this.permission = permission;
    }

    public String get() {
		return EPPermissions.prefix + "." + this.permission;
	}
    
    public boolean has(CommandSource player) {
    	return player.hasPermission(this.get());
    }
}