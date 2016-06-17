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

import fr.evercraft.everpvp.EverPVP;

public class ManagerBossBar {
	public final static String IDENTIFIER = "everpvp";

	private final EverPVP plugin;
	
	private final BossBarFight fight;
	private final BossBarEndFight endFight;
	
	public ManagerBossBar(final EverPVP plugin) {
		this.plugin = plugin;
		
		this.fight = new BossBarFight(this.plugin);
		this.endFight = new BossBarEndFight(this.plugin);
	}
	
	public void reload() {
		this.fight.reload();
		this.endFight.reload();
	}
	
	
	public BossBarFight getFight() {
		return fight;
	}

	public BossBarEndFight getEndFight() {
		return endFight;
	}	
}
