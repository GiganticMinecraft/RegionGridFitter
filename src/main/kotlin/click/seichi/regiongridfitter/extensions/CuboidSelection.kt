package click.seichi.regiongridfitter.extensions

import com.sk89q.worldedit.regions.selector.CuboidRegionSelector

infix fun CuboidRegionSelector?.equalsWith(another: CuboidRegionSelector?) =
        when (this) {
            null -> another == null
            else -> another != null &&
                    region.maximumPoint == another.region.maximumPoint &&
                    region.minimumPoint == another.region.minimumPoint &&
                    world == another.world
        }

fun CuboidRegionSelector.clone() = CuboidRegionSelector(this)
