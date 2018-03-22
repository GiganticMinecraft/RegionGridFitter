package click.seichi.regiongridfitter.event

import com.sk89q.worldedit.bukkit.selections.Selection
import com.sk89q.worldedit.event.Cancellable
import com.sk89q.worldedit.event.Event
import org.bukkit.entity.Player

class UpdatePlayerSelectionEvent(val player: Player,
                                 val oldSelection: Selection?,
                                 var newSelection: Selection?) : Event(), Cancellable {

    private var cancelled = false

    override fun setCancelled(cancelled: Boolean) {
        this.cancelled = cancelled
    }

    override fun isCancelled() = this.cancelled

}