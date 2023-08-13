package icu.suc.kevin557.itemskin.listeners;

import icu.suc.kevin557.itemskin.ItemSkin;
import icu.suc.kevin557.itemskin.skin.Skin;
import icu.suc.kevin557.itemskin.skin.SkinGroup;
import icu.suc.kevin557.itemskin.utils.data.MaterialData;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener
{
    @EventHandler
    public void onItemSpawn(final ItemSpawnEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }

        Item item = event.getEntity();

        ItemStack itemStack = item.getItemStack();

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

        item.setItemStack(itemStack);
    }
}
