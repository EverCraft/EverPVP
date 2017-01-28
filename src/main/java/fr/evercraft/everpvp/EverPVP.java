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

import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import fr.evercraft.everapi.EverAPI;
import fr.evercraft.everapi.exception.PluginDisableException;
import fr.evercraft.everapi.plugin.EPlugin;
import fr.evercraft.everapi.services.PVPService;
import fr.evercraft.everpvp.bossbar.ManagerBossBar;
import fr.evercraft.everpvp.command.sub.EPReload;
import fr.evercraft.everpvp.command.sub.EPUntag;
import fr.evercraft.everpvp.death.EPArmorStand;
import fr.evercraft.everpvp.death.EPDeathMessage;
import fr.evercraft.everpvp.service.EPVPService;
import fr.evercraft.everpvp.service.ETask;
import fr.evercraft.everpvp.service.event.ManagerEvent;

@Plugin(id = "everpvp", 
		name = "EverPVP", 
		version = EverAPI.VERSION, 
		description = "PVP",
		url = "http://evercraft.fr/",
		authors = {"rexbut","lesbleu"},
		dependencies = {
		    @Dependency(id = "everapi", version = EverAPI.VERSION),
		    @Dependency(id = "spongeapi", version = EverAPI.SPONGEAPI_VERSION)
		})
public class EverPVP extends EPlugin<EverPVP> {

	private EPConfig configs;
	private EPMessage messages;
	private EPVPService service;
	private EPArmorStand armorStand;
	
	private ETask task;
	
	private ManagerEvent event;
	private ManagerBossBar bossbar;
	
	@Override
	protected void onPreEnable() throws PluginDisableException {
		// Configurations
		this.configs = new EPConfig(this);
		this.messages = new EPMessage(this);
		this.event = new ManagerEvent(this);
		this.armorStand = new EPArmorStand(this);
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
		// Listerners
		this.getGame().getEventManager().registerListeners(this, new EPListener(this));
		this.getGame().getEventManager().registerListeners(this, new EPDeathMessage(this));
		
		// Commands
		EPCommand command = new EPCommand(this);
				
		command.add(new EPReload(this, command));
		command.add(new EPUntag(this, command));
	}
	
	@Override
	protected void onReload() throws PluginDisableException {
		// Configurations
		this.reloadConfigurations();
		this.bossbar.reload();
		this.service.reload();
		this.task.reload();
		this.armorStand.reload();
	}
	
	@Override
	protected void onDisable() {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void onStopServer() {
		this.armorStand.reload();
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
	
	public EPArmorStand getArmorStand(){
		return this.armorStand;
	}
}
