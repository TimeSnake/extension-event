/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.event.christmas;

import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.CommandListener;
import de.timesnake.basic.bukkit.util.chat.cmd.Completion;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.extension.event.birthday.BirthdayEvent;
import de.timesnake.extension.event.birthday.Present;
import de.timesnake.extension.event.main.ExEvent;
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.chat.Plugin;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import net.kyori.adventure.text.Component;

import java.util.Map;

public class ChristmasCmd implements CommandListener {

  private final Code perm = Plugin.SERVER.createPermssionCode("exevent.christmas");
  private final Code presentNotExists = Plugin.SERVER.createHelpCode("Present not exists");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
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
      case "present" -> {
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
          sender.sendMessageNotExist(presentArg, this.presentNotExists, "Present");
          return;
        }
        user.addItem(present.getItem());
        sender.sendPluginMessage(Component.text("Present " + presentArg.toLowerCase(),
            ExTextColor.PERSONAL));
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
        ExEvent.getInstance().getChristmasEvent().clearEggs(user.getLocation(), radius);
      }
      case "enable" -> {
        ExEvent.getInstance().getChristmasEvent().setEnabled(true);
        sender.sendPluginMessage(
            Component.text("Enabled christmas event", ExTextColor.PERSONAL));
      }
      case "disable" -> {
        ExEvent.getInstance().getChristmasEvent().setEnabled(false);
        sender.sendPluginMessage(
            Component.text("Disabled christmas event", ExTextColor.PERSONAL));
      }
    }
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion(this.perm)
        .addArgument(new Completion("clear", "enable", "disable"))
        .addArgument(new Completion("present")
            .addArgument(new Completion(BirthdayEvent.presentsByName.keySet())));

  }

  @Override
  public String getPermission() {
    return this.perm.getPermission();
  }
}
