package no.runsafe.runsafebank.events;

import no.runsafe.framework.event.player.IPlayerQuitEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerQuitEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.runsafebank.BankHandler;

public class PlayerQuit implements IPlayerQuitEvent
{
	public PlayerQuit(BankHandler bankHandler)
	{
		this.bankHandler = bankHandler;
	}

	@Override
	public void OnPlayerQuit(RunsafePlayerQuitEvent event)
	{
		RunsafePlayer player = event.getPlayer();
		if (this.bankHandler.isViewingBank(player))
			this.bankHandler.closePlayerBank(player);
	}

	private BankHandler bankHandler;
}
