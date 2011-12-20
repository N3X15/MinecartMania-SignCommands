package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.utils.StringUtils;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class StopAtDestinationAction implements SignAction {
    protected String station = null;
    
    public StopAtDestinationAction(final Sign sign) {
        
        boolean found = false;
        for (final String line : sign.getLines()) {
            if (found) {
                station = StringUtils.removeBrackets(line);
                sign.addBrackets();
                break;
            }
            if (line.toLowerCase().contains("station stop")) {
                found = true;
            }
        }
        
    }
    
    public boolean execute(final MinecartManiaMinecart minecart) {
        if (minecart.hasPlayerPassenger()) {
            if (MinecartManiaWorld.getMinecartManiaPlayer(minecart.getPlayerPassenger()).getLastStation().equals(station)) {
                minecart.stopCart();
                minecart.getPlayerPassenger().sendMessage(LocaleParser.getTextKey("SignCommandsDestination"));
                return true;
            }
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
        return "stopatdestinationsign";
    }
    
    public String getFriendlyName() {
        return "Stop At Destination Sign";
    }
    
}
