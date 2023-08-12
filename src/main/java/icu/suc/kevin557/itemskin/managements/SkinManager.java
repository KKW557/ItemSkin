package icu.suc.kevin557.itemskin.managements;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import icu.suc.kevin557.itemskin.ItemSkin;
import icu.suc.kevin557.itemskin.skin.Skin;
import icu.suc.kevin557.itemskin.skin.SkinGroup;
import icu.suc.kevin557.itemskin.utils.data.MaterialData;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.Set;

public class SkinManager
{
    private final BiMap<String, SkinGroup> skinGroups;
    private final Map<MaterialData, SkinGroup> materialTable;

    public SkinManager()
    {
        this.skinGroups = HashBiMap.create();
        this.materialTable = Maps.newHashMap();
    }

    public void load(Configuration configuration)
    {
        for (String key : configuration.getKeys(false))
        {
            try
            {
                ConfigurationSection groupSection = configuration.getConfigurationSection(key);

                String groupName = groupSection.getString("name", null);
                SkinGroup group = new SkinGroup(groupName);

                skinGroups.put(key, group);

                ConfigurationSection skinsSection = groupSection.getConfigurationSection("skins");

                Set<String> keys = skinsSection.getKeys(false);

                if (keys.isEmpty())
                {
                    return;
                }

                for (String key1 : keys)
                {
                    ConfigurationSection skinSection = skinsSection.getConfigurationSection(key1);

                    String name = skinSection.getString("name");
                    Double price = skinSection.contains("price") ? skinSection.getDouble("price") : null;

                    ConfigurationSection materialSection = skinSection.getConfigurationSection("material");
                    MaterialData materialData = new MaterialData(Material.valueOf(materialSection.getString("type")), (byte) materialSection.getInt("data"));

                    registerSkin(group, key1, new Skin(name, price, materialData));
                }
            }
            catch (Exception e)
            {
                ItemSkin.getInstance().getLogger().warning(String.format("Failed to load the skin group '%s': %s", key, e.getMessage()));
            }
        }
    }

    private void registerSkin(SkinGroup group, String name, Skin skin)
    {
        group.getSkins().put(name, skin);
        materialTable.put(skin.getMaterialData(), group);
    }

    public BiMap<String, SkinGroup> getSkinGroups()
    {
        return skinGroups;
    }

    public Map<MaterialData, SkinGroup> getMaterialTable()
    {
        return materialTable;
    }
}
