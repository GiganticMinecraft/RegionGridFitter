package click.seichi.regiongridfitter.listeners

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BypassSettingsManager: CommandExecutor {

    private val bypassMap: MutableMap<Player, Boolean> = HashMap()

    fun isSetToBypass(player: Player): Boolean = bypassMap[player] ?: false

    private fun Player.notifySettingsChange(switchedToBypass: Boolean) =
            if (switchedToBypass) {
                sendMessage("${ChatColor.YELLOW}選択領域のグリッドフィット処理をバイパスします")
            } else {
                sendMessage("${ChatColor.GREEN}選択領域をグリッドにフィットします")
            }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as? Player ?: return sender.sendMessage("${ChatColor.RED}プレーヤーのみ使用できるコマンドです。").let { false }

        if (args.isNotEmpty() && args[0] == "toggle") {
            val current = isSetToBypass(player)
            val newState = !current

            if (player.hasPermission("gridregionfitter.togglebypass")) {
                bypassMap[player] = newState
                player.notifySettingsChange(newState)
            } else {
                player.sendMessage("${ChatColor.RED}パーミッションが不足しています。")
            }
        } else {
            player.sendMessage("${ChatColor.RED}コマンドが見つかりませんでした。")
        }

        return true
    }

}