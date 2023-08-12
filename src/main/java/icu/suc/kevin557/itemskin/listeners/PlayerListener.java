package icu.suc.kevin557.itemskin.listeners;

import icu.suc.kevin557.itemskin.ItemSkin;
import icu.suc.kevin557.itemskin.events.SelectSkinEvent;
import icu.suc.kevin557.itemskin.skin.Skin;
import icu.suc.kevin557.itemskin.skin.SkinGroup;
import icu.suc.kevin557.itemskin.utils.data.MaterialData;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener
{
    @EventHandler
    public void onSelectSkin(final SelectSkinEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }

        OfflinePlayer offlinePlayer = event.getPlayer();
        SkinGroup group = event.getGroup();
        Skin skin = event.getSkin();

        ItemSkin.getInstance().getPlayerManager().getGroupSections(offlinePlayer).put(group, skin);

        Player player = offlinePlayer.getPlayer();

        if (player == null)
        {
            return;
        }

        for (ItemStack itemStack : player.getInventory())
        {
            onSelectSkin(itemStack, group, skin);
        }

        for (ItemStack itemStack : player.getInventory().getArmorContents())
        {
            onSelectSkin(itemStack, group, skin);
        }
    }

    private void onSelectSkin(ItemStack itemStack, SkinGroup group, Skin skin)
    {
        if (itemStack == null || itemStack.getType() == Material.AIR)
        {
            return;
        }

        MaterialData materialData = new MaterialData(itemStack.getType(), itemStack.getDurability());

        SkinGroup skinGroup = ItemSkin.getInstance().getSkinManager().getMaterialTable().get(materialData);

        MaterialData skinMaterialData = skin.getMaterialData();

        if (skinGroup == null || !skinGroup.equals(group) || skinMaterialData.equals(materialData))
        {
            return;
        }

        itemStack.setType(skinMaterialData.getType());
        itemStack.setDurability(skinMaterialData.getData());
    }

    @EventHandler
    public void onPlayerPickupItem(final PlayerPickupItemEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }

        Item item = event.getItem();

        ItemStack itemStack = item.getItemStack();

        MaterialData materialData = new MaterialData(itemStack.getType(), itemStack.getDurability());

        SkinGroup group = ItemSkin.getInstance().getSkinManager().getMaterialTable().get(materialData);

        if (group == null)
        {
            return;
        }

        Skin skin = ItemSkin.getInstance().getPlayerManager().getSelectedSkin(event.getPlayer(), group);
        MaterialData skinMaterialData = skin.getMaterialData();

        if (skinMaterialData.equals(materialData))
        {
            return;
        }

        itemStack.setType(skinMaterialData.getType());
        itemStack.setDurability(skinMaterialData.getData());

        item.setItemStack(itemStack);
    }

    @EventHandler
    public void onPlayerDropItem(final PlayerDropItemEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }

        Item drop = event.getItemDrop();

        ItemStack itemStack = drop.getItemStack();

        MaterialData materialData = new MaterialData(itemStack.getType(), itemStack.getDurability());

        SkinGroup group = ItemSkin.getInstance().getSkinManager().getMaterialTable().get(materialData);

        if (group == null)
        {
            return;
        }

        Skin skin = group.getDefaultSkin();
        MaterialData skinMaterialData = skin.getMaterialData();

        if (skinMaterialData.equals(materialData))
        {
            return;
        }

        itemStack.setType(skinMaterialData.getType());
        itemStack.setDurability(skinMaterialData.getData());

        drop.setItemStack(itemStack);
    }
}
