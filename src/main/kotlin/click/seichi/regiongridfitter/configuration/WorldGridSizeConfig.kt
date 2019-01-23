package click.seichi.regiongridfitter.configuration

import click.seichi.regiongridfitter.util.filterNotNullValues
import org.bukkit.World
import org.bukkit.configuration.InvalidConfigurationException

interface WorldGridSizeConfig {

    fun gridSizeIn(world: World): Int

    companion object {
        operator fun invoke(worldGridSizeMap: Map<String, Any>, defaultSize: Int): WorldGridSizeConfig {
            if (defaultSize < 1) throw InvalidConfigurationException("Default grid size cannot be less than 1.")

            val filteredGridSizeMap: Map<String, Int> = worldGridSizeMap
                    .mapValues { entry -> entry.value as? Int }
                    .filterNotNullValues()

            return object : WorldGridSizeConfig {
                override fun gridSizeIn(world: World): Int = filteredGridSizeMap[world.name] ?: defaultSize
            }
        }
    }

}