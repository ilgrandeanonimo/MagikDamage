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

package it.magnianonymous.magikDamage.filters;

import it.magnianonymous.magikDamage.Filter;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Objects;

public class EventFilter implements Filter {
    @Override
    public String name() {
        return "event";
    }

    @Override
    public boolean filter(String parameters, EntityEvent event) {
        if(Objects.equals(parameters, "regain")) {
            return event instanceof EntityRegainHealthEvent;
        } else if (parameters.equalsIgnoreCase("damage")) {
            return event instanceof EntityDamageEvent;
        }
        return false;
    }
}
