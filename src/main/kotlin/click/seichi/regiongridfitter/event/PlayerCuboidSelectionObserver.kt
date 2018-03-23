package click.seichi.regiongridfitter.event

import click.seichi.regiongridfitter.extensions.selection
import click.seichi.regiongridfitter.region.clone
import click.seichi.regiongridfitter.region.equalsWith
import click.seichi.regiongridfitter.util.filterNotNullValues
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.selections.CuboidSelection
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class PlayerCuboidSelectionObserver(private val server: Server, private val hostPlugin: JavaPlugin): Listener {

    private var selectionCache: Map<Player, CuboidSelection>
    private val worldEdit = WorldEdit.getInstance()
    private val eventBus = worldEdit.eventBus

    private fun Server.getPlayerSelections() = onlinePlayers
            .map { it -> it!! to it.selection as? CuboidSelection }.toMap()

    init {
        server.pluginManager.registerEvents(this, hostPlugin)
        selectionCache = server.getPlayerSelections().filterNotNullValues()
        observeState()
    }

    private fun notifySelectionDifference() = server
            .getPlayerSelections()
            .mapValues { (player, selection) -> selectionCache[player] to selection }
            .filterValues { (old, new) -> old equalsWith new }
            .forEach { player, (old, new) ->
                val updateEvent = UpdatePlayerSelectionEvent(player, old, new)
                eventBus.post(updateEvent)
                player.selection = updateEvent.proposedSelection
            }

    private fun observeState() {
        notifySelectionDifference()
        selectionCache = server.getPlayerSelections().mapValues { (_, sel) -> sel?.clone() }.filterNotNullValues()

        server.scheduler.runTaskLater(hostPlugin, { observeState() }, 1L)
    }

}