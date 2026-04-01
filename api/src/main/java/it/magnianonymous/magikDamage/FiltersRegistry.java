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
