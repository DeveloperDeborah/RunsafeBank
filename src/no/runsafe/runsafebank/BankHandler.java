package no.runsafe.runsafebank;

import no.runsafe.framework.server.inventory.RunsafeInventory;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.HashMap;

public class BankHandler
{
	public BankHandler(BankRepository bankRepository)
	{
		this.bankRepository = bankRepository;
		this.bankingPlayers = new HashMap<String, String>();
	}

	public void openPlayerBank(RunsafePlayer viewer, RunsafePlayer player)
	{
		this.bankingPlayers.put(viewer.getName(), player.getName());
		viewer.openInventory(this.bankRepository.get(player));
	}

	public boolean isViewingBank(RunsafePlayer player)
	{
		return this.bankingPlayers.containsKey(player.getName());
	}

	public void savePlayerBank(RunsafePlayer player, RunsafeInventory bank)
	{
		this.bankRepository.update(player, bank);
	}

	public void closePlayerBank(RunsafePlayer viewer)
	{
		this.bankingPlayers.remove(viewer.getName());
	}

	private HashMap<String, String> bankingPlayers;
	private BankRepository bankRepository;
}
