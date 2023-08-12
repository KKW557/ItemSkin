package icu.suc.kevin557.itemskin.commands;

import com.google.common.collect.Lists;
import icu.suc.kevin557.itemskin.ItemSkin;
import icu.suc.kevin557.itemskin.configs.I18n;
import icu.suc.kevin557.itemskin.skin.SkinGroup;
import icu.suc.kevin557.itemskin.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class SkinMenuCommand extends AbstractCommand
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            ChatUtils.sendMessage(sender, "Only player!");
            return true;
        }

        if (args.length == 1)
        {
            Player player = (Player) sender;

            ItemSkin itemSkin = ItemSkin.getInstance();

            SkinGroup group = itemSkin.getSkinManager().getSkinGroups().get(args[0]);

            if (group == null)
            {
                ChatUtils.sendMessage(player, I18n.format("illegalGroup", args[0]));
            }
            else
            {
                itemSkin.getMenuManager().openMenu(player, group, 1);
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> results = Lists.newArrayList();
        Set<String> nameSet = ItemSkin.getInstance().getSkinManager().getSkinGroups().keySet();

        if (args.length < 1)
        {
            results.addAll(nameSet);
        }
        else if (args.length == 1)
        {
            for (String name : nameSet)
            {
                if (name.startsWith(args[0]))
                {
                    results.add(name);
                }
            }
        }

        return results;
    }
}
