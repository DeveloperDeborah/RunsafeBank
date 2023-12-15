package no.runsafe.runsafebank.events;

import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.api.event.inventory.IInventoryMoveItem;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.Sound;
import no.runsafe.framework.minecraft.event.inventory.RunsafeInventoryMoveItemEvent;
import no.runsafe.runsafebank.BankHandler;
import no.runsafe.runsafebank.Config;

import java.util.List;

public class Inventory implements IInventoryMoveItem
{
	public Inventory(BankHandler bankHandler)
	{
		this.bankHandler = bankHandler;
	}

	@Override
	public void OnInventoryMoveItemEvent(RunsafeInventoryMoveItemEvent event)
	{
		if (!event.getInitiator().getName().contains("'s Bank Vault"))
			return;

		List<IPlayer> viewers = event.getInitiator().getViewers();
		if (!bankHandler.isViewingBank(viewers)) // Double check someone didn't name a random chest "'s Bank Vault"
			return;

		if (!Config.isBlacklistedItem(event.getItem()))
			return;

		event.cancel();
		for (IPlayer viewer : viewers)
		{
			viewer.sendColouredMessage(Config.Messages.getItemNotAllowed());
			viewer.closeInventory();

			ILocation viewerLocation = viewer.getLocation();
			if (viewerLocation != null)
				viewerLocation.playSound(Sound.Creature.Villager.No);
		}
	}

	private final BankHandler bankHandler;
}
