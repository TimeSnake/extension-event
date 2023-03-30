/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.event.main;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.extension.event.Plugin;
import de.timesnake.extension.event.april.AprilCmd;
import de.timesnake.extension.event.april.AprilEvent;
import de.timesnake.extension.event.birthday.BirthdayCmd;
import de.timesnake.extension.event.birthday.BirthdayEvent;
import de.timesnake.extension.event.christmas.ChristmasCmd;
import de.timesnake.extension.event.christmas.ChristmasEvent;
import de.timesnake.extension.event.easter.EasterCmd;
import de.timesnake.extension.event.easter.EasterEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ExEvent extends JavaPlugin {

    private static ExEvent plugin;

    public static ExEvent getInstance() {
        return plugin;
    }

    private EasterEvent easterEvent;
    private BirthdayEvent birthdayEvent;
    private ChristmasEvent christmasEvent;
    private AprilEvent aprilEvent;

    @Override
    public void onEnable() {
        plugin = this;
        this.easterEvent = new EasterEvent();
        Server.getCommandManager().addCommand(this, "easter", new EasterCmd(), Plugin.EASTER);

        this.birthdayEvent = new BirthdayEvent();
        Server.getCommandManager().addCommand(this, "birthday", new BirthdayCmd(), Plugin.BIRTHDAY);

        this.christmasEvent = new ChristmasEvent();
        Server.getCommandManager()
                .addCommand(this, "christmas", new ChristmasCmd(), Plugin.CHRISTMAS);

        this.aprilEvent = new AprilEvent();
        Server.getCommandManager().addCommand(this, "april", new AprilCmd(), Plugin.APRIL);
    }

    public EasterEvent getEasterEvent() {
        return easterEvent;
    }

    public BirthdayEvent getBirthdayEvent() {
        return birthdayEvent;
    }

    public ChristmasEvent getChristmasEvent() {
        return christmasEvent;
    }

    public AprilEvent getAprilEvent() {
        return aprilEvent;
    }

    public static ExEvent getPlugin() {
        return plugin;
    }
}
