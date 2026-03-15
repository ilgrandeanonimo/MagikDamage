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

package it.magnianonymous.magikDamage.configuration;

import de.exlll.configlib.Configuration;
import it.magnianonymous.magikDamage.Indicator;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Configuration
@SuppressWarnings("FieldMayBeFinal")
public class Settings {
    private Map<String, Format> formats = Map.of(
        "european", new Format(
            "#,###.##",
            ',',
            '.',
            true,
            true
        ),
        "imperial", new Format(
            "#,###.##",
            '.',
            ',',
            true,
            true
        )
    );

    private Map<String, Indicator> indicators = Map.of(
        "default", new Indicator(
            "european",
            new HashMap<>(),
            new Indicator.Display(
                0x00000000,
                0xFFFFFFFF,
                true,
                true,
                true
            )
        )
    );

    public record Format(
        String format,
        char decimalsSeparator,
        char groupSeparator,
        boolean positivesSign,
        boolean negativesSign
    ) {}

    public static String HEADER = """
        MagikDamage by IlGrandeAnonimo
        settings.yml
        """;
}
