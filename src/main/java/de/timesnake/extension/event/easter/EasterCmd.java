/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.event.easter;

import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.extension.event.main.ExEvent;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EasterCmd implements CommandListener {

    private Code.Permission perm;
    private Code.Help eggNotExists;

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {

        if (!sender.hasPermission(this.perm)) {
            return;
        }

        if (!args.isLengthHigherEquals(1, true)) {
            return;
        }

        if (!sender.isPlayer(true)) {
            return;
        }

        User user = sender.getUser();

        switch (args.getString(0).toLowerCase()) {
            case "egg" -> {
                if (!args.isLengthEquals(2, true)) {
                    return;
                }
                EasterEgg egg = null;
                String eggArg = args.getString(1);
                for (Map.Entry<String, EasterEgg> entry : EasterEvent.easterEggsByName.entrySet()) {
                    if (eggArg.equalsIgnoreCase(entry.getKey())) {
                        egg = entry.getValue();
                        break;
                    }
                }
                if (egg == null) {
                    sender.sendMessageNotExist(eggArg, this.eggNotExists, "Egg");
                    return;
                }
                user.addItem(egg.getItem());
                sender.sendPluginMessage(Component.text("Egg " + eggArg.toLowerCase(), ExTextColor.PERSONAL));
            }
            case "clear" -> {
                if (!args.isLengthEquals(2, true)) {
                    return;
                }
                Argument argRadius = args.get(1);
                if (!argRadius.isInt(true)) {
                    return;
                }
                int radius = argRadius.toInt();
                ExEvent.getInstance().getEasterEvent().clearEggs(user.getLocation(), radius);
            }
            case "enable" -> {
                ExEvent.getInstance().getEasterEvent().setEnabled(true);
                sender.sendPluginMessage(Component.text("Enabled easter event", ExTextColor.PERSONAL));
            }
            case "disable" -> {
                ExEvent.getInstance().getEasterEvent().setEnabled(false);
                sender.sendPluginMessage(Component.text("Disabled easter event", ExTextColor.PERSONAL));
            }
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.getLength() == 1) {
            return List.of("egg", "clear", "enable", "disable");
        } else if (args.getLength() == 2 || args.getString(0).equalsIgnoreCase("egg")) {
            return new ArrayList<>(EasterEvent.easterEggsByName.keySet());
        }
        return null;
    }

    @Override
    public void loadCodes(Plugin plugin) {
        this.perm = plugin.createPermssionCode("egg", "exevent.easter");
        this.eggNotExists = plugin.createHelpCode("egg", "Egg not exists");
    }
}
