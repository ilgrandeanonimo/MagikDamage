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

package it.magnianonymous.magikDamage.misc;

import it.magnianonymous.magikDamage.Indicator;
import it.magnianonymous.magikDamage.MagikDamage;
import it.magnianonymous.magikDamage.filters.CauseFilter;
import it.magnianonymous.magikDamage.filters.EntityFilter;
import it.magnianonymous.magikDamage.filters.EventFilter;
import it.magnianonymous.magikDamage.filters.Filter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("deprecation")
@RequiredArgsConstructor
public final class DamageListener implements Listener {
    private final MagikDamage plugin;
    private final Map<String, Filter> filters = Map.of(
        "cause", new CauseFilter(),
        "entity", new EntityFilter(),
        "event", new EventFilter()
    );

    private Optional<Indicator> findIndicator(EntityEvent event) {
        final var indicators = plugin.getSettings().getIndicators();

        Indicator defaultIndicator = null;
        for(var indicator : indicators.values()) {
            var indicatorFilters = indicator.getFilters();
            if(indicatorFilters == null || indicatorFilters.isEmpty()) {
                defaultIndicator = indicator;
                continue;
            }

            boolean allMatch = true;
            for(var entry : indicatorFilters.entrySet()) {
                var filter = filters.get(entry.getKey());
                if(filter == null || !filter.filter(entry.getValue(), event)) {
                    allMatch = false;
                    break;
                }
            }

            if(allMatch)
                return Optional.of(indicator);
        }

        if(defaultIndicator != null)
            return Optional.of(defaultIndicator);
        return Optional.empty();
    }

    public void handleVariation(double variation, Entity entity, EntityEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(MagikDamage.getInstance(), () -> {
            var indicator = findIndicator(event);
            if (indicator.isEmpty()) return;
            indicator.get().spawn(entity, variation);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof LivingEntity entity)) return;
        double damage = event.getFinalDamage() - event.getOriginalDamage(
            EntityDamageEvent.DamageModifier.ABSORPTION);
        if(damage <= 0) return;
        handleVariation(-damage, entity, event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if(!(event.getEntity() instanceof LivingEntity entity)) return;
        double maxHealth = Optional.ofNullable(entity.getAttribute(Attribute.MAX_HEALTH))
            .map(AttributeInstance::getValue)
            .orElse(-1d);
        if(maxHealth <= 0) return;
        double actualHealth = entity.getHealth();
        double regain = Math.min(actualHealth + event.getAmount(), maxHealth) - actualHealth;
        if(regain <= 0) return;
        handleVariation(regain, entity, event);
    }
}
