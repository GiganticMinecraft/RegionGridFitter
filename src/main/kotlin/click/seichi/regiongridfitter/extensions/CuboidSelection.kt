package click.seichi.regiongridfitter.extensions

import com.sk89q.worldedit.regions.CuboidRegion

infix fun CuboidRegion?.equalsWith(another: CuboidRegion?) =
        when (this) {
            null -> another == null
            else -> another != null &&
                    world == another.world &&
                    maximumPoint == another.maximumPoint &&
                    minimumPoint == another.minimumPoint
        }
