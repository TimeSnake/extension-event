package de.timesnake.extension.event.birthday;

import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.ChatColor;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.extension.event.main.ExEvent;
import de.timesnake.library.basic.util.cmd.Arguments;
import de.timesnake.library.basic.util.cmd.ExCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BirthdayCmd implements CommandListener {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {

        if (!sender.hasPermission("exevent.birthday")) {
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
            case "present":
                if (!args.isLengthEquals(2, true)) {
                    return;
                }

                Present egg = null;
                String eggArg = args.getString(1);
                for (Map.Entry<String, Present> entry : BirthdayEvent.presentsByName.entrySet()) {
                    if (eggArg.equalsIgnoreCase(entry.getKey())) {
                        egg = entry.getValue();
                        break;
                    }
                }

                if (egg == null) {
                    sender.sendMessageNotExist(eggArg, 0, "egg");
                    return;
                }

                user.addItem(egg.getItem());
                sender.sendPluginMessage(ChatColor.PERSONAL + "Present" + eggArg.toLowerCase());

                break;
            case "clear":
                if (!args.isLengthEquals(2, true)) {
                    return;
                }

                Argument argRadius = args.get(1);
                if (!argRadius.isInt(true)) {
                    return;
                }

                int radius = argRadius.toInt();

                ExEvent.getInstance().getBirthdayEvent().clearEggs(user.getLocation(), radius);
                break;
            case "enable":
                ExEvent.getInstance().getBirthdayEvent().setEnabled(true);
                sender.sendPluginMessage(ChatColor.PERSONAL + "Enabled birthday event");
                break;
            case "disable":
                ExEvent.getInstance().getBirthdayEvent().setEnabled(false);
                sender.sendPluginMessage(ChatColor.PERSONAL + "Disabled birthday event");
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.getLength() == 1) {
            return List.of("present", "clear", "enable", "disable");
        } else if (args.getLength() == 2 || args.getString(0).equalsIgnoreCase("present")) {
            return new ArrayList<>(BirthdayEvent.presentsByName.keySet());
        }
        return null;
    }
}
