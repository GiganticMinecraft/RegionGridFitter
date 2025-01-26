package click.seichi.regiongridfitter.extensions

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.bukkit.WorldEditPlugin
import com.sk89q.worldedit.regions.RegionSelector
import org.bukkit.Bukkit
import org.bukkit.entity.Player

private val worldEditPlugin by lazy {
    Bukkit.getPluginManager().getPlugin("WorldEdit") as WorldEditPlugin
}

var Player.selector: RegionSelector?
    get() {
        val localSession = worldEditPlugin.worldEdit.sessionManager.get(BukkitAdapter.adapt(this))

        return localSession.getRegionSelector(localSession.selectionWorld)
    }
    set(newSelector) =
        if (newSelector != null) worldEditPlugin.worldEdit.sessionManager.get(BukkitAdapter.adapt(this)).setRegionSelector(newSelector.world, newSelector)
        else Unit
