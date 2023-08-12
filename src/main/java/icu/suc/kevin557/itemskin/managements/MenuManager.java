package icu.suc.kevin557.itemskin.managements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import icu.suc.kevin557.itemskin.ItemSkin;
import icu.suc.kevin557.itemskin.configs.I18n;
import icu.suc.kevin557.itemskin.skin.Skin;
import icu.suc.kevin557.itemskin.skin.SkinGroup;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MenuManager
{
    public final static int[] DISABLED_SLOTS = {
            0,  1,  2,  3,  4,  5,  6,  7,  8,
            9,                             17,
           18,                             26,
           27,                             35,
               37, 38, 39,     41, 42, 43
    };
    public final static int LEFT_SLOT = 36;
    public final static int RIGHT_SLOT = 44;
    public final static int REST_SLOT = 40;
    private final static int MAX = 21;

    private final Map<Inventory, Context> inventories;

    public MenuManager()
    {
        this.inventories = Maps.newHashMap();
    }

    public void openMenu(Player player, SkinGroup group, int page)
    {
        Map<String, Skin> skins = group.getSkins();
        int pages = Math.max((int) Math.ceil(skins.size() / (double) MAX), 1);

        if (page < 1 || page > pages)
        {
            return;
        }

        Inventory inventory = Bukkit.createInventory(player, 45, I18n.format("skinMenu", group.getName(), page, pages));
        Context context = new Context(group, page);

        for (int slot : MenuManager.DISABLED_SLOTS)
        {
            inventory.setItem(slot, getDisabledIcon());
        }

        inventory.setItem(MenuManager.LEFT_SLOT, page > 1 ? getLeftArrow() : getDisabledIcon());
        inventory.setItem(MenuManager.RIGHT_SLOT, page < pages ? getRightArrow() : getDisabledIcon());
        inventory.setItem(MenuManager.REST_SLOT, getRestButton());

        int start = (page - 1) * MAX;
        int end = Math.min(page * MAX, skins.size());

        Iterator<Skin> iterator = skins.values().iterator();
        int index = 0;
        while (iterator.hasNext() && index <= end)
        {
            Skin skin = iterator.next();

            if (index >= start && index <= end)
            {
                inventory.addItem(getSkinIcon(player, group, skin));
                context.getSkins().add(skin);
            }

            index++;
        }

        inventories.put(inventory, context);
        player.openInventory(inventory);
    }

    private ItemStack getSkinIcon(Player player, SkinGroup group, Skin skin)
    {
        ItemSkin itemSkin = ItemSkin.getInstance();
        PlayerManager playerManager = itemSkin.getPlayerManager();

        ItemStack itemStack = new ItemStack(skin.getMaterialData().getType());
        itemStack.setDurability(skin.getMaterialData().getData());
        ItemMeta itemMeta = itemStack.getItemMeta();

        String skinName = skin.getName();
        if (skinName != null)
        {
            itemMeta.setDisplayName(ChatColor.RESET + skinName);
        }

        List<String> lore = new ArrayList<>();
        lore.add("");

        if (playerManager.getSelectedSkin(player, group) == skin)
        {
            lore.add(ChatColor.RESET + I18n.format("selected"));
        }
        else if (playerManager.hasSkin(player, group, skin))
        {
            lore.add(ChatColor.RESET + I18n.format("select"));
        }
        else
        {
            Double price = skin.getPrice();

            if (price == null)
            {
                lore.add(ChatColor.RESET + I18n.format("locked"));
            }
            else
            {
                Economy economy = itemSkin.getEconomy();
                double balance = economy.getBalance(player);

                if (balance < price)
                {
                    lore.add(ChatColor.RESET + I18n.format("unlockUnavailable", economy.format(price)));
                    lore.add(ChatColor.RESET + I18n.format("require", economy.format(price - balance)));
                }
                else
                {
                    lore.add(ChatColor.RESET + I18n.format("unlockAvailable", economy.format(price)));
                }
            }
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack getDisabledIcon()
    {
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getLeftArrow()
    {
        ItemStack itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + I18n.format("prePage"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getRightArrow()
    {
        ItemStack itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + I18n.format("nextPage"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getRestButton()
    {
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + I18n.format("resetSkin"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public Map<Inventory, Context> getInventories()
    {
        return inventories;
    }

    public static class Context
    {
        private final SkinGroup group;
        private final int page;
        private final List<Skin> skins;

        public Context(SkinGroup group, int page)
        {
            this.group = group;
            this.page = page;
            this.skins = Lists.newArrayList();
        }

        public SkinGroup getGroup()
        {
            return group;
        }

        public int getPage()
        {
            return page;
        }

        public List<Skin> getSkins()
        {
            return skins;
        }
    }
}
