package com.afforess.minecartmaniasigncommands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.event.MinecartActionEvent;
import com.afforess.minecartmaniacore.event.MinecartCaughtEvent;
import com.afforess.minecartmaniacore.event.MinecartClickedEvent;
import com.afforess.minecartmaniacore.event.MinecartLaunchedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaListener;
import com.afforess.minecartmaniacore.event.MinecartManiaMinecartCreatedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaMinecartDestroyedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaSignFoundEvent;
import com.afforess.minecartmaniacore.event.MinecartMotionStartEvent;
import com.afforess.minecartmaniacore.event.MinecartMotionStopEvent;
import com.afforess.minecartmaniacore.event.MinecartPassengerEjectEvent;
import com.afforess.minecartmaniacore.event.MinecartTimeEvent;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.FailureReason;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.signs.SignManager;
import com.afforess.minecartmaniacore.utils.SignUtils;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;
import com.afforess.minecartmaniasigncommands.sensor.GenericSensor;
import com.afforess.minecartmaniasigncommands.sensor.Sensor;
import com.afforess.minecartmaniasigncommands.sensor.SensorConstructor;
import com.afforess.minecartmaniasigncommands.sensor.SensorManager;
import com.afforess.minecartmaniasigncommands.sign.EjectionAction;
import com.afforess.minecartmaniasigncommands.sign.EjectionConditionAction;
import com.afforess.minecartmaniasigncommands.sign.HoldSignData;
import com.afforess.minecartmaniasigncommands.sign.SignType;

public class MinecartActionListener extends MinecartManiaListener {
    
    @EventHandler(priority = EventPriority.NORMAL)
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
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicleExit(final VehicleExitEvent event) {
        if (event.getVehicle() instanceof Minecart) {
            final MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart) event.getVehicle());
            SignCommands.updateSensors(minecart);
        }
    }
    
    //Test 1
    @Override
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMinecartActionEvent(final MinecartActionEvent event) {
        final MinecartManiaMinecart minecart = event.getMinecart();
        final ArrayList<com.afforess.minecartmaniacore.signs.Sign> list = SignUtils.getAdjacentMinecartManiaSignList(minecart.getLocation(), 2);
        for (final com.afforess.minecartmaniacore.signs.Sign sign : list) {
            sign.executeActions(minecart);
        }
        SignCommands.updateSensors(minecart);
        
    }
    
    @Override
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartPassengerEjectEvent(final MinecartPassengerEjectEvent event) {
        final MinecartManiaMinecart minecart = event.getMinecart();
        ArrayList<com.afforess.minecartmaniacore.signs.Sign> list = SignUtils.getAdjacentMinecartManiaSignList(minecart.getLocation(), 2);
        boolean success = false;
        boolean found = false;
        for (final com.afforess.minecartmaniacore.signs.Sign sign : list) {
            if (sign.hasSignAction(EjectionConditionAction.class)) {
                found = true;
                if (sign.executeAction(minecart, EjectionConditionAction.class)) {
                    success = true;
                    break;
                }
            }
        }
        if (found && !success) {
            event.setCancelled(true);
        }
        if (minecart.getDataValue("Eject At Sign") == null) {
            list = SignUtils.getAdjacentMinecartManiaSignList(minecart.getLocation(), 8, true);
            SignUtils.sortByDistance(minecart.getLocation().getBlock(), list);
            for (final com.afforess.minecartmaniacore.signs.Sign sign : list) {
                if (sign.executeAction(minecart, EjectionAction.class)) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }
    
    @Override
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartLaunchedEvent(final MinecartLaunchedEvent event) {
        if (event.isActionTaken())
            return;
        if (event.getMinecart().getDataValue("hold sign data") != null) {
            event.setActionTaken(true);
            return;
        }
    }
    
    @Override
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartCaughtEvent(final MinecartCaughtEvent event) {
        if (event.isActionTaken())
            return;
        if (event.getMinecart().hasPlayerPassenger() && SignCommands.doPassPlayer(event.getMinecart())) {
            event.setActionTaken(true);
            return;
        }
    }
    
    @Override
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMinecartManiaMinecartCreatedEvent(final MinecartManiaMinecartCreatedEvent event) {
        SignCommands.updateSensors(event.getMinecart());
    }
    
    @Override
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMinecartTimeEvent(final MinecartTimeEvent event) {
        final MinecartManiaMinecart minecart = event.getMinecart();
        final HoldSignData data = (HoldSignData) minecart.getDataValue("hold sign data");
        /*
         * if (data == null) { try { data = MinecartManiaSignCommands.instance.getDatabase().find(HoldSignData.class).where().idEq(minecart.minecart.getEntityId()).findUnique(); } catch (Exception e) { List<HoldSignData> list = MinecartManiaSignCommands.instance.getDatabase().find(HoldSignData.class).where().idEq(minecart.minecart.getEntityId()).findList(); MinecartManiaSignCommands.instance.getDatabase().delete(list); } }
         */
        if (data != null) {
            data.setTime(data.getTime() - 1);
            final com.afforess.minecartmaniacore.signs.Sign sign = SignManager.getSignAt(data.getSignLocation());
            if (sign == null) {
                minecart.minecart.setVelocity(data.getMotion());
                minecart.setDataValue("hold sign data", null);
                minecart.setDataValue("HoldForDelay", null);
                return;
            }
            //update sign counter
            if ((data.getLine() < sign.getNumLines()) && (data.getLine() > -1)) {
                if (data.getTime() > 0) {
                    sign.setLine(data.getLine(), "[Holding For " + data.getTime() + "]");
                } else {
                    sign.setLine(data.getLine(), "");
                }
            }
            
            if (data.getTime() == 0) {
                minecart.minecart.setVelocity(data.getMotion());
                minecart.setDataValue("hold sign data", null);
                minecart.setDataValue("HoldForDelay", null);
                //MinecartManiaSignCommands.instance.getDatabase().delete(data);
            } else {
                minecart.setDataValue("hold sign data", data);
                //MinecartManiaSignCommands.instance.getDatabase().update(data);
            }
        }
    }
    
    @Override
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMinecartManiaMinecartDestroyedEvent(final MinecartManiaMinecartDestroyedEvent event) {
        SignCommands.updateSensors(event.getMinecart(), null);
    }
    
    @Override
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMinecartMotionStopEvent(final MinecartMotionStopEvent event) {
        final MinecartManiaMinecart minecart = event.getMinecart();
        if (minecart.getDataValue("Lock Cart") != null) {
            minecart.setDataValue("Lock Cart", null);
            if (minecart.hasPlayerPassenger()) {
                minecart.getPlayerPassenger().sendMessage(LocaleParser.getTextKey("SignCommandsMinecartUnlocked"));
            }
        }
    }
    
    @Override
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMinecartMotionStartEvent(final MinecartMotionStartEvent event) {
        final MinecartManiaMinecart minecart = event.getMinecart();
        if (minecart.getDataValue("HoldForDelay") != null) {
            minecart.stopCart();
            final HoldSignData data = (HoldSignData) minecart.getDataValue("hold sign data");
            minecart.teleport(data.getMinecartLocation());
        }
    }
    
    @Override
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartClickedEvent(final MinecartClickedEvent event) {
        if (event.isActionTaken())
            return;
        final MinecartManiaMinecart minecart = event.getMinecart();
        if ((minecart.getDataValue("Lock Cart") != null) && minecart.isMoving()) {
            if (minecart.hasPlayerPassenger()) {
                minecart.getPlayerPassenger().sendMessage(LocaleParser.getTextKey("SignCommandsMinecartLockedError"));
            }
            event.setActionTaken(true);
        }
    }
    
    @Override
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMinecartManiaSignFoundEvent(final MinecartManiaSignFoundEvent event) {
        final com.afforess.minecartmaniacore.signs.Sign sign = event.getSign();
        for (final SignType type : SignType.values()) {
            final SignAction action = type.getSignAction(sign);
            if (action.valid(sign)) {
                sign.addSignAction(action);
            } else if ((action instanceof FailureReason) && (event.getPlayer() != null)) {
                if (((FailureReason) action).getReason() != null) {
                    event.getPlayer().sendMessage(ChatColor.RED + ((FailureReason) action).getReason());
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockDamage(final BlockDamageEvent event) {
        if (event.getBlock().getState() instanceof Sign) {
            final Sensor previous = SensorManager.getSensor(event.getBlock().getLocation());
            if (previous == null) {
                final Sensor sensor = SensorConstructor.constructSensor((Sign) event.getBlock().getState(), event.getPlayer());
                if (sensor != null) {
                    SensorManager.addSensor(event.getBlock().getLocation(), sensor);
                }
            } else if (!SensorManager.verifySensor((Sign) event.getBlock().getState(), previous)) {
                final Sensor sensor = SensorConstructor.constructSensor((Sign) event.getBlock().getState(), event.getPlayer());
                if (sensor != null) {
                    SensorManager.addSensor(event.getBlock().getLocation(), sensor);
                } else {
                    SensorManager.delSensor(event.getBlock().getLocation());
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPhysics(final BlockPhysicsEvent event) {
        if (event.isCancelled())
            return;
        //Forces diode not to update and disable itself 
        if (event.getBlock().getTypeId() == Material.DIODE_BLOCK_ON.getId()) {
            final ConcurrentHashMap<Block, Sensor> sensorList = SensorManager.getSensorList();
            final Iterator<Entry<Block, Sensor>> i = sensorList.entrySet().iterator();
            while (i.hasNext()) {
                final Entry<Block, Sensor> e = i.next();
                if (SensorManager.isSign(e.getKey()) && ((GenericSensor) e.getValue()).equals(event.getBlock().getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
