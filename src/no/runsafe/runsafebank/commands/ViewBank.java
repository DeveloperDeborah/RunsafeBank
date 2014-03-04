package no.runsafe.runsafebank.commands;

import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.Player;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.runsafebank.BankHandler;

public class ViewBank extends PlayerCommand
{
	public ViewBank(BankHandler bankHandler)
	{
		super(
			"viewbank", "Opens a players bank", "runsafe.bank.view",
			new Player().require()
		);
		this.bankHandler = bankHandler;
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		IPlayer player = parameters.getValue("player");
		if (player != null)
			this.bankHandler.openBank(executor, player);
		return null;
	}

	private BankHandler bankHandler;
}
