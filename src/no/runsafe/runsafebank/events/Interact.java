package no.runsafe.runsafebank.events;

import no.runsafe.framework.event.player.IPlayerInteractEvent;
import no.runsafe.framework.server.block.RunsafeBlock;
import no.runsafe.framework.server.event.player.RunsafePlayerInteractEvent;

import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.runsafebank.BankHandler;
import org.bukkit.Material;
import org.bukkit.event.block.Action;

public class Interact implements IPlayerInteractEvent
{
	public Interact(BankHandler bankHandler)
	{
		this.bankHandler = bankHandler;
	}

	@Override
	public void OnPlayerInteractEvent(RunsafePlayerInteractEvent event)
	{
		RunsafeBlock block = event.getBlock();
		if (block != null && event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (block.getTypeId() == Material.ENDER_CHEST.getId())
			{
				RunsafePlayer player = event.getPlayer();
				if (player.hasPermission("runsafe.bank.use"))
					this.bankHandler.openBank(player, player);
				else
					player.sendColouredMessage("&cYou do not have permissions to use the bank.");

				event.setCancelled(true);
			}
		}
	}

	private BankHandler bankHandler;
}
