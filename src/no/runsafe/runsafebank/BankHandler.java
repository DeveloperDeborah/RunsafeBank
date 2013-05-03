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

	public String getViewingBank(RunsafePlayer player)
	{
		if (this.isViewingBank(player))
			return this.bankingPlayers.get(player.getName());

		return null;
	}

	public void savePlayerBank(String bankOwner, RunsafeInventory bank)
	{
		this.bankRepository.update(bankOwner, bank);
	}

	public void closePlayerBank(RunsafePlayer viewer)
	{
		this.bankingPlayers.remove(viewer.getName());
	}

	private HashMap<String, String> bankingPlayers;
	private BankRepository bankRepository;
}
