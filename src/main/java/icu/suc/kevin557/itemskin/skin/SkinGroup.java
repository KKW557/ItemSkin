package icu.suc.kevin557.itemskin.skin;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;

public class SkinGroup
{
    private final String name;
    private final Map<String, Skin> skins;

    public SkinGroup(String name)
    {
        this.name = name;
        this.skins = Maps.newLinkedHashMap();
    }

    public Skin getDefaultSkin()
    {
        return skins.values().iterator().next();
    }

    public String getName()
    {
        return name;
    }

    public Map<String, Skin> getSkins()
    {
        return skins;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        SkinGroup group = (SkinGroup) o;
        return Objects.equals(name, group.name) && Objects.equals(skins, group.skins);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, skins);
    }
}
