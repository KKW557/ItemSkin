package icu.suc.kevin557.itemskin.managements;

import com.google.common.collect.Maps;
import icu.suc.kevin557.itemskin.ItemSkin;
import icu.suc.kevin557.itemskin.skin.Skin;
import icu.suc.kevin557.itemskin.skin.SkinGroup;
import icu.suc.kevin557.itemskin.utils.data.MaterialData;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class SkinManager
{
    private final File file;

    private final Map<String, SkinGroup> skinGroups;
    private final Map<MaterialData, SkinGroup> materialTable;

    public SkinManager(File file)
    {
        this.file = file;

        this.skinGroups = Maps.newHashMap();
        this.materialTable = Maps.newHashMap();
    }

    public void load()
    {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

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
                    try
                    {
                        ConfigurationSection skinSection = skinsSection.getConfigurationSection(key1);

                        String name = skinSection.getString("name");
                        Double price = skinSection.contains("price") ? skinSection.getDouble("price") : null;

                        ConfigurationSection materialSection = skinSection.getConfigurationSection("material");
                        MaterialData materialData = new MaterialData(Material.valueOf(materialSection.getString("type")), (byte) materialSection.getInt("data"));

                        registerSkin(group, key1, new Skin(name, price, materialData));
                    }
                    catch (Exception e)
                    {
                        ItemSkin.getInstance().getLogger().warning(String.format("Failed to load the skin '%s' in the skin group '%s': %s", key1, key, e.getMessage()));
                    }
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
        group.getSkinsInverse().put(skin, name);
        materialTable.put(skin.getMaterialData(), group);
    }

    public Map<String, SkinGroup> getSkinGroups()
    {
        return skinGroups;
    }

    public Map<MaterialData, SkinGroup> getMaterialTable()
    {
        return materialTable;
    }

    public File getFile()
    {
        return file;
    }
}
