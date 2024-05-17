/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.event;

public class Plugin extends de.timesnake.basic.bukkit.util.chat.Plugin {

  public static final Plugin EASTER = new Plugin("Easter", "XEE");
  public static final Plugin BIRTHDAY = new Plugin("Birthday", "XEB");
  public static final Plugin CHRISTMAS = new Plugin("Christmas", "XEC");
  public static final Plugin APRIL = new Plugin("April", "XEA");

  protected Plugin(String name, String code) {
    super(name, code);
  }
}
