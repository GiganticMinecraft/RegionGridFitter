package click.seichi.regiongridfitter.event

import com.sk89q.worldedit.bukkit.selections.CuboidSelection
import com.sk89q.worldedit.event.Cancellable
import com.sk89q.worldedit.event.Event
import org.bukkit.entity.Player

class CuboidSelectionUpdateEvent(val player: Player,
                                 val newSelection: CuboidSelection?) : Event(), Cancellable {

    var proposedSelection = newSelection

    private var cancelled = false

    override fun setCancelled(cancelled: Boolean) {
        this.cancelled = cancelled
    }

    override fun isCancelled() = this.cancelled

}