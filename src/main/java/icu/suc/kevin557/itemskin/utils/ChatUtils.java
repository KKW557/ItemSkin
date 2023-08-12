package icu.suc.kevin557.itemskin.utils;

import icu.suc.kevin557.itemskin.configs.I18n;
import org.bukkit.command.CommandSender;

public class ChatUtils
{
    public static void sendMessage(CommandSender receiver, String message)
    {
        receiver.sendMessage(I18n.format("prefix") + message);
    }
}
