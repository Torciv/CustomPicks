package com.torciv.cpickaxes;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BreakBlock implements Listener {

    private Manager manager;
    private Picos plugin;

    public BreakBlock(Manager manager, Picos plugin) {
        this.manager = manager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        FPlayer fp = FPlayers.getInstance().getByPlayer(p);

        if (fp.hasFaction()) {
            Faction f = fp.getFaction();
            Block b = e.getBlock();

            Location loc = b.getLocation();
            FLocation floc = new FLocation(loc);
            Faction fAt = Board.getInstance().getFactionAt(floc);

            if (fAt.equals(f)) {
                if (p.getGameMode() != GameMode.SURVIVAL) {
                    return;
                }

                ItemStack ie = e.getPlayer().getItemInHand();
                
                if(ie == null || ie.getType().equals(Material.AIR)){
                    return;
                }
                
                ItemMeta im = ie.getItemMeta();
                
                String item = manager.getPico(im);

                if (item == null) {
                    return;
                }

                int nr = (int) Math.floor(Integer.parseInt(item) / 2);

                /*String name = ChatColor.stripColor(ie.getItemMeta().getDisplayName());
                List<String> lore = ie.getItemMeta().getLore();

                 for (String s : lore) {
                     String ns = ChatColor.stripColor(s);
                     String[] split = ns.split("x");

                     int radius = Integer.parseInt(split[1]);
                     if (radius < 3) {
                         return;
                     }

                    //System.out.print(nr);
                }*/

                for (int radius = nr, x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; ++x) {
                    for (int y = loc.getBlockY() - radius; y <= loc.getBlockY() + radius; ++y) {
                        for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; ++z) {
                            final Block bl = loc.getWorld().getBlockAt(x, y, z);
                            if (!bl.getType().equals(Material.BEDROCK) && !bl.getType().equals(Material.MOB_SPAWNER)) {
                                final Location location = new Location(Bukkit.getWorld(b.getWorld().getName()), (double) x, (double) y, (double) z);
                                Faction fatb = Board.getInstance().getFactionAt(new FLocation(location));
                                if (!fatb.equals(fp.getFaction()) && !fp.isAdminBypassing()) {
                                    continue;
                                } else {
                                    e.setCancelled(true);

                                    ItemStack is = new ItemStack(bl.getType(), 1, bl.getData());
                                    if (p.getInventory().firstEmpty() == -1) {
                                        //p.sendMessage(manager.color("&e&l(!) &cTu inventario esta lleno!"));
                                        bl.setType(Material.AIR);
                                        continue;
                                    } else {
                                        p.getInventory().addItem(is);
                                        bl.setType(Material.AIR);
                                    }
                                    
                                    //bl.breakNaturally();
                                }
                            }
                        }
                    }
                }
                if(p.getInventory().firstEmpty() == -1){
                    p.sendMessage(manager.color("&e&l(!) &cTu inventario esta lleno!"));
                }
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.EXPLODE, 1F, 1F);
            }
        }
    }
}
