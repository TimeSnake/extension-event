package de.timesnake.extension.event.christmas;

import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.ChatColor;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.extension.event.birthday.BirthdayEvent;
import de.timesnake.extension.event.birthday.Present;
import de.timesnake.extension.event.main.ExEvent;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChristmasCmd implements CommandListener {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {

        if (!sender.hasPermission("exevent.christmas")) {
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

                Present present = null;
                String presentArg = args.getString(1);
                for (Map.Entry<String, Present> entry : BirthdayEvent.presentsByName.entrySet()) {
                    if (presentArg.equalsIgnoreCase(entry.getKey())) {
                        present = entry.getValue();
                        break;
                    }
                }

                if (present == null) {
                    sender.sendMessageNotExist(presentArg, 0, "present");
                    return;
                }

                user.addItem(present.getItem());
                sender.sendPluginMessage(ChatColor.PERSONAL + "Present " + presentArg.toLowerCase());

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

                ExEvent.getInstance().getChristmasEvent().clearEggs(user.getLocation(), radius);
                break;
            case "enable":
                ExEvent.getInstance().getChristmasEvent().setEnabled(true);
                sender.sendPluginMessage(ChatColor.PERSONAL + "Enabled christmas event");
                break;
            case "disable":
                ExEvent.getInstance().getChristmasEvent().setEnabled(false);
                sender.sendPluginMessage(ChatColor.PERSONAL + "Disabled christmas event");
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
