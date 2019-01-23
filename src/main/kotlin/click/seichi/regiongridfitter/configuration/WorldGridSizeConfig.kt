package click.seichi.regiongridfitter.configuration

import org.bukkit.World

interface WorldGridSizeConfig {

    fun gridSizeIn(world: World): Int

    companion object {
        operator fun invoke(): WorldGridSizeConfig = TODO()

        fun const(size: Int) = object : WorldGridSizeConfig {
            override fun gridSizeIn(world: World) = size
        }
    }

}