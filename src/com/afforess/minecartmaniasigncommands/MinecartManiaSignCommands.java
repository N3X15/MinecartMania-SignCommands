package com.afforess.minecartmaniasigncommands;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.config.MinecartManiaConfigurationParser;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniasigncommands.sensor.SensorDataTable;
import com.afforess.minecartmaniasigncommands.sensor.SensorManager;
import com.afforess.minecartmaniasigncommands.sign.HoldSignData;

public class MinecartManiaSignCommands extends JavaPlugin {
    
    public static MinecartManiaLogger log = MinecartManiaLogger.getInstance();
    public static Server server;
    public static PluginDescriptionFile description;
    public static MinecartActionListener listener = new MinecartActionListener();
    public static MinecartManiaSignCommands instance;
    public static final int DATABASE_VERSION = 2;
    
    public void onDisable() {
        
    }
    
    public void onEnable() {
        server = getServer();
        description = getDescription();
        instance = this;
        MinecartManiaConfigurationParser.read(description.getName() + "Configuration.xml", MinecartManiaCore.getDataDirectoryRelativePath(), new SignCommandsSettingParser());
        getServer().getPluginManager().registerEvents(listener, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, listener, Priority.Low, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_ENTER, vehicleListener, Priority.Monitor, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_EXIT, vehicleListener, Priority.Monitor, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Priority.Normal, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
        
        //sensor setup
        final File ebeans = new File(new File(getDataFolder().getParent()).getParent(), "ebean.properties");
        if (!ebeans.exists()) {
            try {
                ebeans.createNewFile();
                final PrintWriter pw = new PrintWriter(ebeans);
                pw.append("# General logging level: (none, explicit, all)");
                pw.append('\n');
                pw.append("ebean.logging=none");
                pw.close();
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        final File oldSensorData = new File(MinecartManiaCore.getDataDirectoryRelativePath(), "Sensors.data");
        if (oldSensorData.exists()) {
            oldSensorData.delete();
        }
        setupDatabase();
        int maxId = 0;
        final List<SensorDataTable> data = getDatabase().find(SensorDataTable.class).findList();
        for (final SensorDataTable temp : data) {
            if (temp.hasValidLocation()) {
                final Block block = temp.getLocation().getBlock();
                if (SensorManager.isSign(block)) {
                    SensorManager.getSensor(block, true); //force load of sensor
                    if (temp.getId() > maxId) {
                        maxId = temp.getId();
                    }
                }
            }
        }
        SensorDataTable.lastId = maxId;
        log.info(description.getName() + " version " + description.getVersion() + " is enabled!");
    }
    
    private int getDatabaseVersion() {
        try {
            getDatabase().find(SensorDataTable.class).findRowCount();
        } catch (final PersistenceException ex) {
            return 0;
        }
        try {
            getDatabase().find(HoldSignData.class).findRowCount();
        } catch (final PersistenceException ex) {
            return 1;
        }
        return DATABASE_VERSION;
    }
    
    protected void setupInitialDatabase() {
        try {
            getDatabase().find(SensorDataTable.class).findRowCount();
            getDatabase().find(HoldSignData.class).findRowCount();
        } catch (final PersistenceException ex) {
            log.info("Installing database");
            installDDL();
        }
    }
    
    protected void setupDatabase() {
        final int version = getDatabaseVersion();
        switch (version) {
            case 0:
                setupInitialDatabase();
                break;
            case 1:
                upgradeDatabase(1);
                break;
            case 2: /* up to date database */
                break;
        }
    }
    
    private void upgradeDatabase(final int current) {
        log.info(String.format("Upgrading database from version %d to version %d", current, DATABASE_VERSION));
        if (current == 1) {
            final List<SensorDataTable> sensorList = getDatabase().find(SensorDataTable.class).findList();
            try {
                removeDDL();
            } catch (final Exception e) {
                //this will throw an error because not all the tables can be dropped, but ignore it
            }
            setupInitialDatabase();
            log.info("Recoved " + sensorList.size() + " from database");
            for (final SensorDataTable sensor : sensorList) {
                final SensorDataTable temp = new SensorDataTable();
                temp.setId(sensor.getId());
                temp.setMaster(sensor.isMaster());
                temp.setName(sensor.getName());
                temp.setState(sensor.isState());
                temp.setType(sensor.getType());
                temp.setWorld(sensor.getWorld());
                temp.setX(sensor.getX());
                temp.setY(sensor.getY());
                temp.setZ(sensor.getZ());
                getDatabase().save(temp);
            }
        }
        
        /*
         * Add additional versions here
         */
    }
    
    @Override
    public List<Class<?>> getDatabaseClasses() {
        final List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(SensorDataTable.class);
        list.add(HoldSignData.class);
        return list;
    }
}
