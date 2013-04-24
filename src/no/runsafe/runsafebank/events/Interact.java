package no.runsafe.runsafebank.events;

import no.runsafe.framework.event.player.IPlayerInteractEvent;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.event.player.RunsafePlayerInteractEvent;
import no.runsafe.framework.server.inventory.RunsafeInventory;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class Interact implements IPlayerInteractEvent
{
	@Override
	public void OnPlayerInteractEvent(RunsafePlayerInteractEvent event)
	{
		if (event.getBlock().getTypeId() == Material.ENDER_CHEST.getId())
		{
			RunsafeInventory newInventory = RunsafeServer.Instance.createInventory(null, 54, "Bank");
			event.getPlayer().openInventory(newInventory);
		}
	}
}
