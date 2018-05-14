package click.seichi.regiongridfitter

import click.seichi.regiongridfitter.event.PlayerCuboidSelectionObserver
import click.seichi.regiongridfitter.listeners.BypassSettingsManager
import click.seichi.regiongridfitter.listeners.GridFitter
import com.sk89q.worldedit.WorldEdit
import org.bukkit.plugin.java.JavaPlugin

class RegionGridFitter: JavaPlugin() {

    private val worldEditBus = WorldEdit.getInstance().eventBus

    private val bypassSettingsManager = BypassSettingsManager()
    private val listener = GridFitter(15, bypassSettingsManager)

    var observer: PlayerCuboidSelectionObserver? = null

    override fun onEnable() {
        observer = observer ?: PlayerCuboidSelectionObserver(server, this)

        getCommand("gridregion").executor = bypassSettingsManager

        worldEditBus.register(listener)
    }

    override fun onDisable() {
        worldEditBus.unregister(listener)
    }

}