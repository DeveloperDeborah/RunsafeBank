package no.runsafe.runsafebank;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.runsafebank.commands.ViewBank;
import no.runsafe.runsafebank.events.Interact;

public class Plugin extends RunsafePlugin
{
	@Override
	protected void PluginSetup()
	{
		addComponent(Interact.class);
		addComponent(BankHandler.class);
		addComponent(BankRepository.class);
		addComponent(ViewBank.class);
	}
}
