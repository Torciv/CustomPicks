package com.torciv.cpickaxes;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GivePick implements CommandExecutor {

    //cpick give <player> <number>
    private Manager manager;
    private Picos plugin;

    public GivePick(Manager manager, Picos plugin) {
        this.manager = manager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("cpick")) {
            if (!sender.hasPermission("cpick.give")) {
                String noadmin = plugin.getConfig().getString("messages.no-perm-commands");
                sender.sendMessage(manager.color(noadmin));
                return true;
            }

            if (args.length > 3) {
                sender.sendMessage(manager.color(" "));
                sender.sendMessage(manager.color(" &cToo many arguments!"));
                sender.sendMessage(manager.color(" &cUsa: &6/cpick &ehelp"));
                sender.sendMessage(manager.color(" "));
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage(manager.color(" "));
                sender.sendMessage(manager.color(" &cUsa: &6/cpick &ehelp"));
                sender.sendMessage(manager.color(" "));
                return true;
            }

            if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 1) {
                    sender.sendMessage(manager.color(" "));
                    sender.sendMessage(manager.color(" &e=+=+=+=+=+=+=+= &aCPICK HELP &e=+=+=+=+=+=+=+="));
                    sender.sendMessage(manager.color(" "));
                    sender.sendMessage(manager.color(" &6/cpick &egive <player> <radio>"));
                    sender.sendMessage(manager.color(" &6/cpick &ereload"));
                    sender.sendMessage(manager.color(" "));
                    sender.sendMessage(manager.color(" &ePlugin made by &bTorciv"));
                    sender.sendMessage(manager.color(" "));
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (args.length == 1) {

                    plugin.reloadConfig();
                    plugin.saveConfig();

                    sender.sendMessage(manager.color(" "));
                    sender.sendMessage(manager.color(" &a CPick's config reloaded!"));
                    sender.sendMessage(manager.color(" "));
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("give")) { //cpick give <player> <number>
                if (args.length == 3) {

                    Player target = Bukkit.getServer().getPlayer(args[1]);

                    if (target == null) {
                        sender.sendMessage(manager.color(" "));
                        sender.sendMessage(manager.color(" &cCouln't find the specified player!"));
                        sender.sendMessage(manager.color(" &cError: That Player may not be online!"));
                        sender.sendMessage(manager.color(" "));
                        return true;
                    }

                    if (target.getInventory().firstEmpty() == -1) {
                        sender.sendMessage(manager.color(" "));
                        sender.sendMessage(manager.color(" &cEl jugador tiene el inventario lleno!"));
                        sender.sendMessage(manager.color(" "));
                        return true;
                    }

                    if (manager.isInt(args[2])) {
                        String num = args[2];
                        if (plugin.getConfig().getConfigurationSection("picks").contains(String.valueOf(args[2]))) {
                            String name = manager.displaynameCheck(num);
                            List<String> lore = manager.loreCheck(num);
                            String type = plugin.getConfig().getString("picks." + num + ".Pico").toUpperCase();

                            PlayerInventory pi = Bukkit.getServer().getPlayer(args[1]).getInventory();
                            ItemStack pick = manager.createItem(new ItemStack(Material.valueOf(type), 1), name, lore);
                            
                            List<String> ench = plugin.getConfig().getStringList("picks." + num + ".enchants");
                            for(String e : ench){
                                String [] split = e.split(":");
                                String enchant = split[0];
                                String lvl = split[1];
                                int level = Integer.parseInt(lvl);
                                pick.addEnchantment(Enchantment.getByName(enchant), level);
                                
                            }
                            
                            pi.addItem(pick);
                            target.sendMessage(manager.color("&eHas recibido un pico &d" + num + "x" + num));
                            sender.sendMessage(manager.color("&d" + args[1] + " &eha recibido un pico  &d" + num + "x" + num));
                            return true;

                        } else {
                            sender.sendMessage(manager.color(" "));
                            sender.sendMessage(manager.color(" &cEl Radio no se encuentra en la config!"));
                            sender.sendMessage(manager.color(" "));
                            return true;
                        }
                    } else {
                        sender.sendMessage(manager.color(" "));
                        sender.sendMessage(manager.color(" &cEl Radio tiene que ser un número!"));
                        sender.sendMessage(manager.color(" "));
                        return true;
                    }
                }
            }
        }
        return true;
    }

}
