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

import com.github.retrooper.packetevents.PacketEvents;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurationStore;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import it.magnianonymous.magikDamage.configuration.Settings;
import it.magnianonymous.magikDamage.configuration.serializer.MiniMessageSerializer;
import it.magnianonymous.magikDamage.paper.MagikCommand;
import it.magnianonymous.magikDamage.filters.CauseFilter;
import it.magnianonymous.magikDamage.filters.EntityFilter;
import it.magnianonymous.magikDamage.filters.EventFilter;
import it.magnianonymous.magikDamage.listener.DamageListener;
import it.magnianonymous.magikDamage.utils.DecimalFormatter;
import lombok.Getter;
import me.tofaa.entitylib.APIConfig;
import me.tofaa.entitylib.EntityLib;
import me.tofaa.entitylib.spigot.SpigotEntityLibPlatform;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Getter
@SuppressWarnings("UnstableApiUsage")
public final class MagikDamage extends JavaPlugin {
    @Getter
    private static MagikDamage instance;
    private volatile Settings settings;
    private FiltersRegistry filtersRegistry;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        instance = this;
        loadConfiguration();
        loadUtils();
        registerFilters();
        registerListeners();
        registerCommand();
    }

    private void loadEntityLib() {
        SpigotEntityLibPlatform platform = new SpigotEntityLibPlatform(this);
        APIConfig config = new APIConfig(PacketEvents.getAPI())
            .tickTickables()
            .usePlatformLogger();
        EntityLib.init(platform, config);
    }

    private void loadPacketEvents() {
        PacketEvents.getAPI()
            .getSettings()
            .checkForUpdates(false);
        PacketEvents.getAPI().init();
    }

    private void loadUtils() {
        if(!getServer().getPluginManager().isPluginEnabled("PacketEvents")) {
            loadPacketEvents();
        }
        loadEntityLib();
    }

    private void registerFilters() {
        this.filtersRegistry = new FiltersRegistry();
        filtersRegistry.registerMinecraftFilter(new CauseFilter());
        filtersRegistry.registerMinecraftFilter(new EntityFilter());
        filtersRegistry.registerMinecraftFilter(new EventFilter());
    }

    private void loadConfiguration() {
        final File settingsFile = new File(getDataFolder(), "config.yml");
        final YamlConfigurationStore<Settings> settingsStore =
            new YamlConfigurationStore<>(Settings.class, YamlConfigurationProperties
                .newBuilder()
                .addSerializer(Component.class, new MiniMessageSerializer())
                .charset(StandardCharsets.UTF_8)
                .header(Settings.HEADER)
                .build()
            );
        this.settings = settingsStore.update(settingsFile.toPath());
        DecimalFormatter.loadFormats(settings);
    }

    private void registerListeners() {
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new DamageListener(this, filtersRegistry), this);
    }

    private void registerCommand() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
            commands -> commands.registrar().register(
                MagikCommand.create().build()
            ));
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }

    public void reload() {
        loadConfiguration();
    }
}
