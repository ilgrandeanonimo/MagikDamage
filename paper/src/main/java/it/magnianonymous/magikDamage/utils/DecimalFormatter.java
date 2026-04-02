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

package it.magnianonymous.magikDamage.utils;

import it.magnianonymous.magikDamage.configuration.Settings;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.concurrent.ConcurrentHashMap;

public final class DecimalFormatter {
    private static final ConcurrentHashMap<String, DecimalFormat> formats;
    private static final DecimalFormat fallback;

    public static void loadFormats(Settings settings) {
        if (!formats.isEmpty()) formats.clear();
        settings.getFormats()
            .forEach(DecimalFormatter::registerFormat);
    }

    public static void registerFormat(String name, Settings.Format format) {
        final var symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(format.decimalsSeparator());
        symbols.setGroupingSeparator(format.groupSeparator());
        final var decimalFormat = new DecimalFormat(format.format(), symbols);
        formats.put(name, decimalFormat);
    }

    public static String format(String format, double number) {
        return formats.getOrDefault(format, fallback)
            .format(number);
    }

    static {
        formats = new ConcurrentHashMap<>();
        fallback = new DecimalFormat();
    }
}