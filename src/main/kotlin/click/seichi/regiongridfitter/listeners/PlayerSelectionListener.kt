package click.seichi.regiongridfitter.listeners

import click.seichi.regiongridfitter.BypassState
import click.seichi.regiongridfitter.configuration.WorldGridSizeConfig
import click.seichi.regiongridfitter.event.CuboidSelectionUpdateEvent
import click.seichi.regiongridfitter.extensions.toSpans
import click.seichi.regiongridfitter.region.RealSpan
import click.seichi.regiongridfitter.region.getQuantizedEnclosure
import com.sk89q.worldedit.Vector
import com.sk89q.worldedit.bukkit.selections.CuboidSelection
import com.sk89q.worldedit.bukkit.selections.Selection
import com.sk89q.worldedit.util.eventbus.EventHandler
import com.sk89q.worldedit.util.eventbus.Subscribe
import org.bukkit.World

class PlayerSelectionListener(private val gridSizeConfig: WorldGridSizeConfig,
                              private val bypassState: BypassState) {

    private fun toVertExtendedSelection(world: World, xSpan: RealSpan, zSpan: RealSpan): CuboidSelection {
        val (minX, maxX) = Pair(xSpan.smallEnd, xSpan.largeEnd)
        val (minZ, maxZ) = Pair(zSpan.smallEnd, zSpan.largeEnd)

        val start = Vector(minX, 0.0, minZ)
        val end = Vector(maxX, 256.0, maxZ)

        return CuboidSelection(world, start, end)
    }

    private fun Selection.fitToGrid(gridSize: Int): CuboidSelection {
        val (xSpan, _, zSpan) = toSpans()
        val (xGridSpan, zGridSpan) = listOf(xSpan, zSpan).map { it.getQuantizedEnclosure(gridSize) }

        return toVertExtendedSelection(world!!, xGridSpan, zGridSpan)
    }

    @Subscribe(priority = EventHandler.Priority.EARLY)
    fun onEditSession(event: CuboidSelectionUpdateEvent) {
        if (!bypassState.isSetToBypassFor(event.player)) {
            val proposedSelection = event.proposedSelection ?: return
            val selectionWorld = proposedSelection.world ?: return
            val gridSize = gridSizeConfig.gridSizeIn(selectionWorld)

            event.proposedSelection = proposedSelection.fitToGrid(gridSize)
        }
    }

}