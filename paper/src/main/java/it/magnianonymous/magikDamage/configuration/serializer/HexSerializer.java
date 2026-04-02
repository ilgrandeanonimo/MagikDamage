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

public class HexSerializer implements Serializer<Integer, String> {
    @Override
    public String serialize(Integer integer) {
        return String.format("#%08x", integer);
    }

    @Override
    public Integer deserialize(String string) {
        String hex = string.substring(1);

        if (hex.length() == 6) {
            return 0xFF000000 | Integer.parseInt(hex, 16);
        } else if (hex.length() == 8) {
            return (int) Long.parseLong(hex, 16);
        } else {
            return 0xFF000000;
        }
    }
}
