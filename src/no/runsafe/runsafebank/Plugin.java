package no.runsafe.runsafebank;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.runsafebank.events.CloseInventory;
import no.runsafe.runsafebank.events.Interact;

public class Plugin extends RunsafePlugin
{
	@Override
	protected void PluginSetup()
	{
		// Events
		addComponent(Interact.class);
		addComponent(CloseInventory.class);

		// Database Repositories
		addComponent(BankRepository.class);
	}
}
