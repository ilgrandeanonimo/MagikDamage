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

package it.magnianonymous.magikDamage.filters;

import it.magnianonymous.magikDamage.Filter;
import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.EntityEvent;

public class EntityFilter implements Filter {
    @Override
    public String name() {
        return "entity";
    }

    @Override
    public boolean filter(String parameters, EntityEvent event) {
        var entity = event.getEntity();
        if (parameters == null || parameters.isEmpty()) return false;

        final NamespacedKey key;
        if (parameters.contains(":")) {
            String[] components = parameters.split(":", 2);
            key = new NamespacedKey(components[0], components[1]);
        } else {
            key = NamespacedKey.minecraft(parameters);
        }

        return entity.getType().getKey().equals(key);
    }
}
