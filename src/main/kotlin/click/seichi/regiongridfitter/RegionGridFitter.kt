package click.seichi.regiongridfitter

import click.seichi.regiongridfitter.event.PlayerCuboidSelectionObserver
import com.sk89q.worldedit.WorldEdit
import org.bukkit.plugin.java.JavaPlugin

class RegionGridFitter: JavaPlugin() {

    private val worldEditBus = WorldEdit.getInstance().eventBus
    private val listener = GridFitter(15)

    var observer: PlayerCuboidSelectionObserver? = null

    override fun onEnable() {
        observer = observer ?: PlayerCuboidSelectionObserver(server, this)

        worldEditBus.register(listener)
    }

    override fun onDisable() {
        worldEditBus.unregister(listener)
    }

}