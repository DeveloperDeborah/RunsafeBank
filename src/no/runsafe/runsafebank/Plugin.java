package no.runsafe.runsafebank;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.runsafebank.commands.ViewBank;
import no.runsafe.runsafebank.events.CloseInventory;
import no.runsafe.runsafebank.events.Interact;
import no.runsafe.runsafebank.events.InventoryClick;
import no.runsafe.runsafebank.events.PlayerQuit;

public class Plugin extends RunsafePlugin
{
	@Override
	protected void PluginSetup()
	{
		// Events
		addComponent(Interact.class);
		addComponent(PlayerQuit.class);
		addComponent(InventoryClick.class);
		addComponent(CloseInventory.class);

		// Handlers
		addComponent(BankHandler.class);

		// Database Repositories
		addComponent(BankRepository.class);

		// Commands
		addComponent(ViewBank.class);
	}
}
