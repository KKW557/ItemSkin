package icu.suc.kevin557.itemskin.listeners;

import icu.suc.kevin557.itemskin.ItemSkin;
import icu.suc.kevin557.itemskin.configs.I18n;
import icu.suc.kevin557.itemskin.events.SelectSkinEvent;
import icu.suc.kevin557.itemskin.skin.Skin;
import icu.suc.kevin557.itemskin.skin.SkinGroup;
import icu.suc.kevin557.itemskin.utils.ChatUtils;
import icu.suc.kevin557.itemskin.utils.data.MaterialData;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
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

        Player player = event.getPlayer().getPlayer();

        if (player == null)
        {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE && ItemSkin.getInstance().settings.disableInCreative)
        {
            ChatUtils.sendMessage(player, I18n.format("disableInCreative"));
            return;
        }

        SkinGroup group = event.getGroup();
        Skin skin = event.getSkin();

        ItemSkin.getInstance().getPlayerManager().setSelectedSkin(player, group, skin);

        for (ItemStack itemStack : player.getInventory())
        {
            applySkin(itemStack, group, skin);
        }

        for (ItemStack itemStack : player.getInventory().getArmorContents())
        {
            applySkin(itemStack, group, skin);
        }

        ItemSkin.getInstance().getPlayerManager().save(player);
    }

    private void applySkin(ItemStack itemStack, SkinGroup group, Skin skin)
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

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE && ItemSkin.getInstance().settings.disableInCreative)
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
    public void onPlayerGameModeChange(final PlayerGameModeChangeEvent event)
    {
        GameMode gameMode = event.getNewGameMode();

        if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE)
        {
            return;
        }

        Player player = event.getPlayer();

        for (ItemStack itemStack : player.getInventory())
        {
            applySkin(itemStack, player);
        }

        for (ItemStack itemStack : player.getInventory().getArmorContents())
        {
            applySkin(itemStack, player);
        }
    }

    private void applySkin(ItemStack itemStack, Player player)
    {
        if (itemStack == null || itemStack.getType() == Material.AIR)
        {
            return;
        }

        MaterialData materialData = new MaterialData(itemStack.getType(), itemStack.getDurability());

        SkinGroup group = ItemSkin.getInstance().getSkinManager().getMaterialTable().get(materialData);

        if (group == null)
        {
            return;
        }

        Skin skin = ItemSkin.getInstance().getPlayerManager().getSelectedSkin(player, group);

        MaterialData skinMaterialData = skin.getMaterialData();

        if (skinMaterialData.equals(materialData))
        {
            return;
        }

        itemStack.setType(skinMaterialData.getType());
        itemStack.setDurability(skinMaterialData.getData());
    }
}
