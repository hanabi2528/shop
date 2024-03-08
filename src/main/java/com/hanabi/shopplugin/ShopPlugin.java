package com.hanabi.shopplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSignOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShopPlugin extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Shopプラグインが開始しました");
        Bukkit.getPluginManager().registerEvents(this,this);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Shopプラグインが終了しました");
        // Plugin shutdown logic
    }

    public void onPlayerSignOpenEvent(PlayerSignOpenEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        // 左クリックかつ対象がブロックの場合
        if (action == Action.LEFT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();

            // 対象が看板の場合
            if (clickedBlock != null && clickedBlock.getType() == Material.OAK_SIGN) {
                Sign sign = (Sign) clickedBlock.getState();
                String[] lines = sign.getLines();

                if (lines[0].equalsIgnoreCase("公式ショップ")){
                    if (lines[1].equalsIgnoreCase("ダイヤモンド")){
                        opendiamondInventory(player);
                    }
                }


            }
        }
    }


    private void opendiamondInventory(Player player){
        Inventory inventory = getServer().createInventory(null,9,"Shopインベントリ");
        ItemStack itemStack = new ItemStack(Material.DIAMOND);
        inventory.addItem(itemStack);
        player.openInventory(inventory);
    }

}




