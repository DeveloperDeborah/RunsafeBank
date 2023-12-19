package no.runsafe.runsafebank;

import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.log.IDebug;
import no.runsafe.framework.features.Commands;
import no.runsafe.framework.features.Database;
import no.runsafe.framework.features.Events;
import no.runsafe.runsafebank.commands.ViewBank;
import no.runsafe.runsafebank.commands.ClearBank;
import no.runsafe.runsafebank.events.Interact;
import no.runsafe.runsafebank.events.Inventory;

public class Plugin extends RunsafeConfigurablePlugin
{
	public static IDebug Debugger;
	public static IServer Server;
	public static IConsole Console;
	public static IScheduler Scheduler;

	@Override
	protected void pluginSetup()
	{
		Debugger = getComponent(IDebug.class);
		Server = getComponent(IServer.class);
		Console = getComponent(IConsole.class);
		Scheduler = getComponent(IScheduler.class);

		// Framework features
		addComponent(Commands.class);
		addComponent(Events.class);
		addComponent(Database.class);

		// Plugin components
		addComponent(Config.class);
		addComponent(Interact.class);
		addComponent(Inventory.class);
		addComponent(BankHandler.class);
		addComponent(BankRepository.class);
		addComponent(ViewBank.class);
		addComponent(ClearBank.class);
	}
}
