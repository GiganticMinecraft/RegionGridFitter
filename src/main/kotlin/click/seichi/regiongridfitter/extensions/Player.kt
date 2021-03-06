package click.seichi.regiongridfitter.extensions

import com.sk89q.worldedit.bukkit.WorldEditPlugin
import com.sk89q.worldedit.bukkit.selections.Selection
import org.bukkit.Bukkit
import org.bukkit.entity.Player

private val worldEditPlugin by lazy {
    Bukkit.getPluginManager().getPlugin("WorldEdit") as WorldEditPlugin
}

var Player.selection: Selection?
    get() = worldEditPlugin.getSelection(this)
    set(selection) = if (selection != null) worldEditPlugin.setSelection(this, selection) else Unit
