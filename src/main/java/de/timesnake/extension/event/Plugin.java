/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.event;

import de.timesnake.library.basic.util.LogHelper;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Plugin extends de.timesnake.basic.bukkit.util.chat.Plugin {

    public static final Plugin EASTER = new Plugin("Easter", "XEE",
            LogHelper.getLogger("Easter", Level.INFO));
    public static final Plugin BIRTHDAY = new Plugin("Birthday", "XEB",
            LogHelper.getLogger("Birthday", Level.INFO));
    public static final Plugin CHRISTMAS = new Plugin("Christmas", "XEC",
            LogHelper.getLogger("Christmas", Level.INFO));
    public static final Plugin APRIL = new Plugin("April", "XEA",
            LogHelper.getLogger("April", Level.INFO));

    protected Plugin(String name, String code, Logger logger) {
        super(name, code, logger);
    }
}
