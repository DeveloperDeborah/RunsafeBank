package no.runsafe.runsafebank.events;

import no.runsafe.framework.event.inventory.IInventoryClick;
import no.runsafe.framework.server.event.inventory.RunsafeInventoryClickEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.runsafebank.BankHandler;

public class InventoryClick implements IInventoryClick
{
	public InventoryClick(BankHandler bankHandler)
	{
		this.bankHandler = bankHandler;
	}

	@Override
	public void OnInventoryClickEvent(RunsafeInventoryClickEvent event)
	{
		RunsafePlayer player = event.getWhoClicked();
		if (this.bankHandler.isViewingBank(player))
			this.bankHandler.savePlayerBank(this.bankHandler.getViewingBank(player), event.getInventory());
	}

	private BankHandler bankHandler;
}
