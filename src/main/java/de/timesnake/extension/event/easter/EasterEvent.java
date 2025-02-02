/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.event.easter;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.extension.event.main.ExEvent;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EasterEvent implements Listener {


  public static final Map<String, EasterEgg> easterEggsByName = new HashMap<>();

  static {
    addEgg(new EasterEgg("yellow",
        "3ed037452223bda1381a17c3daafa71baac6ed8aa3a71ae36163cfeb61227b47"));
    addEgg(
        new EasterEgg("leaf", "a6a6051f7f6f439d8f214c234e8e2c477630052432e42607f0404b840b53ceab"));
    addEgg(new EasterEgg("yellow1",
        "5bf6299c98709fe7ced8b95a719251b17f75d46489f948095095561c235f970e"));
    addEgg(
        new EasterEgg("aqua", "fb734421bf980ba1b755c7a36f6dd6c9fa4977a1289c9fab46442420e245c636"));
    addEgg(
        new EasterEgg("cyan", "2c70406e753942135424bb99692b744c9a0345d3f3c8193e9d8e30d5441218e3"));
    addEgg(
        new EasterEgg("pink", "dc7df23a37a9ddfcd31318bf9e88ba25adbd652b7a611fa671dbbc8c931e55a"));
    addEgg(
        new EasterEgg("blue", "5628531eb5f0569edae16c8a43eb22eecf7c15235b835ae1414c269cad12ca7"));
    addEgg(new EasterEgg("lightblue",
        "a240a1a6e23cb787d4daf133ab2c8b9abac4efb81e739caec58b3e8ea54987"));
    addEgg(
        new EasterEgg("green", "b2cd5df9d7f1fa8341fcce2f3c118e2f517e4d2d99df2c51d61d93ed7f83e13"));
    addEgg(new EasterEgg("yellow_brown",
        "e9b7d8c0636f73932eba734bad1826cc8c16d783aa9a5cfccda461a0105932"));
    addEgg(
        new EasterEgg("pink1", "8ad3daebae2f5e718b9526d52b5b3199d68e336f9e4d943b283d818419e366"));
    addEgg(
        new EasterEgg("pink2", "58b9e29ab1a795e2b887faf1b1a31025e7cc3073330afec375393b45fa335d1"));
    addEgg(
        new EasterEgg("white", "264430e493feb5eaa145582e54e761a8603fb16cc0ff1268a5d1e864e6f479f6"));
    addEgg(
        new EasterEgg("chick", "5358233710818212c013a110398524253c5d949f50fb099c951d0cb3b410edff"));
    addEgg(
        new EasterEgg("lime", "d7f8519cc5a6243d858949a6c5739dce85a7619ad0e71c53701c80d476046a00"));
    addEgg(
        new EasterEgg("orange", "5d70e58cc13e398a22157556a93827369bb7f40220bd7d84e77bd35b0774dfe"));
    addEgg(new EasterEgg("orange_pink",
        "5b3e03639922a51f268e856fbb0dd4c19d206878b1ce6b5addc6b9f8f42fadcd"));
    addEgg(new EasterEgg("purple",
        "3066ac587165d50a1af1117cf38e4b06bc7ebbaf0c17dd24ffd44e9ff845e555"));
  }

  private static void addEgg(EasterEgg easterEgg) {
    easterEggsByName.put(easterEgg.getName(), easterEgg);
  }


  private boolean enabled;

  public EasterEvent() {
    for (EasterEgg easterEgg : easterEggsByName.values()) {
      easterEgg.createItemStack();
    }
    Server.registerListener(this, ExEvent.getInstance());
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent e) {
    if (!this.enabled) {
      return;
    }

    Block block = e.getClickedBlock();

    if (block == null) {
      return;
    }

    if (block.getType().equals(Material.PLAYER_HEAD) || block.getType()
        .equals(Material.PLAYER_WALL_HEAD)) {
      block.setType(Material.AIR);
      block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, 17);

      User user = Server.getUser(e.getPlayer());
      user.playSound(Sound.ENTITY_PLAYER_LEVELUP, 4);
      user.addCoins(1, true);
    }
  }

  public void clearEggs(Location location, int radius) {
    for (int x = location.getBlockX() - radius; x < location.getBlockX() + radius; x++) {
      for (int y = 0; y < 255; y++) {
        for (int z = location.getBlockZ() - radius; z < location.getBlockZ() + radius; z++) {
          Block block = location.getWorld().getBlockAt(x, y, z);
          if (block.getType().equals(Material.PLAYER_HEAD)) {
            block.setType(Material.AIR);
          }
        }
      }
    }
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public static Object getAttributeValue(Object obj, String attributeName) {
    Field field = null;
    try {
      field = obj.getClass().getDeclaredField(attributeName);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
    field.setAccessible(true);

    Object value = null;
    try {
      value = field.get(obj);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return value;
  }

  static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType,
      int index) {
    for (final Field field : target.getDeclaredFields()) {
      if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(
          field.getType()) && index-- <= 0) {
        field.setAccessible(true);

        // A function for retrieving a specific field value
        return new FieldAccessor<>() {
          @SuppressWarnings("unchecked")
          @Override
          public T get(Object target) {
            try {
              return (T) field.get(target);
            } catch (IllegalAccessException e) {
              throw new RuntimeException("Cannot access reflection.", e);
            }
          }

          @Override
          public void set(Object target, Object value) {
            try {
              field.set(target, value);
            } catch (IllegalAccessException e) {
              throw new RuntimeException("Cannot access reflection.", e);
            }
          }

          @Override
          public boolean hasField(Object target) {
            // target instanceof DeclaringClass
            return field.getDeclaringClass().isAssignableFrom(target.getClass());
          }
        };
      }
    }

    // Search in parent classes
    if (target.getSuperclass() != null) {
      return getField(target.getSuperclass(), name, fieldType, index);
    }
    throw new IllegalArgumentException("Cannot find field with type " + fieldType);
  }

  public interface FieldAccessor<T> {

    /**
     * Retrieve the content of a field.
     *
     * @param target the target object, or NULL for a static field
     * @return the value of the field
     */
    T get(Object target);

    /**
     * Set the content of a field.
     *
     * @param target the target object, or NULL for a static field
     * @param value  the new value of the field
     */
    void set(Object target, Object value);

    /**
     * Determine if the given object has this field.
     *
     * @param target the object to test
     * @return TRUE if it does, FALSE otherwise
     */
    boolean hasField(Object target);
  }
}
