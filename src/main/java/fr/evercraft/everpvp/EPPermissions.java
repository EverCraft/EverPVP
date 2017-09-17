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

import fr.evercraft.everapi.plugin.EnumPermission;
import fr.evercraft.everapi.plugin.file.EnumMessage;
import fr.evercraft.everpvp.EPMessage.EPMessages;

public enum EPPermissions implements EnumPermission {
	ARROW("arrow", EPMessages.PERMISSIONS_ARROW),
	
	EVERPVP("commands.execute", EPMessages.PERMISSIONS_COMMANDS_EXECUTE),
	HELP("commands.help", EPMessages.PERMISSIONS_COMMANDS_HELP),
	RELOAD("commands.reload", EPMessages.PERMISSIONS_COMMANDS_RELOAD),
	UNTAG("commands.untag", EPMessages.PERMISSIONS_COMMANDS_UNTAG);
	
	private static final String PREFIX = "everpvp";
	
	private final String permission;
	private final EnumMessage message;
	private final boolean value;
    
    private EPPermissions(final String permission, final EnumMessage message) {
    	this(permission, message, false);
    }
    
    private EPPermissions(final String permission, final EnumMessage message, final boolean value) {   	    	
    	this.permission = PREFIX + "." + permission;
    	this.message = message;
    	this.value = value;
    }

    @Override
    public String get() {
    	return this.permission;
	}

	@Override
	public boolean getDefault() {
		return this.value;
	}

	@Override
	public EnumMessage getMessage() {
		return this.message;
	}
}
