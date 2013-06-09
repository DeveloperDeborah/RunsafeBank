package no.runsafe.runsafebank.events;

import no.runsafe.framework.event.player.IPlayerInteractEvent;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.framework.server.block.RunsafeBlock;
import no.runsafe.framework.server.event.player.RunsafePlayerInteractEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.runsafebank.BankHandler;

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
		if (block != null && event.isRightClick() && block.getTypeId() == Item.Decoration.EnderChest.getTypeID())
		{
			RunsafePlayer player = event.getPlayer();
			if (player.hasPermission("runsafe.bank.use"))
				this.bankHandler.openBank(player, player);
			else
				player.sendColouredMessage("&cYou do not have permissions to use the bank.");

			event.setCancelled(true);
		}
	}

	private BankHandler bankHandler;
}
