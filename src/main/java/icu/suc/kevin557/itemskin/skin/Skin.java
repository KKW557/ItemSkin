package icu.suc.kevin557.itemskin.skin;

import icu.suc.kevin557.itemskin.utils.data.MaterialData;

import java.util.Objects;

public class Skin
{
    private final String name;
    private final Double price;
    private final MaterialData materialData;

    public Skin(String name, Double price, MaterialData materialData)
    {
        this.name = name;
        this.price = price;
        this.materialData = materialData;
    }

    public String getName()
    {
        return name;
    }

    public Double getPrice()
    {
        return price;
    }

    public MaterialData getMaterialData()
    {
        return materialData;
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
        Skin skin = (Skin) o;
        return Objects.equals(name, skin.name) && Objects.equals(price, skin.price) && Objects.equals(materialData, skin.materialData);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, price, materialData);
    }
}
