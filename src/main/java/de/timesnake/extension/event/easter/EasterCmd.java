/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.event.easter;

import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.CommandListener;
import de.timesnake.basic.bukkit.util.chat.cmd.Completion;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.extension.event.Plugin;
import de.timesnake.extension.event.main.ExEvent;
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import net.kyori.adventure.text.Component;

import java.util.Map;

public class EasterCmd implements CommandListener {

  private final Code perm = Plugin.EASTER.createPermssionCode("exevent.easter");
  private final Code eggNotExists = Plugin.EASTER.createHelpCode("Egg not exists");

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
        sender.sendPluginMessage(
            Component.text("Egg " + eggArg.toLowerCase(), ExTextColor.PERSONAL));
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
        sender.sendPluginMessage(
            Component.text("Enabled easter event", ExTextColor.PERSONAL));
      }
      case "disable" -> {
        ExEvent.getInstance().getEasterEvent().setEnabled(false);
        sender.sendPluginMessage(
            Component.text("Disabled easter event", ExTextColor.PERSONAL));
      }
    }
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion(this.perm)
        .addArgument(new Completion("clear", "enable", "disable"))
        .addArgument(new Completion("egg")
            .addArgument(new Completion(EasterEvent.easterEggsByName.keySet())));
  }

  @Override
  public String getPermission() {
    return this.perm.getPermission();
  }
}
