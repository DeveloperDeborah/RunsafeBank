package no.runsafe.runsafebank;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.runsafebank.events.Interact;

public class Plugin extends RunsafePlugin
{
	@Override
	protected void PluginSetup()
	{
		// Events
		addComponent(Interact.class);
	}
}
