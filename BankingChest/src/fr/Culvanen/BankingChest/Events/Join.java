package fr.Culvanen.BankingChest.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.Culvanen.BankingChest.NPC;
import fr.Culvanen.BankingChest.PacketReader;

public class Join implements Listener{
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		PacketReader reader = new PacketReader();
		reader.inject(event.getPlayer());
		if(NPC.getNPCs() == null) {
			return;
		}
		if(NPC.getNPCs().isEmpty()) {
			return;
		}
		NPC.addJoinPacket(event.getPlayer());
		
		PacketReader reader2 = new PacketReader();
		reader2.inject(event.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		PacketReader reader = new PacketReader();
		reader.uninject(event.getPlayer());
	}

}
