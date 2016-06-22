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
package fr.evercraft.everpvp.service;

import java.util.concurrent.TimeUnit;

import org.spongepowered.api.scheduler.Task;

import fr.evercraft.everpvp.EverPVP;

public class ETask {

	private final EverPVP plugin;
	
	private Task task;
	
	public ETask(final EverPVP plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Active ou désactive le scheduler
	 */
	public void reload() {
		// Si il y a pas de scheduler et qu'il y a des joueurs
		if(this.task == null && (this.plugin.getService().update() || this.plugin.getManagerBossBar().getEndFight().update())) {
			this.taskStart();
		// Si il y a un scheduler et qu'il n'y a pas de joueur
		} else if(this.task != null && !(this.plugin.getService().update() || this.plugin.getManagerBossBar().getEndFight().update())) {
			this.taskStop();
		}
	}
	
	/**
	 * Active le scheduler
	 */
	private boolean taskStart() {
		if(this.task == null) {
			this.task = this.plugin.getGame().getScheduler().createTaskBuilder()
					.execute(() -> updateAsync())
					.delay(1, TimeUnit.SECONDS)
					.interval(1, TimeUnit.SECONDS)
					.async()
					.submit(this.plugin);
			return true;
		}
		return false;
	}
	
	/**
	 * Désactive le scheduler
	 */
	private boolean taskStop() {
		if(this.task != null) {
			this.task.cancel();
			this.task = null;
			return true;
		}
		return false;
	}
	
	private void updateAsync() {
		this.plugin.getService().updateAsync();
		this.plugin.getManagerBossBar().getEndFight().updateAsync();
	}
}
