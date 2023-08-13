package icu.suc.kevin557.itemskin.managements;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import icu.suc.kevin557.itemskin.ItemSkin;
import icu.suc.kevin557.itemskin.events.SelectSkinEvent;
import icu.suc.kevin557.itemskin.skin.Skin;
import icu.suc.kevin557.itemskin.skin.SkinGroup;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerManager
{
    private final File dir;
    private final Map<OfflinePlayer, Map<SkinGroup, Set<Skin>>> playerData;
    private final Map<OfflinePlayer, Map<SkinGroup, Skin>> playerSections;

    public PlayerManager(File dir)
    {
        this.dir = dir;
        this.playerData = Maps.newHashMap();
        this.playerSections = Maps.newHashMap();
    }

    public void load()
    {
        dir.mkdir();

        for (File file : FileUtils.listFiles(dir, FileFilterUtils.suffixFileFilter("yml"), null))
        {
            try
            {
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

                String uuid = file.getName().split("\\.")[0];
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));

                for (String groupName : configuration.getKeys(false))
                {
                    SkinGroup group = ItemSkin.getInstance().getSkinManager().getSkinGroups().get(groupName);

                    if (group == null)
                    {
                        ItemSkin.getInstance().getLogger().warning(String.format("The Item Skin group with the name '%s' is not exists.", groupName));
                        continue;
                    }

                    ConfigurationSection groupSection = configuration.getConfigurationSection(groupName);

                    for (String skinName : groupSection.getStringList("skins"))
                    {
                        Skin skin = group.getSkins().get(skinName);

                        if (skin == null)
                        {
                            ItemSkin.getInstance().getLogger().warning(String.format("The Item Skin '%s' is not exists.", skinName));
                            continue;
                        }

                        addSkin(player, group, skin);
                    }

                    String selected = groupSection.getString("selected");
                    Skin skin = group.getSkins().get(selected);

                    if (skin == null)
                    {
                        ItemSkin.getInstance().getLogger().warning(String.format("The Item Skin '%s' is not exists.", selected));
                        continue;
                    }

                    if (hasSkin(player, group, skin))
                    {
                        setSelectedSkin(player, group, skin);
                    }
                    else
                    {
                        ItemSkin.getInstance().getLogger().info(String.format("Player '%s' not have the Item Skin '%s'.", uuid, selected));
                    }
                }
            }
            catch (Exception e)
            {
                ItemSkin.getInstance().getLogger().warning(String.format("Failed to load the file '%s': %s", file.getName(), e.getMessage()));
            }
        }
    }

    public void save(OfflinePlayer player)
    {
        File file = new File(dir, player.getUniqueId() + ".yml");

        FileConfiguration configuration = new YamlConfiguration();

        for (Map.Entry<String, SkinGroup> entry : ItemSkin.getInstance().getSkinManager().getSkinGroups().entrySet())
        {
            ConfigurationSection groupSection = configuration.createSection(entry.getKey());
            SkinGroup group = entry.getValue();

            List<String> skins = new ArrayList<>();
            for (Skin skin : getPlayerSkins(player, group))
            {
                skins.add(group.getSkinsInverse().get(skin));
            }
            groupSection.set("skins", skins);

            groupSection.set("selected", group.getSkinsInverse().get(getSelectedSkin(player, group)));
        }

        try
        {
            dir.mkdir();
            configuration.save(file);
        }
        catch (IOException e)
        {
            ItemSkin.getInstance().getLogger().warning(String.format("Failed to save the file '%s': %s", file.getName(), e.getMessage()));
        }
    }

    public void resetSkin(OfflinePlayer player, SkinGroup group)
    {
        setSelectedSkinAndCall(player, group, group.getDefaultSkin());
    }

    public boolean hasSkin(OfflinePlayer player, SkinGroup group, Skin skin)
    {
        return getPlayerSkins(player, group).contains(skin);
    }

    public void addSkin(OfflinePlayer player, SkinGroup group, Skin skin)
    {
        getPlayerSkins(player, group).add(skin);
    }

    public void removeSkin(OfflinePlayer player, SkinGroup group, Skin skin)
    {
        getPlayerSkins(player, group).remove(skin);
    }

    public Set<Skin> getPlayerSkins(OfflinePlayer player, SkinGroup group)
    {
        return getGroupData(player).computeIfAbsent(group, g ->
        {
            HashSet<Skin> set = Sets.newHashSet();
            set.add(g.getDefaultSkin());
            return set;
        });
    }

    public Map<SkinGroup, Set<Skin>> getGroupData(OfflinePlayer player)
    {
        return playerData.computeIfAbsent(player, p -> Maps.newHashMap());
    }

    public Skin getSelectedSkin(OfflinePlayer player, SkinGroup group)
    {
        return getGroupSections(player).computeIfAbsent(group, SkinGroup::getDefaultSkin);
    }

    public void setSelectedSkin(OfflinePlayer player, SkinGroup group, Skin skin)
    {
        ItemSkin.getInstance().getPlayerManager().getGroupSections(player).put(group, skin);
    }

    public void setSelectedSkinAndCall(OfflinePlayer player, SkinGroup group, Skin skin)
    {
        Bukkit.getPluginManager().callEvent(new SelectSkinEvent(player, group, skin));
    }

    public Map<SkinGroup, Skin> getGroupSections(OfflinePlayer player)
    {
        return playerSections.computeIfAbsent(player, p -> Maps.newHashMap());
    }

    public Map<OfflinePlayer, Map<SkinGroup, Set<Skin>>> getPlayerData()
    {
        return playerData;
    }

    public Map<OfflinePlayer, Map<SkinGroup, Skin>> getPlayerSections()
    {
        return playerSections;
    }
}
