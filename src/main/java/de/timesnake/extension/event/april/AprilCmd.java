/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.event.april;

import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.CommandListener;
import de.timesnake.basic.bukkit.util.chat.cmd.Completion;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.extension.event.main.ExEvent;
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.Plugin;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;

public class AprilCmd implements CommandListener {

  private final Code perm = Plugin.SERVER.createPermssionCode("exevent.april");
  ;

  @Override
  public void onCommand(Sender sender, PluginCommand cmd,
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
  public Completion getTabCompletion() {
    return new Completion(this.perm)
        .addArgument(new Completion("enable", "disable"));
  }

  @Override
  public String getPermission() {
    return this.perm.getPermission();
  }
}
