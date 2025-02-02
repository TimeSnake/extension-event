/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.event.birthday;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import de.timesnake.basic.bukkit.util.user.inventory.ExItemStack;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class Present {

  private final String name;
  private final String url;
  private ExItemStack item;

  public Present(String name, String tag) {
    this.name = name;
    this.url = "https://textures.minecraft.net/texture/" + tag;
  }

  public String getName() {
    return name;
  }

  public String getUrl() {
    return url;
  }

  public ExItemStack createItemStack() {
    GameProfile profile = new GameProfile(UUID.randomUUID(), "test");
    PropertyMap propertyMap = profile.getProperties();
    if (propertyMap == null) {
      throw new IllegalStateException("Profile doesn't contain a property map");
    }
    byte[] encodedData = new Base64().encode(
        String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
    propertyMap.put("textures", new Property("textures", new String(encodedData)));
    this.item = new ExItemStack(Material.PLAYER_HEAD);
    ItemMeta headMeta = this.item.getItemMeta();
    Class<?> headMetaClass = headMeta.getClass();
    try {
      Field profileField = headMetaClass.getDeclaredField("profile");
      profileField.setAccessible(true);
      profileField.set(headMeta, profile);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
    this.item.setItemMeta(headMeta);

    return this.item;
  }

  public ExItemStack getItem() {
    return item;
  }

}
