package click.seichi.regiongridfitter.extensions

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.bukkit.WorldEditPlugin
import com.sk89q.worldedit.regions.Region
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector
import org.bukkit.Bukkit
import org.bukkit.entity.Player

private val worldEditPlugin by lazy {
    Bukkit.getPluginManager().getPlugin("WorldEdit") as WorldEditPlugin
}

var Player.selection: Region?
    get() {
        val localSession = worldEditPlugin.worldEdit.sessionManager.get(BukkitAdapter.adapt(this))
        val selectedWorld = localSession.selectionWorld

        // Pos1とPos2のどちらか片方だけ選択している場合に、LocalSession#getSelectionが例外を吐くので、その例外を無視する
        return if (selectedWorld != null) runCatching { localSession.getSelection(selectedWorld) }.fold(
                onSuccess = { it },
                onFailure = { null }
            )
        else null
    }
    set(newRegion) {
        if (newRegion == null) return

        val localSession = worldEditPlugin.worldEdit.sessionManager.get(BukkitAdapter.adapt(this))
        val newSelector = CuboidRegionSelector(newRegion.world, newRegion.minimumPoint, newRegion.maximumPoint)

        localSession.setRegionSelector(newRegion.world, newSelector)
        localSession.dispatchCUISelection(player)
    }
