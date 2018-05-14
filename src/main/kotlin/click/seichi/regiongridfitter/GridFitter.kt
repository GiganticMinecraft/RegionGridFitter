package click.seichi.regiongridfitter

import click.seichi.regiongridfitter.event.UpdatePlayerSelectionEvent
import click.seichi.regiongridfitter.extensions.toSpans
import click.seichi.regiongridfitter.region.RealSpan
import click.seichi.regiongridfitter.region.getQuantizedEnclosure
import com.sk89q.worldedit.Vector
import com.sk89q.worldedit.bukkit.selections.CuboidSelection
import com.sk89q.worldedit.bukkit.selections.Selection
import com.sk89q.worldedit.util.eventbus.EventHandler
import com.sk89q.worldedit.util.eventbus.Subscribe
import org.bukkit.World

/**
 * プレーヤーの選択領域が変更されたときにグリッドに強制フィットするようなリスナ
 */
class GridFitter(private val gridSize: Int) {

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
    fun onEditSession(event: UpdatePlayerSelectionEvent) {
        val newSelection = event.newSelection?.fitToGrid(gridSize) ?: return

        event.proposedSelection = newSelection
    }

}