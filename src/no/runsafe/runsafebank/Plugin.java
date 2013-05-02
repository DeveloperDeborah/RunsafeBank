package no.runsafe.runsafebank;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.runsafebank.events.CloseInventory;
import no.runsafe.runsafebank.events.Interact;
import no.runsafe.runsafebank.events.PlayerQuit;

public class Plugin extends RunsafePlugin
{
	@Override
	protected void PluginSetup()
	{
		// Events
		addComponent(Interact.class);
		addComponent(CloseInventory.class);
		addComponent(PlayerQuit.class);

		// Database Repositories
		addComponent(BankRepository.class);
	}
}
