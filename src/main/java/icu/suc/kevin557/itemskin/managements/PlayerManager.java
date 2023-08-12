package icu.suc.kevin557.itemskin.managements;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import icu.suc.kevin557.itemskin.events.SelectSkinEvent;
import icu.suc.kevin557.itemskin.skin.Skin;
import icu.suc.kevin557.itemskin.skin.SkinGroup;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class PlayerManager
{
    private final Map<OfflinePlayer, Map<SkinGroup, Set<Skin>>> playerData;
    private final Map<OfflinePlayer, Map<SkinGroup, Skin>> playerSections;

    public PlayerManager()
    {
        this.playerData = Maps.newHashMap();
        this.playerSections = Maps.newHashMap();
    }

    public void resetSkin(OfflinePlayer player, SkinGroup group)
    {
        setSelectedSkin(player, group, group.getDefaultSkin());
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
