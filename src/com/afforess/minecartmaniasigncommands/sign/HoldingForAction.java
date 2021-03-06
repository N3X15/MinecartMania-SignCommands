package com.afforess.minecartmaniasigncommands.sign;

import org.bukkit.Location;

import com.afforess.minecartmaniacore.config.ControlBlockList;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.signs.SignManager;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class HoldingForAction implements SignAction {
    
    protected int line = -1;
    protected int time = -1;
    protected Location sign;
    
    public HoldingForAction(final Sign sign) {
        this.sign = sign.getLocation();
        
        for (int i = 0; i < sign.getNumLines(); i++) {
            if (sign.getLine(i).toLowerCase().contains("hold for")) {
                try {
                    time = Double.valueOf(StringUtils.getNumber(sign.getLine(i))).intValue();
                } catch (final Exception e) {
                }
            } else if ((line == -1) && sign.getLine(i).contains("[Holding For")) {
                line = i;
            } else if ((line == -1) && sign.getLine(i).trim().isEmpty()) {
                line = i;
            }
        }
        if (time != -1) {
            sign.addBrackets();
        }
    }
    
    public boolean execute(final MinecartManiaMinecart minecart) {
        if (minecart.getDataValue("HoldForDelay") != null)
            return false;
        if (ControlBlockList.isCatcherBlock(minecart.getSpecificMaterialBeneath())) {
            HoldSignData data = null;
            /*
             * try { data = MinecartManiaSignCommands.instance.getDatabase().find(HoldSignData.class).where().idEq(minecart.minecart.getEntityId()).findUnique(); } catch (PersistenceException e) { data = null; }
             */
            if (data == null) {
                data = new HoldSignData(minecart.minecart.getEntityId(), time, line, minecart.getLocation(), sign, minecart.minecart.getVelocity());
            }
            minecart.stopCart();
            minecart.setDataValue("hold sign data", data);
            minecart.setDataValue("HoldForDelay", true);
            //MinecartManiaSignCommands.instance.getDatabase().save(data);
            if (line != -1) {
                final Sign sign = SignManager.getSignAt(this.sign);
                sign.setLine(line, String.format("[Holding For %d]", time));
            }
            return true;
        }
        return false;
    }
    
    public boolean async() {
        return true;
    }
    
    public boolean valid(final Sign sign) {
        return time != -1;
    }
    
    public String getName() {
        return "holdingsign";
    }
    
    public String getFriendlyName() {
        return "Holding Sign";
    }
    
}
