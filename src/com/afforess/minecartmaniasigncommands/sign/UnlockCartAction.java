package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;

public class UnlockCartAction extends GenericAction {
    public static final String name = "Unlock Cart";
    
    public UnlockCartAction(final Sign sign) {
        super(name, LockCartAction.name, null);
    }
    
    @Override
    public boolean execute(final MinecartManiaMinecart minecart) {
        if (minecart.hasPlayerPassenger()) {
            if (minecart.getDataValue(key) != null) {
                minecart.setDataValue(key, null);
                minecart.getPlayerPassenger().sendMessage(LocaleParser.getTextKey("SignCommandsMinecartUnlocked"));
            }
        }
        return super.execute(minecart);
    }
}
