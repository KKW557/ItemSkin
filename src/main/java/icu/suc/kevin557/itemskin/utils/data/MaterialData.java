package icu.suc.kevin557.itemskin.utils.data;

import org.bukkit.Material;

import java.util.Objects;

public class MaterialData
{
    private final Material type;
    private final short data;

    public MaterialData(final Material type, final short data) {
        this.type = type;
        this.data = data;
    }

    public Material getType()
    {
        return type;
    }

    public short getData() {
        return data;
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
        MaterialData that = (MaterialData) o;
        return data == that.data && type == that.type;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(type, data);
    }

    @Override
    public String toString()
    {
        return "MaterialData{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}
