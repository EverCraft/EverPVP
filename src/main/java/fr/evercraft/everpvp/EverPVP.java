/**
 * This file is part of EverStats.
 *
 * EverStats is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EverStats is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EverStats.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.evercraft.everpvp;

import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import fr.evercraft.everapi.exception.PluginDisableException;
import fr.evercraft.everapi.plugin.EPlugin;

@Plugin(id = "fr.evercraft.everpvp", 
		name = "EverPVP", 
		version = "1.1", 
		description = "PVP",
		url = "http://evercraft.fr/",
		authors = {"rexbut","lesbleu"},
		dependencies = {
		    @Dependency(id = "fr.evercraft.everapi", version = "1.1")
		})
public class EverPVP extends EPlugin {

	private EPConfig configs;
	private EPMessage messages;
	
	@Override
	protected void onPreEnable() throws PluginDisableException {
		// Configurations
		this.configs = new EPConfig(this);
		this.messages = new EPMessage(this);
	}
	
	@Override
	protected void onCompleteEnable() throws PluginDisableException {		
		// Commands
		new EPCommand(this);
	}
	
	@Override
	protected void onReload() throws PluginDisableException {
		// Configurations
		this.reloadConfigurations();
	}
	
	@Override
	protected void onDisable() {
	}
	
	public EPConfig getConfigs(){
		return this.configs;
	}
	
	public EPMessage getMessages(){
		return this.messages;
	}
}
