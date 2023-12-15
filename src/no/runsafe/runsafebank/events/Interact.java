package no.runsafe.runsafebank.events;

import no.runsafe.framework.api.block.IBlock;
import no.runsafe.framework.api.event.player.IPlayerInteractEvent;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerInteractEvent;
import no.runsafe.runsafebank.BankHandler;
import no.runsafe.runsafebank.Config;

public class Interact implements IPlayerInteractEvent
{
	public Interact(BankHandler bankHandler)
	{
		this.bankHandler = bankHandler;
	}

	@Override
	public void OnPlayerInteractEvent(RunsafePlayerInteractEvent event)
	{
		IBlock block = event.getBlock();
		if (block == null || !event.isRightClick() || !block.is(Item.Decoration.EnderChest))
			return;

		IPlayer player = event.getPlayer();

		if (!player.hasPermission("runsafe.bank.use"))
			player.sendColouredMessage(Config.Messages.getNoPermissions());
		else if (!Config.isBankWorld(player.getWorld()))
			player.sendColouredMessage(Config.Messages.getWrongWorld());
		else this.bankHandler.openBank(player, player);

		event.cancel();
	}

	private final BankHandler bankHandler;
}
