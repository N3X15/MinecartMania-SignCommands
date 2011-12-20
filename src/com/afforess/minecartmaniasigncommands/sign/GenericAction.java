package com.afforess.minecartmaniasigncommands.sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class GenericAction implements SignAction {
    protected String setting = null;
    protected String key = null;
    protected Object value = null;
    
    public GenericAction(final String setting) {
        this.setting = setting;
        key = setting;
        value = true;
    }
    
    public GenericAction(final String setting, final String key, final Object value) {
        this.setting = setting;
        this.key = key;
        this.value = value;
    }
    
    public boolean execute(final MinecartManiaMinecart minecart) {
        minecart.setDataValue(key, value);
        return true;
    }
    
    public boolean async() {
        return true;
    }
    
    public boolean valid(final Sign sign) {
        for (final String line : sign.getLines()) {
            if (line.toLowerCase().contains(setting.toLowerCase())) {
                sign.addBrackets();
                return true;
            }
        }
        return false;
    }
    
    public String getName() {
        return StringUtils.removeWhitespace(setting.toLowerCase()) + "sign";
    }
    
    public String getFriendlyName() {
        return setting + " Sign";
    }
    
}
