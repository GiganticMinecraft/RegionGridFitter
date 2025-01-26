package click.seichi.regiongridfitter.listeners

import click.seichi.regiongridfitter.BypassState
import click.seichi.regiongridfitter.configuration.WorldGridSizeConfig
import click.seichi.regiongridfitter.event.CuboidSelectionUpdateEvent
import click.seichi.regiongridfitter.extensions.toSpans
import click.seichi.regiongridfitter.region.getQuantizedEnclosure
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector
import com.sk89q.worldedit.util.eventbus.EventHandler
import com.sk89q.worldedit.util.eventbus.Subscribe

fun <A: Comparable<A>, T: Comparable<T>> Pair<A, A>.alignTo(pair: Pair<T, T>): Pair<A, A> {
    val (max, min) = if (first >= second) (first to second) else (second to first)

    return if (pair.first > pair.second) {
        max to min
    } else {
        min to max
    }
}

private fun CuboidRegionSelector.gridEnclosure(gridSize: Int): CuboidRegionSelector {
    val (xSpan, _, zSpan) = toSpans()
    val (xGridSpan, zGridSpan) = listOf(xSpan, zSpan).map { it.getQuantizedEnclosure(gridSize) }

    val (pos1X, pos2X) = (xGridSpan.smallEnd to xGridSpan.largeEnd).alignTo(region.pos1.x to region.pos2.x)
    val (pos1Z, pos2Z) = (zGridSpan.smallEnd to zGridSpan.largeEnd).alignTo(region.pos1.z to region.pos2.z)

    val pos1 = BlockVector3.at(pos1X, 0.0, pos1Z)
    val pos2 = BlockVector3.at(pos2X, world!!.maxY.toDouble(), pos2Z)

    return CuboidRegionSelector(world, pos1, pos2)
}

class PlayerSelectionListener(private val gridSizeConfig: WorldGridSizeConfig,
                              private val bypassState: BypassState) {

    @Subscribe(priority = EventHandler.Priority.EARLY)
    fun onEditSession(event: CuboidSelectionUpdateEvent) {
        if (!bypassState.isSetToBypassFor(event.player)) {
            val proposedSelection = event.proposedSelection ?: return
            val selectionWorld = proposedSelection.world ?: return
            val gridSize = gridSizeConfig.gridSizeIn(BukkitAdapter.adapt(selectionWorld))

            event.proposedSelection = proposedSelection.gridEnclosure(gridSize)
        }
    }

}