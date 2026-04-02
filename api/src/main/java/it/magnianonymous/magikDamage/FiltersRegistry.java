/*
 * This file is part of MagikDamage, licensed under the AGPLv3 license.
 * Copyright (C) IlGrandeAnonimo <paulus.mmix@icloud.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.magnianonymous.magikDamage;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class FiltersRegistry {
    @Getter
    private static FiltersRegistry instance;
    private final ConcurrentHashMap<NamespacedKey, Filter> filters;

    /// Register a filter in your plugin's namespace
    public void register(JavaPlugin plugin, Filter filter) {
        filters.put(new NamespacedKey(plugin, filter.name()), filter);
    }

    /// Register a filter using a custom key
    public void register(NamespacedKey key, Filter filter) {
        filters.put(key, filter);
    }

    /// Check if a filter is registered
    public boolean isRegistered(NamespacedKey key) {
        return filters.containsKey(key);
    }

    /// Get all registered filters
    public Set<Map.Entry<NamespacedKey, Filter>> getAll() {
        return filters.entrySet();
    }

    @ApiStatus.Internal
    public Filter get(NamespacedKey key) {
        return filters.get(key);
    }

    @ApiStatus.Internal
    public void registerMinecraftFilter(Filter filter) {
        filters.put(NamespacedKey.minecraft(filter.name()), filter);
    }

    public FiltersRegistry() {
        filters = new ConcurrentHashMap<>();
        instance = this;
    }
}
