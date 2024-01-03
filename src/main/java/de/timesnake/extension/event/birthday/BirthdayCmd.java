/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.event.birthday;

import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.CommandListener;
import de.timesnake.basic.bukkit.util.chat.cmd.Completion;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.extension.event.Plugin;
import de.timesnake.extension.event.main.ExEvent;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import de.timesnake.library.extension.util.chat.Code;
import net.kyori.adventure.text.Component;

import java.util.Map;

public class BirthdayCmd implements CommandListener {

  private final Code perm = Plugin.BIRTHDAY.createPermssionCode("exevent.birthday");
  private final Code presentNotExists = Plugin.BIRTHDAY.createHelpCode("Present not exists");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
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
      case "present" -> {
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
          sender.sendMessageNotExist(eggArg, this.presentNotExists, "Present");
          return;
        }
        user.addItem(egg.getItem());
        sender.sendPluginMessage(
            Component.text("Present" + eggArg.toLowerCase(), ExTextColor.PERSONAL));
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
        ExEvent.getInstance().getBirthdayEvent().clearEggs(user.getLocation(), radius);
      }
      case "enable" -> {
        ExEvent.getInstance().getBirthdayEvent().setEnabled(true);
        sender.sendPluginMessage(
            Component.text("Enabled birthday event", ExTextColor.PERSONAL));
      }
      case "disable" -> {
        ExEvent.getInstance().getBirthdayEvent().setEnabled(false);
        sender.sendPluginMessage(
            Component.text("Disabled birthday event", ExTextColor.PERSONAL));
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
