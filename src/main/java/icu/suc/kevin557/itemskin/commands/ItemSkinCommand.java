package icu.suc.kevin557.itemskin.commands;

import icu.suc.kevin557.itemskin.ItemSkin;
import icu.suc.kevin557.itemskin.configs.I18n;
import icu.suc.kevin557.itemskin.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ItemSkinCommand extends AbstractCommand
{
    private static final String RELOAD = "reload";
    private static final List<String> ARGS = Collections.singletonList(RELOAD);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 1)
        {
            ChatUtils.sendMessage(sender, I18n.format("version", ItemSkin.getInstance().getDescription().getVersion()));

            return true;
        }

        if ("reload".equals(args[0].toLowerCase(Locale.ENGLISH)))
        {
            ChatUtils.sendMessage(sender, I18n.format("reloadStart"));
            ItemSkin.getInstance().reloadConfig();
            ItemSkin.getInstance().onEnable();
            ChatUtils.sendMessage(sender, I18n.format("reloadEnd"));
        }
        else
        {
            ChatUtils.sendMessage(sender, I18n.format("version"));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        if (args.length < 1)
        {
            return ARGS;
        }
        else if (args.length == 1 && RELOAD.startsWith(args[0]))
        {
            return ARGS;
        }

        return null;
    }
}
