package no.runsafe.runsafebank.commands;

import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.Player;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.Sound;
import no.runsafe.runsafebank.BankHandler;
import no.runsafe.runsafebank.Config;

public class ClearBank extends ExecutableCommand
{
    public ClearBank(BankHandler bankHandler)
    {
        super(
            "clearbank", "Deletes a player's bank.", "runsafe.bank.clear",
            new Player().require()
        );
        this.bankHandler = bankHandler;
    }

    @Override
    public String OnExecute(ICommandExecutor executor, IArgumentList parameters)
    {
        IPlayer bankOwner = parameters.getValue("player");
        if(bankOwner == null)
            return null;

        bankHandler.clearBank(bankOwner);

		if (executor instanceof IPlayer)
			((IPlayer) executor).playSound(Sound.Item.Bottle.Empty);

        return String.format(Config.Messages.getCommandDeleteBank(), bankOwner.getPrettyName());
    }
    private final BankHandler bankHandler;
}
