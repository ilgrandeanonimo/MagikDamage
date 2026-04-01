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

package it.magnianonymous.magikDamage.core;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.configuration.PluginMeta;
import it.magnianonymous.magikDamage.MagikDamage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

@SuppressWarnings({"SameReturnValue"})
public final class MagikCommand {
    private final static MagikDamage plugin = MagikDamage.getInstance();
    private final static Component modFancyName = MiniMessage.miniMessage()
        .deserialize("<gradient:#C81023:#E27505:#E2C001:#A1B513>MagikDamage");

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("magikdamage")
            .requires(source -> source.getSender()
                .hasPermission("magikdamage.op"))
            .then(Commands.literal("about")
                .executes(MagikCommand::about))
            .then(Commands.literal("reload")
                .executes(MagikCommand::reload));
    }

    private static int about(CommandContext<CommandSourceStack> context) {
        final CommandSender sender = context.getSource().getSender();
        final PluginMeta pluginMeta = plugin.getPluginMeta();
        sender.sendMessage(modFancyName.append(
            Component.space(),
            Component.text(pluginMeta.getVersion())
                .append(
                    Component.space(),
                    Component.text("by"),
                    Component.space(),
                    Component.text("IlGrandeAnonimo", NamedTextColor.YELLOW)
                )
        ));
        return Command.SINGLE_SUCCESS;
    }

    private static int reload(CommandContext<CommandSourceStack> context) {
        final CommandSender sender = context.getSource().getSender();
        plugin.reload();
        sender.sendMessage(modFancyName.append(
            Component.space(),
            Component.text("Configuration reloaded", NamedTextColor.GREEN)
        ));
        return Command.SINGLE_SUCCESS;
    }
}
