package icu.suc.kevin557.itemskin.events;

import icu.suc.kevin557.itemskin.skin.Skin;
import icu.suc.kevin557.itemskin.skin.SkinGroup;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SelectSkinEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;

    private OfflinePlayer player;
    private SkinGroup group;
    private Skin skin;

    public SelectSkinEvent(OfflinePlayer player, SkinGroup group, Skin skin)
    {
        this.player = player;
        this.group = group;
        this.skin = skin;
    }

    public OfflinePlayer getPlayer()
    {
        return player;
    }

    public void setPlayer(OfflinePlayer player)
    {
        this.player = player;
    }

    public SkinGroup getGroup()
    {
        return group;
    }

    public void setGroup(SkinGroup group)
    {
        this.group = group;
    }

    public Skin getSkin()
    {
        return skin;
    }

    public void setSkin(Skin skin)
    {
        this.skin = skin;
    }

    @Override
    public boolean isCancelled()
    {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel)
    {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
