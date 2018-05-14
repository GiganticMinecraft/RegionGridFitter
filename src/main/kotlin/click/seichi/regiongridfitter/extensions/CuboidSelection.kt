package click.seichi.regiongridfitter.extensions

import com.sk89q.worldedit.bukkit.selections.CuboidSelection

infix fun CuboidSelection?.equalsWith(another: CuboidSelection?) =
        when (this) {
            null -> another == null
            else -> another != null &&
                    maximumPoint == another.maximumPoint &&
                    minimumPoint == another.minimumPoint &&
                    world == another.world
        }

fun CuboidSelection.clone() = CuboidSelection(world, minimumPoint, maximumPoint)
