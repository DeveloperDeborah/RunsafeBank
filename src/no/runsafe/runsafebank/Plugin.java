package no.runsafe.runsafebank;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.features.Commands;
import no.runsafe.framework.features.Database;
import no.runsafe.framework.features.Events;
import no.runsafe.runsafebank.commands.ViewBank;
import no.runsafe.runsafebank.events.Interact;

public class Plugin extends RunsafePlugin
{
	@Override
	protected void pluginSetup()
	{
		// Framework features
		addComponent(Commands.class);
		addComponent(Events.class);
		addComponent(Database.class);

		// Plugin components
		addComponent(Interact.class);
		addComponent(BankHandler.class);
		addComponent(BankRepository.class);
		addComponent(ViewBank.class);
	}
}
