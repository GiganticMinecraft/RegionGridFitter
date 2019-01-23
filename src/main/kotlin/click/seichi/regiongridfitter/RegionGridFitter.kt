package click.seichi.regiongridfitter

import click.seichi.regiongridfitter.configuration.WorldGridSizeConfig
import click.seichi.regiongridfitter.event.PlayerCuboidSelectionObserver
import click.seichi.regiongridfitter.command.ToggleCommandExecutor
import click.seichi.regiongridfitter.listeners.PlayerSelectionListener
import com.sk89q.worldedit.WorldEdit
import org.bukkit.plugin.java.JavaPlugin

class RegionGridFitter: JavaPlugin() {

    private val worldEditBus = WorldEdit.getInstance().eventBus

    private val worldGridSizeConfig = WorldGridSizeConfig(
            config.getConfigurationSection("grid_size")?.getValues(false) ?: HashMap(),
            config.getInt("default_grid_size")
    )

    private val bypassState = BypassState()
    private val bypassSettingsManager = ToggleCommandExecutor(bypassState)

    private val listeners = listOf(
            PlayerSelectionListener(worldGridSizeConfig, bypassState)
    )

    var observer: PlayerCuboidSelectionObserver? = null

    override fun onEnable() {
        saveDefaultConfig()

        observer = observer ?: PlayerCuboidSelectionObserver(server, this)

        getCommand("gridregion").executor = bypassSettingsManager

        listeners.forEach { worldEditBus.register(it) }
    }

    override fun onDisable() {
        listeners.forEach { worldEditBus.unregister(it) }
    }

}