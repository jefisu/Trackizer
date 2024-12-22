package com.jefisu.trackizer.build

import org.gradle.api.artifacts.VersionCatalog

fun VersionCatalog.getPluginId(alias: String): String {
    return findPlugin(alias).get().get().pluginId
}