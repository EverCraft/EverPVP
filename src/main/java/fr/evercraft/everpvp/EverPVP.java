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

import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import fr.evercraft.everapi.exception.PluginDisableException;
import fr.evercraft.everapi.plugin.EPlugin;
import fr.evercraft.everapi.services.pvp.PVPService;
import fr.evercraft.everpvp.bossbar.ManagerBossBar;
import fr.evercraft.everpvp.service.EPVPService;
import fr.evercraft.everpvp.service.ETask;
import fr.evercraft.everpvp.service.ManagerEvent;

@Plugin(id = "fr.evercraft.everpvp", 
		name = "EverPVP", 
		version = "1.2", 
		description = "PVP",
		url = "http://evercraft.fr/",
		authors = {"rexbut","lesbleu"},
		dependencies = {
		    @Dependency(id = "fr.evercraft.everapi", version = "1.2")
		})
public class EverPVP extends EPlugin {

	private EPConfig configs;
	private EPMessage messages;
	private EPVPService service;
	
	private ETask task;
	
	private ManagerEvent event;
	private ManagerBossBar bossbar;
	
	@Override
	protected void onPreEnable() throws PluginDisableException {
		// Configurations
		this.configs = new EPConfig(this);
		this.messages = new EPMessage(this);
		this.event = new ManagerEvent(this);
	}
	
	@Override
	protected void onEnable() throws PluginDisableException {
		this.task = new ETask(this);
		this.bossbar = new ManagerBossBar(this);
		this.service = new EPVPService(this);
		
		this.getGame().getServiceManager().setProvider(this, PVPService.class, this.service);
	}
	
	@Override
	protected void onCompleteEnable() throws PluginDisableException {		
		// Commands
		new EPCommand(this);
		
		// Listerners
		this.getGame().getEventManager().registerListeners(this, new EPListener(this));
	}
	
	@Override
	protected void onReload() throws PluginDisableException {
		// Configurations
		this.reloadConfigurations();
		this.bossbar.reload();
		this.service.reload();
		this.task.reload();
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
	
	public ManagerEvent getManagerEvent(){
		return this.event;
	}
	
	public ManagerBossBar getManagerBossBar(){
		return this.bossbar;
	}
	
	public EPVPService getService(){
		return this.service;
	}
	
	public ETask getTask(){
		return this.task;
	}
}
