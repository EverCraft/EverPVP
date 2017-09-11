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

import java.util.Arrays;
import java.util.List;

import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlay;
import org.spongepowered.api.boss.BossBarOverlays;

import fr.evercraft.everapi.plugin.file.EConfig;
import fr.evercraft.everapi.sponge.UtilsBossBar;

public class EPConfig extends EConfig<EverPVP> {

	public EPConfig(EverPVP plugin) {
		super(plugin);
	}
	
	public void reload() {
		super.reload();
		this.plugin.getELogger().setDebug(this.isDebug());
	}
	
	@Override
	public List<String> getHeader() {
		return 	Arrays.asList(	"####################################################### #",
								"                    EverPVP (By rexbut)                  #",
								"    For more information : https://docs.evercraft.fr     #",
								"####################################################### #");
	}

	public void loadDefault() {
		this.configDefault();
		
		addDefault("cooldown-fight", 45);
		addDefault("disconnected-in-fight", true, "Si true le joueur sera tué si il déconnecte en combat.");
		// BossBar : Fight
		addDefault("bossbar.fight.message", "&4Vous êtes en combat pour encore {time} !");
		addDefault("bossbar.fight.color", BossBarColors.RED.getName());
		addDefault("bossbar.fight.overlay", BossBarOverlays.PROGRESS.getName());
		addDefault("bossbar.fight.darkenSky", false);
		addDefault("bossbar.fight.playEndBossMusic", false);
		addDefault("bossbar.fight.createFog", false);
		
		// BossBar : EndFight
		addDefault("bossbar.end-fight.stay", 15);
		addDefault("bossbar.end-fight.percent", 100);
		addDefault("bossbar.end-fight.message", "&aVous n'êtes plus en combat !");
		addDefault("bossbar.end-fight.color", BossBarColors.GREEN.getName());
		addDefault("bossbar.end-fight.overlay", BossBarOverlays.PROGRESS.getName());
		addDefault("bossbar.end-fight.darkenSky", false);
		addDefault("bossbar.end-fight.playEndBossMusic", false);
		addDefault("bossbar.end-fight.createFog", false);
	}

	public long getCooldown() {
		return this.get("cooldown-fight").getLong(45);
	}
	
	/*
	 * BossBar Fight
	 */
	
	public String getBossBarFightMessage() {
		return this.plugin.getChat().replace(this.get("bossbar.fight.message").getString(""));
	}
	
	public BossBarColor getBossBarFightColor() {
		return UtilsBossBar.getColor(this.get("bossbar.fight.color").getString("")).orElse(BossBarColors.RED);
	}
	
	public BossBarOverlay getBossBarFightOverlay() {
		return UtilsBossBar.getOverlay(this.get("bossbar.fight.overlay").getString("")).orElse(BossBarOverlays.PROGRESS);
	}
	
	public boolean getBossBarFightDarkenSky() {
		return this.get("bossbar.fight.darkenSky").getBoolean(false);
	}
	
	public boolean getBossBarFightPlayEndBossMusic() {
		return this.get("bossbar.fight.playEndBossMusic").getBoolean(false);
	}

	public boolean getBossBarFightCreateFog() {
		return this.get("bossbar.fight.createFog").getBoolean(false);
	}
	
	/*
	 * BossBar EndFight
	 */
	
	public long getBossBarEndFightStay() {
		return this.get("bossbar.end-fight.stay").getLong(15);
	}
	
	public long getBossBarEndFightPercent() {
		return this.get("bossbar.end-fight.percent").getLong(100);
	}
	
	public String getBossBarEndFightMessage() {
		return this.plugin.getChat().replace(this.get("bossbar.end-fight.message").getString(""));
	}
	
	public BossBarColor getBossBarEndFightColor() {
		return UtilsBossBar.getColor(this.get("bossbar.end-fight.color").getString("")).orElse(BossBarColors.WHITE);
	}
	
	public BossBarOverlay getBossBarEndFightOverlay() {
		return UtilsBossBar.getOverlay(this.get("bossbar.end-fight.overlay").getString("")).orElse(BossBarOverlays.PROGRESS);
	}
	
	public boolean getBossBarEndFightDarkenSky() {
		return this.get("bossbar.end-fight.darkenSky").getBoolean(false);
	}
	
	public boolean getBossBarEndFightPlayEndBossMusic() {
		return this.get("bossbar.end-fight.playEndBossMusic").getBoolean(false);
	}

	public boolean getBossBarEndFightCreateFog() {
		return this.get("bossbar.end-fight.createFog").getBoolean(false);
	}
}
