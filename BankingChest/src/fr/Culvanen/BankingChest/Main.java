package fr.Culvanen.BankingChest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.Culvanen.BankingChest.Events.ClickNPC;
import fr.Culvanen.BankingChest.Events.Join;
import net.minecraft.server.v1_16_R1.EntityPlayer;

public class Main extends JavaPlugin implements Listener {
	
	public Inventory inv;
	public int Inv_max = 54;
	
	public static DataManager data;
	
	@Override
	public void onEnable() {

		data = new DataManager(this);
		this.getServer().getPluginManager().registerEvents(new Join(),  this);
		this.getServer().getPluginManager().registerEvents(new ClickNPC(),  this);
		
		if(data.getConfig().contains("data"))
			loadNPC();
		
		this.getServer().getPluginManager().registerEvents(this,  this);
		createBankingChest();
		
		
		if(!Bukkit.getOnlinePlayers().isEmpty())
			for(Player player : Bukkit.getOnlinePlayers()) {
				PacketReader reader = new PacketReader();
				reader.inject(player);
			}
	}
	
	@Override
	public void onDisable() {
		
		for(Player player :Bukkit.getOnlinePlayers()) {
			PacketReader reader = new PacketReader();
			reader.uninject(player);
			for(EntityPlayer npc : NPC.getNPCs())
				NPC.removeNPC(player, npc);
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmcd, String label, String[] args) {
		if(label.equalsIgnoreCase("createnpc")) {
			if(!(sender  instanceof Player)) {
				return true;
			}
			Player player = (Player) sender;
			if(args.length == 0) {
				NPC.createNPC(player, player.getName());
				player.sendMessage("Création du NPC");
				return true;
			}
			NPC.createNPC(player, args[0]);
			player.sendMessage("Création du NPC!");
			return true;
		}
		if(label.equalsIgnoreCase("inv")) {
			if(!(sender  instanceof Player)) {
				return true;
			}
			Player player = (Player) sender;
			player.openInventory(inv);
			return true;
		}
		return false;
	}
	
	public static FileConfiguration getData() {
		return data.getConfig();
	}
	
	public static void saveData() {
		data.saveConfig();
	}
	
	public void loadNPC() {
		FileConfiguration file = data.getConfig();
		file.getConfigurationSection("data").getKeys(false).forEach(npc ->{
			Location location = new Location(Bukkit.getWorld(file.getString("data." + npc + ".world")),
					file.getInt("data." + npc + ".x"), file.getInt("data." + npc + ".y"), file.getInt("data." + npc + ".z")); 
			location.setPitch((float) file.getDouble("data." + npc + ".p"));
			location.setYaw((float) file.getDouble("data." + npc + ".yaw"));
			
			String name = file.getString("data." + npc + ".name");
			GameProfile gameProfile = new GameProfile(UUID.randomUUID(),  ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Inventoriste");
			gameProfile.getProperties().put("textures", new Property("textures", file.getString("data." + npc + ".text"), 
					file.getString("data." + npc + ".signature")));
			
			
			NPC.loadNPC(location, gameProfile);
		});
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if(!event.getInventory().equals(inv))
			return;
		if(event.getCurrentItem() == null) return;
		if(event.getCurrentItem().getItemMeta() == null) return;
		if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
		
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		
		if(event.getSlot() == 0) {
			player.sendMessage("Click sur le coffre 1");
		}
		return;
	}
	
	public void createBankingChest() {
		
		inv = Bukkit.createInventory(null, 54, ChatColor.BLUE + "" + ChatColor.BOLD + "Coffre de Stockage");
		int invSlot = 1;
		
		
		ItemStack item = new ItemStack(Material.CHEST);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.WHITE + "Cliquez-ici pour acheter!");
		lore.add(ChatColor.GREEN + "" + ChatColor.ITALIC + "Prix: 2500p");
		ItemMeta meta = item.getItemMeta();
		
		while(invSlot <= 54) {
			if(invSlot == 1) {

				meta.setLore(lore);
			}
			item.setType(Material.CHEST);
			item.setAmount(invSlot);//fonctionne
			meta.setDisplayName(ChatColor.WHITE + ""+ ChatColor.BOLD + "Coffre n°" + invSlot);
			item.setItemMeta(meta);
			meta.setLore(lore);
			inv.setItem(invSlot-1, item);
			invSlot++;
		}
		
		/*
		meta.setDisplayName(ChatColor.WHITE + "Coffre n°1");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.WHITE + "Cliquez-ici pour acheter!\nPrix: 2500");
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(0, item);
		
		item.setType(Material.CHEST);
		meta.setDisplayName(ChatColor.WHITE + "Coffre n°2");
		item.setItemMeta(meta);
		inv.setItem(1, item);
		
		item.setType(Material.CHEST);
		meta.setDisplayName(ChatColor.WHITE + "Coffre n°3");
		item.setItemMeta(meta);
		inv.setItem(2, item);
		
		item.setType(Material.CHEST);
		meta.setDisplayName(ChatColor.WHITE + "Coffre n°4");
		item.setItemMeta(meta);
		inv.setItem(3, item);
		
		item.setType(Material.CHEST);
		meta.setDisplayName(ChatColor.WHITE + "Coffre n°5");
		item.setItemMeta(meta);
		inv.setItem(4, item);*/
	}

}






















