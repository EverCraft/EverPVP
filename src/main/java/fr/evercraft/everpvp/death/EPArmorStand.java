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
package fr.evercraft.everpvp.death;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.rotation.Rotations;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import com.flowpowered.math.vector.Vector3d;

import fr.evercraft.everapi.java.UtilsInteger;
import fr.evercraft.everapi.sponge.UtilsItemStack;
import fr.evercraft.everpvp.EverPVP;

public class EPArmorStand {
	private List<Entity> entities = new ArrayList<Entity>();
	
	public EPArmorStand(EverPVP plugin) {
		this.reload();
	}

	public void spawnArmorStand(Player victim){
		Vector3d position = victim.getLocation().getBlockPosition().toDouble();
		Location<World> location = victim.getWorld().getLocation(position.add(0.5, -1.24, 0.5));
		Extent extent = location.getExtent();
	    Entity entity = extent.createEntity(EntityTypes.ARMOR_STAND, location.getPosition());
	    if (entity instanceof ArmorStand) {
	        ArmorStand armorStand = (ArmorStand) entity;
	        // General
	        armorStand.offer(Keys.ARMOR_STAND_HAS_ARMS, true);
	        armorStand.offer(Keys.ARMOR_STAND_HAS_BASE_PLATE, false);
	        armorStand.offer(Keys.ARMOR_STAND_HAS_GRAVITY, false);
	        armorStand.offer(Keys.ARMOR_STAND_MARKER, false);
	        armorStand.offer(Keys.ARMOR_STAND_IS_SMALL, false);
	        // Position ArmorSTAND
	        armorStand.offer(Keys.HEAD_ROTATION, new Vector3d(310, 0, UtilsInteger.range(-45, 45)));
	        armorStand.offer(Keys.LEFT_ARM_ROTATION, new Vector3d(UtilsInteger.range(-45, 45), 0, 270));
	        armorStand.offer(Keys.RIGHT_ARM_ROTATION, new Vector3d(UtilsInteger.range(-45, 45), 0, 90));
	        armorStand.offer(Keys.CHEST_ROTATION, new Vector3d(285, 0, 0));
	        armorStand.offer(Keys.LEFT_LEG_ROTATION, new Vector3d(180, 0, 0));
	        armorStand.offer(Keys.RIGHT_LEG_ROTATION, new Vector3d(180, 0, 0));
	        armorStand.offer(Keys.ROTATION, Rotations.TOP);
	        // Equipement 
	        armorStand.setHelmet(UtilsItemStack.createPlayerHead(victim.getProfile()));
	        if(victim.getChestplate().isPresent()){
	        	armorStand.setChestplate(victim.getChestplate().get());
	        } else {
	        	//armorStand.setChestplate(ItemStack.of(ItemTypes.LEATHER_CHESTPLATE, 1));
	        }
	        if(victim.getItemInHand(HandTypes.MAIN_HAND).isPresent()){
	        	armorStand.setItemInHand(HandTypes.MAIN_HAND, victim.getItemInHand(HandTypes.MAIN_HAND).get());
	        }
	        armorStand.setItemInHand(HandTypes.OFF_HAND, ItemStack.of(ItemTypes.CHEST, 1));
	        extent.spawnEntity(armorStand, 
	        		Cause.source(EntitySpawnCause.builder()
	        				.entity(armorStand)
	        				.type(SpawnTypes.PLUGIN)
	        				.build())
	        			.build());
	        entities.add(armorStand);
	    }
	}
	
	public void reload(){
		for(Entity entity : entities){
			entity.remove();
		}
		entities.clear();
	}
}
