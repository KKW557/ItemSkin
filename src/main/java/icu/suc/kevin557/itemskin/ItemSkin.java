package icu.suc.kevin557.itemskin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import icu.suc.kevin557.itemskin.commands.AbstractCommand;
import icu.suc.kevin557.itemskin.commands.ItemSkinCommand;
import icu.suc.kevin557.itemskin.commands.SkinMenuCommand;
import icu.suc.kevin557.itemskin.configs.I18n;
import icu.suc.kevin557.itemskin.listeners.InventoryListener;
import icu.suc.kevin557.itemskin.listeners.PlayerListener;
import icu.suc.kevin557.itemskin.listeners.SlotListener;
import icu.suc.kevin557.itemskin.managements.MenuManager;
import icu.suc.kevin557.itemskin.managements.PlayerManager;
import icu.suc.kevin557.itemskin.managements.SkinManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ItemSkin extends JavaPlugin
{
    private static ItemSkin instance;
    private Economy economy = null;

    private ProtocolManager protocolManager;
    private SkinManager skinManager;
    private PlayerManager playerManager;
    private MenuManager menuManager;

    @Override
    public void onLoad()
    {
        super.onLoad();

        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable()
    {
        super.onEnable();

        instance = this;
        setupEconomy();

        I18n.init(new File(getDataFolder(), "messages.properties"));
        saveConfigs();
        loadConfigs();

        menuManager = new MenuManager();

        registerEvents();
        registerCommands();
    }

    public void loadConfigs()
    {
        I18n.load();

        skinManager = new SkinManager();
        skinManager.load(getConfig());
        playerManager = new PlayerManager();
    }

    private void saveConfigs()
    {
        saveDefaultConfig();

        if (I18n.FILE != null && !I18n.FILE.exists())
        {
            saveResource("messages.properties", false);
        }
    }

    private void registerCommands()
    {
        registerCommand(getCommand("itemskin"), new ItemSkinCommand());
        registerCommand(getCommand("skinmenu"), new SkinMenuCommand());
    }

    private void registerEvents()
    {
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        protocolManager.addPacketListener(new SlotListener(this));
    }

    private void registerCommand(PluginCommand pluginCommand, AbstractCommand abstractCommand)
    {
        pluginCommand.setExecutor(abstractCommand);
        pluginCommand.setTabCompleter(abstractCommand);
    }

    private void setupEconomy()
    {
        setup:
        {
            if (getServer().getPluginManager().getPlugin("Vault") == null)
            {
                break setup;
            }

            RegisteredServiceProvider<Economy> registration = getServer().getServicesManager().getRegistration(Economy.class);

            if (registration == null)
            {
                break setup;
            }

            economy = registration.getProvider();

            if (economy == null)
            {
                break setup;
            }

            return;
        }

        getLogger().severe("Disabled due to no Vault dependency found!");
        getServer().getPluginManager().disablePlugin(this);
    }

    public static ItemSkin getInstance()
    {
        return instance;
    }

    public Economy getEconomy()
    {
        return economy;
    }

    public SkinManager getSkinManager()
    {
        return skinManager;
    }

    public PlayerManager getPlayerManager()
    {
        return playerManager;
    }

    public MenuManager getMenuManager()
    {
        return menuManager;
    }
}
