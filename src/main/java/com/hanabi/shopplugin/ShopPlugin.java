package com.hanabi.shopplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

    private Inventory customInventory;

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


    private Inventory createCustomInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(new CustomInventoryHolder(), 9 * 6, "買取ショップ");

        setInventoryRow(inventory, createItem(Material.BLUE_STAINED_GLASS_PANE, "."));

        // プレイヤーにインベントリを開く
        player.openInventory(inventory);
        return inventory;
    }

    // 指定されたアイテムで行を埋めるメソッド
    private void setInventoryRow(Inventory inventory, ItemStack item) {
        for (int i = 0; i < 13; i++) {
            inventory.setItem(i, item);
        }
        inventory.setItem(13, new ItemStack(Material.DIAMOND));
        for (int i = 14; i < 30; i++) {
            inventory.setItem(i, item);
        }
        for (int i = 30; i < 33; i++) {
            inventory.setItem(i, createItem(Material.RED_STAINED_GLASS_PANE,"売却"));
        }
        for (int i = 33; i < 37; i++) {
            inventory.setItem(i, item);
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
        if (event.getInventory().getHolder() instanceof CustomInventoryHolder) {
            // カスタムインベントリの場合、アイテムの移動をキャンセル
            event.setCancelled(true);
        }
    }

    private static class CustomInventoryHolder implements org.bukkit.inventory.InventoryHolder {
        @Override
        public Inventory getInventory() {
            return null;
        }
    }
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (command.getName().equalsIgnoreCase("createsign")) {
                Player player = (Player) sender;

                // プレイヤーが向いているブロックを取得
                Block targetBlock = player.getTargetBlock(null, 5);

                // プレイヤーが看板を向いている場合
                if (targetBlock.getType() == Material.OAK_SIGN) {
                    Sign sign = (Sign) targetBlock.getState();

                    // 看板にテキストを設定
                    for (int i = 0; i < args.length && i < 4; i++) {
                        sign.setLine(i, args[i]);
                    }

                    // 看板の更新
                    sign.update();

                    player.sendMessage("成功しました");
                } else {
                    player.sendMessage("看板を見ていないので作成できません");
                }

                return true;
            }
            return false;
        }
    }






