package fr.evercraft.everpvp.death;

import java.util.Optional;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.util.rotation.Rotations;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import com.flowpowered.math.vector.Vector3d;

import fr.evercraft.everapi.java.UtilsInteger;
import fr.evercraft.everapi.plugin.EChat;
import fr.evercraft.everapi.sponge.UtilsItemStack;
import fr.evercraft.everpvp.EverPVP;
import fr.evercraft.everpvp.EPMessage.EPMessages;

public class EPArmorStand {
	private EverPVP plugin;
	
	public EPArmorStand(EverPVP plugin) {
		this.plugin = plugin;
	}

	public void spawnArmorStand(Location<World> location, Player victim){
	    Extent extent = location.getExtent();
	    Optional<Entity> optional = extent.createEntity(EntityTypes.ARMOR_STAND, location.getPosition());
	    if (optional.isPresent()) {
	        ArmorStand armorStand = (ArmorStand) optional.get();
	        // General
	        armorStand.offer(Keys.ARMOR_STAND_HAS_ARMS, true);
	        armorStand.offer(Keys.ARMOR_STAND_HAS_BASE_PLATE, false);
	        armorStand.offer(Keys.ARMOR_STAND_HAS_GRAVITY, false);
	        armorStand.offer(Keys.ARMOR_STAND_MARKER, true);
	        armorStand.offer(Keys.ARMOR_STAND_IS_SMALL, false);
	        armorStand.offer(Keys.DISPLAY_NAME, EChat.of(
	        		EPMessages.ARMORSTAND_NAME.get()
	        			.replaceAll("<player>", victim.getName())));
	        armorStand.offer(Keys.CUSTOM_NAME_VISIBLE, true);
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
	        	this.plugin.getEServer().broadcast("Test getChestplate");
	        	armorStand.setChestplate(victim.getChestplate().get());
	        }
	        if(victim.getItemInHand(HandTypes.MAIN_HAND).isPresent()){
	        	armorStand.setItemInHand(HandTypes.MAIN_HAND, victim.getItemInHand(HandTypes.MAIN_HAND).get());
	        	this.plugin.getEServer().broadcast("Test setItemInHand");
	        }
	        extent.spawnEntity(armorStand, 
	        		Cause.source(EntitySpawnCause.builder()
	        				.entity(armorStand)
	        				.type(SpawnTypes.PLUGIN)
	        				.build())
	        			.build());
	    }
	}
}
