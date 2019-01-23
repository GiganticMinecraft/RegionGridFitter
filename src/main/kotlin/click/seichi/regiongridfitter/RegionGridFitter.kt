package click.seichi.regiongridfitter

import click.seichi.regiongridfitter.configuration.WorldGridSizeConfig
import click.seichi.regiongridfitter.event.PlayerCuboidSelectionObserver
import click.seichi.regiongridfitter.command.BypassState
import click.seichi.regiongridfitter.listeners.PlayerSelectionListener
import com.sk89q.worldedit.WorldEdit
import org.bukkit.plugin.java.JavaPlugin

class RegionGridFitter: JavaPlugin() {

    private val worldEditBus = WorldEdit.getInstance().eventBus

    private val bypassSettingsManager = BypassState()

    private val worldGridSizeConfig = WorldGridSizeConfig.const(15)

    private val listeners = listOf(
            PlayerSelectionListener(worldGridSizeConfig, bypassSettingsManager)
    )

    var observer: PlayerCuboidSelectionObserver? = null

    override fun onEnable() {
        observer = observer ?: PlayerCuboidSelectionObserver(server, this)

        getCommand("gridregion").executor = bypassSettingsManager

        listeners.forEach { worldEditBus.register(it) }
    }

    override fun onDisable() {
        listeners.forEach { worldEditBus.unregister(it) }
    }

}