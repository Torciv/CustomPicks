package com.torciv.cpickaxes;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Manager {

    private Picos plugin;

    public Manager(Picos plugin) {
        this.plugin = plugin;
    }

    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public ItemStack createItem(ItemStack item, String name, List<String> lore) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }

    public String displaynameCheck(String i) {
        String name;
        name = plugin.getConfig().getString("picks." + i + ".name");
        name = color(name);
        return name;
    }

    public List<String> loreCheck(String it) {

        ArrayList<String> lore = new ArrayList<String>();
        List<String> list = plugin.getConfig().getStringList("picks." + it + ".lore");
        for (String s : list) {
            lore.add(color(s.replace("%RAD%", it)));
        }
        return lore;
    }

    public String getPico(ItemMeta im) {
        for (String its : plugin.getConfig().getConfigurationSection("picks").getKeys(false)) {

            String type = plugin.getConfig().getString("picks." + its + ".Pico").toUpperCase();

            String name = displaynameCheck(its);
            List<String> lore = loreCheck(its);
            int nm = 1;

            ItemStack i = createItem(new ItemStack(Material.valueOf(type), nm), name, lore);

            List<String> ench = plugin.getConfig().getStringList("picks." + its + ".enchants");
            for (String e : ench) {
                String[] split = e.split(":");
                String enchant = split[0];
                String lvl = split[1];
                int level = Integer.parseInt(lvl);
                i.addEnchantment(Enchantment.getByName(enchant), level);
            }
            
            if (im.equals(i.getItemMeta())) {
                return its;
            }
        }
        return null;
    }
}
