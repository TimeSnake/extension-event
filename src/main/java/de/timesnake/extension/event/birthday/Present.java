/*
 * extension-event.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

package de.timesnake.extension.event.birthday;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import de.timesnake.basic.bukkit.util.user.ExItemStack;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

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
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }
        byte[] encodedData = new Base64().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        propertyMap.put("textures", new Property("textures", new String(encodedData)));
        this.item = new ExItemStack(Material.PLAYER_HEAD);
        ItemMeta headMeta = this.item.getItemMeta();
        Class<?> headMetaClass = headMeta.getClass();
        BirthdayEvent.getField(headMetaClass, "profile", GameProfile.class, 0).set(headMeta, profile);
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        this.item.setItemMeta(headMeta);

        return this.item;
    }

    public ExItemStack getItem() {
        return item;
    }

}
