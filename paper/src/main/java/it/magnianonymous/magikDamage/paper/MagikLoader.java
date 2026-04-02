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

package it.magnianonymous.magikDamage.paper;

import de.exlll.configlib.Configuration;
import de.exlll.configlib.YamlConfigurations;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import lombok.NoArgsConstructor;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"UnstableApiUsage","unused","FieldMayBeFinal"})
public final class MagikLoader implements PluginLoader {
    @Nullable
    private InputStream getStream() {
        return MagikLoader.class
            .getClassLoader()
            .getResourceAsStream("paper-libraries.yml");
    }

    private PaperLibraries getPaperLibraries() {
        try(InputStream inputStream = getStream()) {
            return YamlConfigurations.read(
                Optional.ofNullable(inputStream).orElseThrow(),
                PaperLibraries.class
            );
        } catch (IOException e) {
            return new PaperLibraries();
        }
    }

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        final MavenLibraryResolver resolver = new MavenLibraryResolver();
        PaperLibraries paperLibraries = getPaperLibraries();
        paperLibraries.repositories.forEach((id, url) ->
            resolver.addRepository(new RemoteRepository.Builder(
                id, "default", url
            ).build())
        );
        paperLibraries.libraries.forEach(library ->
            resolver.addDependency(new Dependency(
                new DefaultArtifact(library), null
            ))
        );
        classpathBuilder.addLibrary(resolver);
    }

    @Configuration
    @NoArgsConstructor
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static class PaperLibraries {
        private HashMap<String, String> repositories;
        private List<String> libraries;
    }
}
