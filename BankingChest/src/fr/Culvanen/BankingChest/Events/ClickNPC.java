package fr.Culvanen.BankingChest.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClickNPC implements Listener {

	@EventHandler
	public void onClick(RightClickNPC event) {
		
		Player player = event.getPlayer();
		player.sendMessage("[BankingChest] - Je récupère vos coffres, quelques instants s'il vous plaît!");
		return;
	}
}
