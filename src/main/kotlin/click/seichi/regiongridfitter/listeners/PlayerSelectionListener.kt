package click.seichi.regiongridfitter.listeners

import click.seichi.regiongridfitter.BypassState
import click.seichi.regiongridfitter.configuration.WorldGridSizeConfig
import click.seichi.regiongridfitter.event.CuboidSelectionUpdateEvent
import click.seichi.regiongridfitter.extensions.toSpans
import click.seichi.regiongridfitter.region.getQuantizedEnclosure
import com.sk89q.worldedit.Vector
import com.sk89q.worldedit.bukkit.selections.CuboidSelection
import com.sk89q.worldedit.regions.CuboidRegion
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

private fun CuboidSelection.gridEnclosure(gridSize: Int): CuboidSelection {
    val (xSpan, _, zSpan) = toSpans()
    val (xGridSpan, zGridSpan) = listOf(xSpan, zSpan).map { it.getQuantizedEnclosure(gridSize) }

    val selectedRegion: CuboidRegion = (regionSelector as CuboidRegionSelector).region

    val (pos1X, pos2X) = (xGridSpan.smallEnd to xGridSpan.largeEnd).alignTo(selectedRegion.pos1.x to selectedRegion.pos2.x)
    val (pos1Z, pos2Z) = (zGridSpan.smallEnd to zGridSpan.largeEnd).alignTo(selectedRegion.pos1.z to selectedRegion.pos2.z)

    val pos1 = Vector(pos1X, 0.0, pos1Z)
    val pos2 = Vector(pos2X, world!!.maxHeight.toDouble(), pos2Z)

    return CuboidSelection(world, pos1, pos2)
}

class PlayerSelectionListener(private val gridSizeConfig: WorldGridSizeConfig,
                              private val bypassState: BypassState) {

    @Subscribe(priority = EventHandler.Priority.EARLY)
    fun onEditSession(event: CuboidSelectionUpdateEvent) {
        if (!bypassState.isSetToBypassFor(event.player)) {
            val proposedSelection = event.proposedSelection ?: return
            val selectionWorld = proposedSelection.world ?: return
            val gridSize = gridSizeConfig.gridSizeIn(selectionWorld)

            event.proposedSelection = proposedSelection.gridEnclosure(gridSize)
        }
    }

}