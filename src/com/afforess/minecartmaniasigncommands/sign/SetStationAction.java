package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.utils.StringUtils;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class SetStationAction implements SignAction {
    protected String station = null;
    
    public SetStationAction(final Sign sign) {
        
        for (int i = 0; i < sign.getNumLines(); i++) {
            final String line = sign.getLine(i);
            if (line.toLowerCase().contains("[station")) {
                final String val[] = line.split(":");
                if (val.length != 2) {
                    continue;
                }
                station = StringUtils.removeBrackets(val[1].trim());
                //check following lines
                while ((++i < sign.getNumLines()) && sign.getLine(i).startsWith("-")) {
                    station += StringUtils.removeBrackets(sign.getLine(i).substring(1));
                }
                break;
            }
        }
    }
    
    public boolean execute(final MinecartManiaMinecart minecart) {
        if (minecart.hasPlayerPassenger()) {
            MinecartManiaWorld.getMinecartManiaPlayer(minecart.getPlayerPassenger()).setLastStation(station);
            return true;
        }
        return false;
    }
    
    public boolean async() {
        return true;
    }
    
    public boolean valid(final Sign sign) {
        return station != null;
    }
    
    public String getName() {
        return "setstationsign";
    }
    
    public String getFriendlyName() {
        return "Set Station Sign";
    }
    
}
