package no.runsafe.runsafebank.commands;

import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.command.argument.AnyPlayerRequired;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.runsafebank.BankHandler;

public class ViewBank extends PlayerCommand
{
	public ViewBank(BankHandler bankHandler, IServer server)
	{
		super(
			"viewbank", "Opens a players bank", "runsafe.bank.view",
			new AnyPlayerRequired()
		);
		this.bankHandler = bankHandler;
		this.server = server;
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		IPlayer player = server.getPlayer(parameters.get("player"));

		if (player != null)
			this.bankHandler.openBank(executor, player);
		else
			executor.sendColouredMessage("&cThe player you are looking for does not exist.");

		return null;
	}

	private BankHandler bankHandler;
	private final IServer server;
}
