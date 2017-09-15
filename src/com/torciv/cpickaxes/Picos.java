
package com.torciv.cpickaxes;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Picos extends JavaPlugin{
    
    private static Picos plugin;
    private Manager manager;
    
    public FileConfiguration config;
    public File cfile;
    
    public Picos() {
        plugin = this;
        manager = new Manager(this);
    }
    
    @Override
    public void onEnable(){
        this.cfile = new File(getDataFolder(), "config.yml");
        if (!this.cfile.exists()) {
          getLogger().info("Config.yml not found, creating!");
          saveDefaultConfig();
        } else {
          getLogger().info("Config.yml found, loading!");
        }
        
        this.config = getConfig();
        this.config.options().copyDefaults(true);
        
        getCommand("cpick").setExecutor(new GivePick(manager, this));
        Bukkit.getServer().getPluginManager().registerEvents(new BreakBlock(manager, this), this);
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&l(!) &bCustom PickAxes Plugin &aEnabled!"));
    }
    
    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&l(!) &bCustom  PickAxes Plugin &cDisabled!"));
    }
    
    
    
}
