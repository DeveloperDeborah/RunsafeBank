package no.runsafe.runsafebank.commands;

import no.runsafe.framework.api.command.argument.PlayerArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.runsafebank.BankHandler;

import java.util.Map;

public class ViewBank extends PlayerCommand
{
	public ViewBank(BankHandler bankHandler)
	{
		super(
			"viewbank", "Opens a players bank", "runsafe.bank.view",
			new PlayerArgument()
		);
		this.bankHandler = bankHandler;
	}

	@Override
	public String OnExecute(IPlayer executor, Map<String, String> parameters)
	{
		IPlayer player = RunsafeServer.Instance.getPlayer(parameters.get("player"));

		if (player != null)
			this.bankHandler.openBank(executor, player);
		else
			executor.sendColouredMessage("&cThe player you are looking for does not exist.");

		return null;
	}

	private BankHandler bankHandler;
}
