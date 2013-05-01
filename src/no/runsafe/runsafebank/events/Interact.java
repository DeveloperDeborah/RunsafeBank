package no.runsafe.runsafebank.events;

import no.runsafe.framework.event.player.IPlayerInteractEvent;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.event.player.RunsafePlayerInteractEvent;

import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.runsafebank.BankRepository;
import org.bukkit.Material;

public class Interact implements IPlayerInteractEvent
{
	public Interact(BankRepository bankRepository, IOutput output)
	{
		this.output = output;
		this.bankRepository = bankRepository;
	}

	@Override
	public void OnPlayerInteractEvent(RunsafePlayerInteractEvent event)
	{
		if (event.getBlock().getTypeId() == Material.ENDER_CHEST.getId())
		{
			RunsafePlayer player = event.getPlayer();
			if (player.hasPermission("runsafe.bank.use"))
				player.openInventory(this.bankRepository.get(player));

			event.setCancelled(true);
		}
	}

	private IOutput output;
	private BankRepository bankRepository;
}
