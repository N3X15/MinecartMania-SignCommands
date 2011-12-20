package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;

public class LockCartAction extends GenericAction {
    public static final String name = "Lock Cart";
    
    public LockCartAction(final Sign sign) {
        super(name);
    }
    
    @Override
    public boolean execute(final MinecartManiaMinecart minecart) {
        if (minecart.hasPlayerPassenger()) {
            if (minecart.getDataValue(key) == null) {
                minecart.getPlayerPassenger().sendMessage(LocaleParser.getTextKey("SignCommandsMinecartLocked"));
            }
        }
        return super.execute(minecart);
    }
    
    @Override
    public boolean valid(final Sign sign) {
        for (final String line : sign.getLines()) {
            if (line.toLowerCase().contains(name.toLowerCase()) && !line.toLowerCase().contains(UnlockCartAction.name.toLowerCase())) {
                sign.addBrackets();
                return true;
            }
        }
        return false;
    }
    
}
