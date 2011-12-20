package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;

public class PassPlayerAction implements SignAction {
    
    public PassPlayerAction(final Sign sign) {
        
    }
    
    public boolean execute(final MinecartManiaMinecart minecart) {
        return false;
    }
    
    public boolean async() {
        return false;
    }
    
    public boolean valid(final Sign sign) {
        for (final String line : sign.getLines()) {
            if (line.toLowerCase().contains("pass player")) {
                sign.addBrackets();
                return true;
            }
        }
        return false;
    }
    
    public String getName() {
        return "passplayersign";
    }
    
    public String getFriendlyName() {
        return "Pass Player Sign";
    }
    
}
