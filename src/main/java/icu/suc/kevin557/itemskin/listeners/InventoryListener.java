package icu.suc.kevin557.itemskin.listeners;

import icu.suc.kevin557.itemskin.ItemSkin;
import icu.suc.kevin557.itemskin.configs.I18n;
import icu.suc.kevin557.itemskin.managements.MenuManager;
import icu.suc.kevin557.itemskin.skin.Skin;
import icu.suc.kevin557.itemskin.utils.ChatUtils;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Locale;

public class InventoryListener implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(final InventoryClickEvent event)
    {
        Inventory inventory = event.getClickedInventory();

        if (ItemSkin.getInstance().getMenuManager().getInventories().containsKey(inventory))
        {
            Player player = (Player) inventory.getHolder();
            int slot = event.getRawSlot();

            MenuManager.Context context = ItemSkin.getInstance().getMenuManager().getInventories().get(inventory);

            if ((slot == MenuManager.LEFT_SLOT || slot == MenuManager.REST_SLOT) && event.getView().getItem(slot).getType() == Material.ARROW)
            {
                int page = context.getPage() + (slot == MenuManager.LEFT_SLOT ? -1 : 1);

                ItemSkin.getInstance().getMenuManager().getInventories().remove(inventory);
                ItemSkin.getInstance().getMenuManager().openMenu(player, context.getGroup(), page);
            }
            else if (slot == MenuManager.REST_SLOT)
            {
                ItemSkin.getInstance().getPlayerManager().resetSkin(player, context.getGroup());
                ChatUtils.sendMessage(player, I18n.format("resetSuccess"));
                ItemSkin.getInstance().getMenuManager().getInventories().remove(inventory);
                ItemSkin.getInstance().getMenuManager().openMenu(player, context.getGroup(), context.getPage());
            }
            else if (!ArrayUtils.contains(MenuManager.DISABLED_SLOTS, slot))
            {
                int index = 0;

                if (slot > 27)
                {
                    index = slot - 28;
                }
                else if (slot > 18)
                {
                    index = slot - 19;
                }
                else if (slot > 9)
                {
                    index = slot - 10;
                }

                List<Skin> skins = context.getSkins();
                if (index < skins.size())
                {
                    Skin skin = skins.get(index);

                    String skinName = skin.getName();
                    if (skinName == null)
                    {
                        skinName = skin.getMaterialData().getType().name().replaceAll("_", " ").toLowerCase(Locale.ENGLISH) + " " + skin.getMaterialData().getData();
                    }

                    if (ItemSkin.getInstance().getPlayerManager().hasSkin(player, context.getGroup(), skin))
                    {
                        if (!ItemSkin.getInstance().getPlayerManager().getSelectedSkin(player, context.getGroup()).equals(skin))
                        {
                            ItemSkin.getInstance().getPlayerManager().setSelectedSkinAndCall(player, context.getGroup(), skin);
                            ChatUtils.sendMessage(player, I18n.format("selectSuccess", skinName));
                        }
                    }
                    else
                    {
                        Double price = skin.getPrice();

                        if (price == null)
                        {
                            ChatUtils.sendMessage(player, I18n.format("notHave", skinName));
                        }
                        else
                        {
                            Economy economy = ItemSkin.getInstance().getEconomy();
                            double balance = economy.getBalance(player);

                            if (balance < price)
                            {
                                ChatUtils.sendMessage(player, I18n.format("notEnough", skinName));
                            }
                            else
                            {
                                economy.withdrawPlayer(player, price);
                                ItemSkin.getInstance().getPlayerManager().addSkin(player, context.getGroup(), skin);
                                ChatUtils.sendMessage(player, I18n.format("gotSkin", economy.format(price), skinName));
                                ItemSkin.getInstance().getPlayerManager().setSelectedSkinAndCall(player, context.getGroup(), skin);
                                ChatUtils.sendMessage(player, I18n.format("selectSuccess", skinName));
                            }
                        }
                    }

                    ItemSkin.getInstance().getMenuManager().getInventories().remove(inventory);
                    ItemSkin.getInstance().getMenuManager().openMenu(player, context.getGroup(), context.getPage());
                }
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event)
    {
        ItemSkin.getInstance().getMenuManager().getInventories().remove(event.getInventory());
    }
}
