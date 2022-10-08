/*
 * extension-event.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

package de.timesnake.extension.event.main;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.extension.event.Plugin;
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

    @Override
    public void onEnable() {
        plugin = this;
        this.easterEvent = new EasterEvent();
        Server.getCommandManager().addCommand(this, "easter", new EasterCmd(), Plugin.EASTER);

        this.birthdayEvent = new BirthdayEvent();
        Server.getCommandManager().addCommand(this, "birthday", new BirthdayCmd(), Plugin.BIRTHDAY);

        this.christmasEvent = new ChristmasEvent();
        Server.getCommandManager().addCommand(this, "christmas", new ChristmasCmd(), Plugin.CHRISTMAS);
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
}
