/*
 * This file is part of MagikDamage, licensed under the AGPLv3 license.
 * Copyright (C) IlGrandeAnonimo <magnianonymous@icloud.com>
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

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMove;
import de.exlll.configlib.Configuration;
import de.exlll.configlib.SerializeWith;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import it.magnianonymous.magikDamage.configuration.serializers.HexSerializer;
import it.magnianonymous.magikDamage.misc.DecimalFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.tofaa.entitylib.meta.display.AbstractDisplayMeta;
import me.tofaa.entitylib.meta.display.TextDisplayMeta;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;

@Getter
@Configuration
@NoArgsConstructor
@SuppressWarnings("unused")
public class Indicator {
    private static BukkitScheduler scheduler = Bukkit.getScheduler();
    private static MagikDamage plugin = MagikDamage.getInstance();

    private String format;
    private HashMap<String, String> filters;
    private Display display;

    private transient HashMap<Integer, WrapperEntity> instances;

    public synchronized Collection<? extends Player> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }

    private void animate(WrapperEntity entity, BoundingBox entityHitbox) {
        final com.github.retrooper.packetevents.protocol.world.Location location = entity.getLocation();
        final Vector entitySize = entityHitbox.getMax().clone()
            .subtract(entityHitbox.getMin());
        final Vector centerPos = entityHitbox.getMin().clone()
            .midpoint(entityHitbox.getMax());

        double movementX = entitySize.getX() * 0.5 + (Math.random() * 0.3);
        double movementZ = entitySize.getZ() * 0.5 + (Math.random() * 0.3);
        final double deltaY = (entityHitbox.getMinY() - location.getY()) - 2;

        if(location.getX() < centerPos.getX()) {
            movementX = -movementX;
        }else if(location.getZ() < centerPos.getZ()) {
            movementZ = -movementZ;
        }

        final double deltaX = movementX;
        final double deltaZ = movementZ;

        entity.sendPacketToViewers(new WrapperPlayServerEntityRelativeMove(
            entity.getEntityId(), movementX, 2, movementZ, false
        ));
        scheduler.runTaskLaterAsynchronously(plugin, () ->
            entity.sendPacketToViewers(new WrapperPlayServerEntityRelativeMove(
                entity.getEntityId(), deltaX, deltaY, deltaZ, false
            )), 5L);
    }

    public void spawn(Entity entity, double healthVariation) {
        final WrapperEntity indicatorEntity;
        final BoundingBox boundingBox = entity.getBoundingBox();
        final Vector entitySize = boundingBox.getMax()
            .subtract(boundingBox.getMin());
        final Location spawn = entity.getLocation().toBlockLocation()
            .add(new Vector(
                entitySize.getX() * Math.random(),
                entitySize.getY() * Math.random(),
                entitySize.getZ() * Math.random()
            ));

        final int entityId = SpigotReflectionUtil.generateEntityId();
        indicatorEntity = new WrapperEntity(entityId, EntityTypes.TEXT_DISPLAY);
        TextDisplayMeta meta = (TextDisplayMeta) indicatorEntity.getEntityMeta();
        meta.setBillboardConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
        meta.setBackgroundColor(display.background);
        meta.setPositionRotationInterpolationDuration(6);
        meta.setShadow(display.shadow);

        final var text = Component
            .text(DecimalFormatter.format(format, healthVariation))
            .color(TextColor.color(display.foreground));
        meta.setText(text);

        meta.setSeeThrough(display.seeThrough);
        indicatorEntity.spawn(SpigotConversionUtil.fromBukkitLocation(spawn));

        getOnlinePlayers().stream()
            .filter(player -> spawn.getWorld().equals(player.getWorld())
                && spawn.distanceSquared(player.getLocation()) < 900)
            .map(Player::getUniqueId)
            .forEach(indicatorEntity::addViewer);

        if(display.animate) {
            animate(indicatorEntity, boundingBox);
        }
        scheduler.runTaskLaterAsynchronously(plugin, indicatorEntity::despawn, 20L);
    }

    public void killAll() {
        instances.forEach(this::kill);
    }

    public void kill(int id, @Nullable WrapperEntity wrapper) {
        if (wrapper != null) {
            wrapper.remove();
        } else if (instances.containsKey(id)) {
            WrapperEntity entity = instances.get(id);
            entity.remove();
        }
    }

    @Configuration
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Display {
        @SerializeWith(serializer = HexSerializer.class)
        int background;
        @SerializeWith(serializer = HexSerializer.class)
        int foreground;
        boolean shadow;
        boolean seeThrough;
        boolean animate;
    }

    public enum Viewer {
        SUBJECT, ATTACKER, OTHERS
    }

    public Indicator(
        String format,
        HashMap<String, String> filters,
        Display display
    ) {
        this.format = format;
        this.filters = filters;
        this.display = display;
    }
}
