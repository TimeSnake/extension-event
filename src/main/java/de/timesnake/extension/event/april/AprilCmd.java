/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.event.april;

import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.extension.event.main.ExEvent;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import java.util.List;

public class AprilCmd implements CommandListener {

    private Code perm;

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd,
            Arguments<Argument> args) {

        sender.hasPermissionElseExit(this.perm);

        if (!args.isLengthHigherEquals(1, true)) {
            return;
        }

        if (!sender.isPlayer(true)) {
            return;
        }

        User user = sender.getUser();

        switch (args.getString(0).toLowerCase()) {
            case "enable" -> {
                ExEvent.getInstance().getAprilEvent().setEnabled(true);
                sender.sendPluginTDMessage("§sEnabled april event");
            }
            case "disable" -> {
                ExEvent.getInstance().getAprilEvent().setEnabled(false);
                sender.sendPluginTDMessage("§sDisabled april event");
            }
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd,
            Arguments<Argument> args) {
        if (args.getLength() == 1) {
            return List.of("enable", "disable");
        }
        return null;
    }

    @Override
    public void loadCodes(Plugin plugin) {
        this.perm = plugin.createPermssionCode("exevent.april");
    }
}
