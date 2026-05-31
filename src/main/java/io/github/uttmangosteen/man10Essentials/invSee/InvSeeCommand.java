package io.github.uttmangosteen.man10Essentials.invSee;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InvSeeCommand implements CommandExecutor {

    private final InvSeeManager invSeeManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage("§プレイヤーのみしか実行できません.");
            return false;
        }

        if(args.length == 0) {
            sender.sendMessage("§c/invsee <player>");
            return false;
        }

        Player player= Bukkit.getPlayer(args[0]);

        if(player == null){
            sender.sendMessage("§c"+args[0]+"はオフラインです.");
            return false;
        }

        invSeeManager.createNewInvSee((Player)sender,player);

        return true;
    }

    InvSeeCommand(InvSeeManager invSeeManager){
        this.invSeeManager = invSeeManager;
    }


}
