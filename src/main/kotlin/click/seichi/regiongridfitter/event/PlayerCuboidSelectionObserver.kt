package click.seichi.regiongridfitter.event

import click.seichi.regiongridfitter.extensions.equalsWith
import click.seichi.regiongridfitter.extensions.selection
import click.seichi.regiongridfitter.util.filterNotNullValues
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.regions.CuboidRegion
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class PlayerCuboidSelectionObserver(private val server: Server, private val hostPlugin: JavaPlugin): Listener {

    private var selectionCache: Map<Player, CuboidRegion>
    private val worldEdit = WorldEdit.getInstance()
    private val eventBus = worldEdit.eventBus

    private fun Server.getPlayerSelections() =
        onlinePlayers.associate { it -> it!! to it.selection as? CuboidRegion }

    init {
        server.pluginManager.registerEvents(this, hostPlugin)
        selectionCache = server.getPlayerSelections().filterNotNullValues()
        observeState()
    }

    private fun notifySelectionDifference() = server
            .getPlayerSelections()
            .mapValues { (player, selection) -> selectionCache[player] to selection }
            .filterValues { (old, new) -> !(old equalsWith new) }
            .forEach { player, (_, newSelection) ->
                val updateEvent = CuboidSelectionUpdateEvent(player, newSelection)
                eventBus.post(updateEvent)
                player.selection = updateEvent.proposedSelection
            }

    private fun observeState() {
        notifySelectionDifference()
        selectionCache = server.getPlayerSelections().mapValues { (_, sel) -> sel?.clone() }.filterNotNullValues()

        server.scheduler.runTaskLater(hostPlugin, Runnable { observeState() }, 1L)
    }

}