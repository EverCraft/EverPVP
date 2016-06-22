package fr.evercraft.everpvp;

import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import fr.evercraft.everapi.exception.PluginDisableException;
import fr.evercraft.everapi.plugin.EPlugin;
import fr.evercraft.everapi.services.PVPService;
import fr.evercraft.everpvp.bossbar.ManagerBossBar;
import fr.evercraft.everpvp.command.EPReload;
import fr.evercraft.everpvp.command.EPUntag;
import fr.evercraft.everpvp.service.EPVPService;
import fr.evercraft.everpvp.service.ETask;
import fr.evercraft.everpvp.service.event.ManagerEvent;

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
