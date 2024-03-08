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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSignOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShopPlugin extends JavaPlugin implements Listener {

    private Inventory createCustomInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "買取ショップ");

        // 1列目
        ItemStack blueGlass = createItem(Material.BLUE_STAINED_GLASS, "");
        setInventoryColumn(inventory, blueGlass, 0);

        // 2列目
        ItemStack diamond = createItem(Material.DIAMOND, "ダイヤモンド");
        setInventoryColumn(inventory, blueGlass, 1);
        inventory.setItem(1 * 9 + 4, diamond);
        setInventoryColumn(inventory, blueGlass, 2);

        // 3列目
        setInventoryColumn(inventory, blueGlass, 2);

        // 4列目
        setInventoryColumn(inventory, blueGlass, 3);
        setInventoryColumn(inventory, createItem(Material.RED_STAINED_GLASS, "売却"), 4);
        setInventoryColumn(inventory, blueGlass, 5);

        // 5列目
        setInventoryColumn(inventory, blueGlass, 4);
        inventory.setItem(4 * 9 + 1, createItem(Material.RED_BANNER, "一つ減らす"));
        setInventoryColumn(inventory, createItem(Material.RED_STAINED_GLASS, "売却"), 4);
        inventory.setItem(4 * 9 + 7, createItem(Material.BLUE_BANNER, "一つ増やす"));
        setInventoryColumn(inventory, blueGlass, 5);

        // 6列目
        setInventoryColumn(inventory, blueGlass, 5);

        player.openInventory(inventory);
        return inventory;
    }

    // 指定されたアイテムで列を埋めるメソッド
    private void setInventoryColumn(Inventory inventory, ItemStack item, int column) {
        for (int row = 0; row < inventory.getSize() / 9; row++) {
            inventory.setItem(row * 9 + column, item);
        }
    }

    // アイテムスタックを作成するメソッド
    private ItemStack createItem(Material material, String displayName) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Shopプラグインが開始しました");
        Bukkit.getPluginManager().registerEvents(this, this);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Shopプラグインが終了しました");
        // Plugin shutdown logic
    }
    @EventHandler
    public void onPlayerSignOpenEvent(PlayerSignOpenEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        // 左クリックかつ対象がブロックの場合
        if (action == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();

            // 対象が看板の場合
            if (clickedBlock != null && clickedBlock.getType() == Material.OAK_SIGN) {
                Sign sign = (Sign) clickedBlock.getState();
                String[] lines = sign.getLines();

                if (lines[0].equalsIgnoreCase("買取ショップ")) {
                    if (lines[1].equalsIgnoreCase("ダイヤモンド")) {
                        createCustomInventory(player);
                    }
                }


            }
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getClickedInventory().equals("買取ショップ")) {
            // プレイヤーがカスタムインベントリ内でアイテムを移動しようとした場合
            event.setCancelled(true); // アイテムの移動をキャンセル
        }
    }
}




