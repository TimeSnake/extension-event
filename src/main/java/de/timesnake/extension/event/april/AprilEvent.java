/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.event.april;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.AsyncUserJoinEvent;
import de.timesnake.extension.event.main.ExEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent.Cause;

public class AprilEvent implements Listener {

    private static final List<String> REASONS = List.of("Hacking", "Auto-Clicker",
            "Bug Exploiting", "Spam", "Offensive Language", "Unknown");

    private final Random random = new Random();
    private final HashMap<UUID, State> stateByUuid = new HashMap<>();
    private boolean enabled;

    public AprilEvent() {
        Server.registerListener(this, ExEvent.getPlugin());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @EventHandler
    public void onUserJoin(AsyncUserJoinEvent e) {
        if (!this.isEnabled()) {
            return;
        }

        User user = e.getUser();

        State state = this.stateByUuid.get(user.getUniqueId());

        if (state == null) {
            Server.runTaskSynchrony(
                    () -> user.kick(Component.text("\n§c§lYour were banned!\n§cReason: "
                            + REASONS.get(this.random.nextInt(REASONS.size()))), Cause.BANNED),
                    ExEvent.getPlugin());
            this.stateByUuid.put(user.getUniqueId(), State.BANNED);
        } else if (state == State.BANNED) {
            Server.runTaskSynchrony(
                    () -> user.kick(Component.text("\n§6§lApril April!"), Cause.KICK_COMMAND),
                    ExEvent.getPlugin());
            this.stateByUuid.put(user.getUniqueId(), State.FOOLED);
        }
    }

    private enum State {
        BANNED, FOOLED
    }
}
