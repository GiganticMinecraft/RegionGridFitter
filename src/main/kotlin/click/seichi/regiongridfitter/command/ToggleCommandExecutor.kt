package click.seichi.regiongridfitter.command

import click.seichi.regiongridfitter.BypassState
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ToggleCommandExecutor(private val bypassState: BypassState): CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as? Player ?: return sender.sendMessage("${ChatColor.RED}プレーヤーのみ使用できるコマンドです。").let { false }

        if (args.isNotEmpty() && args[0] == "toggle") {
            if (player.hasPermission("gridregionfitter.togglebypass")) {
                bypassState.toggleAndNotifyChangeTo(player)
            } else {
                player.sendMessage("${ChatColor.RED}パーミッションが不足しています。")
            }
        } else {
            player.sendMessage("${ChatColor.RED}コマンドが見つかりませんでした。")
        }

        return true
    }

}