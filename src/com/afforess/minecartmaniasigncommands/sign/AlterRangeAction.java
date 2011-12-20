package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.config.MinecartManiaConfiguration;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.minecart.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.utils.MathUtils;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class AlterRangeAction implements SignAction {
    protected int range = -1;
    protected boolean itemRange = false;
    protected boolean rangeY = false;
    
    public AlterRangeAction(final Sign sign) {
        
        for (final String line : sign.getLines()) {
            if (line.toLowerCase().contains("range")) {
                final String[] split = line.split(":");
                if (split.length != 2) {
                    continue;
                }
                try {
                    range = Integer.parseInt(StringUtils.getNumber(split[1]));
                    range = MathUtils.range(range, MinecartManiaConfiguration.getMinecartMaximumRange(), 0);
                } catch (final Exception e) {
                    range = -1;
                }
                itemRange = line.toLowerCase().contains("item range");
                rangeY = line.toLowerCase().contains("rangey");
                sign.addBrackets();
                break;
            }
        }
    }
    
    public boolean execute(final MinecartManiaMinecart minecart) {
        if (itemRange) {
            if (minecart.isStorageMinecart()) {
                ((MinecartManiaStorageCart) minecart).setItemRange(range);
                return true;
            }
        } else if (rangeY) {
            minecart.setRangeY(range);
        } else {
            minecart.setRange(range);
            return true;
        }
        return false;
    }
    
    public boolean async() {
        return true;
    }
    
    public boolean valid(final Sign sign) {
        return range != -1;
    }
    
    public String getName() {
        return "alterrangesign";
    }
    
    public String getFriendlyName() {
        return "Alter Range Sign";
    }
    
}
