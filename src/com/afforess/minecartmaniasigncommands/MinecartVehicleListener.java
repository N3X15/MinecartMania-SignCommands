package com.afforess.minecartmaniasigncommands;

import org.bukkit.entity.Minecart;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleListener;

import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class MinecartVehicleListener extends VehicleListener {
    
    @Override
    public void onVehicleEnter(final VehicleEnterEvent event) {
        if (event.getVehicle() instanceof Minecart) {
            if (event.isCancelled())
                return;
            final MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart) event.getVehicle());
            if ((minecart.getDataValue("Lock Cart") != null) && minecart.isMoving()) {
                if (minecart.hasPlayerPassenger()) {
                    minecart.getPlayerPassenger().sendMessage(LocaleParser.getTextKey("SignCommandsMinecartLockedError"));
                }
                event.setCancelled(true);
                return;
            }
            
            SignCommands.updateSensors(minecart);
        }
    }
    
    @Override
    public void onVehicleExit(final VehicleExitEvent event) {
        if (event.getVehicle() instanceof Minecart) {
            final MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart) event.getVehicle());
            SignCommands.updateSensors(minecart);
        }
    }
}
