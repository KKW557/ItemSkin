package icu.suc.kevin557.itemskin.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import icu.suc.kevin557.itemskin.ItemSkin;
import icu.suc.kevin557.itemskin.skin.Skin;
import icu.suc.kevin557.itemskin.skin.SkinGroup;
import icu.suc.kevin557.itemskin.utils.data.MaterialData;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class SlotListener extends PacketAdapter
{
    public SlotListener(Plugin plugin)
    {
        super(plugin, PacketType.Play.Server.SET_SLOT);
    }

    @Override
    public void onPacketSending(final PacketEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }

        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE && ItemSkin.getInstance().settings.disableInCreative)
        {
            return;
        }

        PacketContainer packet = event.getPacket();

        StructureModifier<Integer> integers = packet.getIntegers();

        if (integers.read(0) != 0)
        {
            return;
        }

        StructureModifier<ItemStack> modifier = packet.getItemModifier();

        ItemStack itemStack = modifier.read(0);

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

        int index = integers.read(1);

        switch (index)
        {
            case 5:
            {
                player.getInventory().setHelmet(itemStack);
                break;
            }
            case 6:
            {
                player.getInventory().setChestplate(itemStack);
                break;
            }
            case 7:
            {
                player.getInventory().setLeggings(itemStack);
                break;
            }
            case 8:
            {
                player.getInventory().setBoots(itemStack);
                break;
            }
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            {
                player.getInventory().setItem(index - 36, itemStack);
                break;
            }
            default:
            {
                player.getInventory().setItem(index, itemStack);
                break;
            }
        }

        event.setCancelled(true);
    }
}
