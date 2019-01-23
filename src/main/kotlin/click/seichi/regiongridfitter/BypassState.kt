package click.seichi.regiongridfitter

import org.bukkit.ChatColor
import org.bukkit.entity.Player

class BypassState {

    private val bypassMap: MutableMap<Player, Boolean> = HashMap()

    fun isSetToBypassFor(player: Player): Boolean = bypassMap[player] ?: false

    private fun notifyStateChangeTo(player: Player, switchedToBypass: Boolean) {
        val message = if (switchedToBypass) {
            "${ChatColor.YELLOW}選択領域のグリッドフィット処理をバイパスします"
        } else {
            "${ChatColor.GREEN}選択領域をグリッドにフィットします"
        }
        player.sendMessage(message)
    }

    fun toggleAndNotifyChangeTo(player: Player) {
        val current = isSetToBypassFor(player)
        val newState = !current

        bypassMap[player] = newState
        notifyStateChangeTo(player, newState)
    }

}