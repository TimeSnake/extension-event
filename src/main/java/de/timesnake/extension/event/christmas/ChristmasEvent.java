/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.extension.event.christmas;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.ExItemStack;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.extension.event.Plugin;
import de.timesnake.extension.event.birthday.Present;
import de.timesnake.extension.event.main.ExEvent;
import de.timesnake.library.basic.util.chat.ExTextColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChristmasEvent implements Listener {

    public static final Map<String, Present> presentsByName = new HashMap<>();

    private static void addPresent(Present present) {
        presentsByName.put(present.getName(), present);
    }

    static {
        addPresent(new Present("red1", "f0afa4fffd10863e76c698da2c9c9e799bcf9ab9aa37d8312881734225d3ca"));
        addPresent(new Present("red2", "bd7a9f6ed08dd217fdf09f4652bf6b7af621e1d5f8963605349da73998a443"));
        addPresent(new Present("red3", "6cef9aa14e884773eac134a4ee8972063f466de678363cf7b1a21a85b7"));
        addPresent(new Present("red4", "eca643a539c27575573708c5a426ad6203bc519749fb149ef7f1b2a683398"));
        addPresent(new Present("yellow1", "a3e58ea7f3113caecd2b3a6f27af53b9cc9cfed7b043ba334b5168f1391d9"));
        addPresent(new Present("yellow2", "f9b2ea2dedc7435a575273e0d4172c045374f1937dc28e4ae911f14577ad83"));
        addPresent(new Present("purple1", "f65ce3cd66abe0aca867a135c4bf6a02ed2b961d7af841236257d2dd03a0bfc2"));
        addPresent(new Present("lime", "96b9cb35a406473208064c0298edb5de79aeff1824be72a8045fe3a4751b724f"));
        addPresent(new Present("green1", "dcdf70b28a7eb9b1eac78a9278b9087ccdae3d80c0f6b57798e4148acbebf249"));
        addPresent(new Present("purple2", "f57b2ee656d7b865c3fadd5b1428c358d4763f4178ac599d604869a19d7"));
        addPresent(new Present("blue1", "43e165857c4c2b7d6d8d70c108cc6d464f7f30e04d914731b59e31d425a21"));
        addPresent(new Present("copper", "74300a5f8111753fb9e224d60e46d46fbb03cf3f0974b3dee43887ea5ff01fcb"));
        addPresent(new Present("blue2", "1c6274c22d726fc120ce25736030cc8af238b44bcbf56655207953c414422f"));
        addPresent(new Present("gray", "5f2b3f7cdb59394a0c82fe67e672842d755ce8e8eddaeeb9b5cc173b5501e8d6"));
        addPresent(new Present("shulker_cyan", "6923bc0781a8d57b97a252c918f4a45b4cbc0be8630678137108ff82ca1798bf"));
        addPresent(new Present("netherite", "be0c7af591bd1337944abfac52b9bcf883528db72aacb6dbc4edd56a803667f4"));
        addPresent(new Present("black", "5c712b1971c5f42eeff80551179220c08b8213eacbe6bc19d238c13f86e2c0"));

    }

    private final ExItemStack crown = new Present("crown",
            "b26e38d8c1da8d451d7194f3a7fad90a141dfe6165b6667f7a8cba0919139").getItem();
    private final HashMap<UUID, Integer> foundPresentsByUuid = new HashMap<>();
    private boolean enabled;

    public ChristmasEvent() {
        for (Present present : presentsByName.values()) {
            present.createItemStack();
        }
        Server.registerListener(this, ExEvent.getInstance());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!this.enabled) {
            return;
        }

        Block block = e.getClickedBlock();

        if (block == null || block.getType() == null) {
            return;
        }

        if (block.getType().equals(Material.PLAYER_HEAD) || block.getType().equals(Material.PLAYER_WALL_HEAD)) {
            block.setType(Material.AIR);
            block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, 17);

            User user = Server.getUser(e.getPlayer());
            user.playSound(Sound.ENTITY_PLAYER_LEVELUP, 4);
            user.addCoins(1, false);

            Integer presents = this.foundPresentsByUuid.get(user.getUniqueId());

            if (presents != null) {
                presents++;
            } else {
                presents = 1;
            }

            this.foundPresentsByUuid.put(user.getUniqueId(), presents);

            user.sendActionBarText(Component.text("Presents found: ", ExTextColor.GOLD)
                    .append(Component.text(presents, ExTextColor.VALUE)));

            if (presents % 5 == 0) {
                Server.broadcastMessage(Plugin.CHRISTMAS,
                        user.getChatNameComponent()
                                .append(Component.text(" found ", ExTextColor.GOLD))
                                .append(Component.text(presents, ExTextColor.VALUE))
                                .append(Component.text("presents", ExTextColor.GOLD)));
            }
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

        if (enabled) {
            for (User user : Server.getOnlineUsers()) {
                user.getInventory().setHelmet(this.crown);
                user.updateInventory();
            }
        } else {
            for (User user : Server.getOnlineUsers()) {
                user.getInventory().setHelmet(null);
                user.updateInventory();
            }
        }
    }

    @EventHandler
    public void onUserJoin(UserJoinEvent e) {
        if (this.enabled) {
            e.getUser().getInventory().setHelmet(this.crown);
            e.getUser().updateInventory();
        }
    }

    @EventHandler
    public void onUserQuit(UserQuitEvent e) {
        if (this.enabled) {
            e.getUser().getInventory().setHelmet(null);
            e.getUser().updateInventory();
        }
    }
}
