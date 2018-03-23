package click.seichi.regiongridfitter

import click.seichi.regiongridfitter.event.UpdatePlayerSelectionEvent
import click.seichi.regiongridfitter.region.fitToGrid
import com.sk89q.worldedit.util.eventbus.EventHandler
import com.sk89q.worldedit.util.eventbus.Subscribe

class GridFitter(private val gridSize: Int) {

    @Subscribe(priority = EventHandler.Priority.EARLY)
    fun onEditSession(event: UpdatePlayerSelectionEvent) {
        val newSelection = event.newSelection?.fitToGrid(gridSize) ?: return

        event.proposedSelection = newSelection
    }

}