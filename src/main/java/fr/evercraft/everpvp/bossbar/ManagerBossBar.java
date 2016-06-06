/**
 * This file is part of EverInformations.
 *
 * EverInformations is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EverInformations is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EverInformations.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.evercraft.everpvp.bossbar;

import fr.evercraft.everapi.services.priority.PriorityService;
import fr.evercraft.everpvp.EverPVP;

public class ManagerBossBar {
	public final static String IDENTIFIER = "everpvp";

	private final EverPVP plugin;
	
	private int priority;
	
	private final BossBarFight fight;
	private final BossBarEndFight endFight;
	
	public ManagerBossBar(final EverPVP plugin) {
		this.plugin = plugin;
		
		this.start();
		
		this.fight = new BossBarFight(this.plugin);
		this.endFight = new BossBarEndFight(this.plugin);
	}
	
	public void start() {
		this.priority = PriorityService.DEFAULT;
		if(this.plugin.getEverAPI().getManagerService().getPriority().isPresent()) {
			this.priority = this.plugin.getEverAPI().getManagerService().getPriority().get().getBossBar(ManagerBossBar.IDENTIFIER);
		}
	}
	
	public void reload() {
		this.start();
		
		this.fight.reload();
		this.endFight.reload();
	}
	
	
	public BossBarFight getFight() {
		return fight;
	}

	public BossBarEndFight getEndFight() {
		return endFight;
	}

	public int getPriority() {
		return this.priority;
	}
	
}
