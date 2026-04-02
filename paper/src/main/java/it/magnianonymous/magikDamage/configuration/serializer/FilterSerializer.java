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

package it.magnianonymous.magikDamage.configuration.serializer;

import de.exlll.configlib.Serializer;
import org.bukkit.NamespacedKey;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterSerializer implements Serializer<Map<NamespacedKey, String>, List<String>> {
    private String filterStringRepresentation(NamespacedKey key, @Nullable String parameter) {
        if(parameter != null) {
            return String.format("%s=%s", parameter, key.toString());
        } else {
            return key.toString();
        }
    }

    @Override
    public List<String> serialize(Map<NamespacedKey, String> filterMap) {
        final List<String> list = new ArrayList<>();
        filterMap.forEach((key, parameter) ->
            list.add(filterStringRepresentation(key, parameter)));
        return list;
    }

    @Override
    public Map<NamespacedKey, String> deserialize(List<String> serializedFilters) {
        final HashMap<NamespacedKey, String> map = new HashMap<>();
        for (String string : serializedFilters) {
            final String[] components = string.split("=", 2);
            if(components.length < 2) {
                map.put(NamespacedKey.fromString(components[0]), null);
            } else {
                map.put(NamespacedKey.fromString(components[0]), components[1]);
            }
        }
        return map;
    }
}
