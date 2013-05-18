package no.runsafe.runsafebank;

import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.inventory.RunsafeInventory;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankHandler
{
	public BankHandler(BankRepository bankRepository, IOutput output, IScheduler scheduler)
	{
		this.bankRepository = bankRepository;
		this.output = output;

		scheduler.startAsyncRepeatingTask(new Runnable() {
			@Override
			public void run() {
				saveLoadedBanks();
			}
		}, 60, 60);
	}

	public void openBank(RunsafePlayer viewer, RunsafePlayer owner)
	{
		String ownerName = owner.getName();
		if (!this.loadedBanks.containsKey(ownerName))
			this.loadBank(ownerName);

		viewer.openInventory(this.loadedBanks.get(ownerName));
		this.output.fine(String.format("Opening %s's bank for %s", ownerName, viewer.getName()));
	}

	private void loadBank(String ownerName)
	{
		this.loadedBanks.put(ownerName, bankRepository.get(ownerName));
		this.output.fine("Loaded bank from database for " + ownerName);
	}

	private void saveLoadedBanks()
	{
		List<String> oldBanks = new ArrayList<String>();
		for (Map.Entry<String, RunsafeInventory> bank : this.loadedBanks.entrySet())
		{
			RunsafeInventory bankInventory = bank.getValue();
			String ownerName = bank.getKey();
			this.bankRepository.update(ownerName, bankInventory);
			this.output.fine("Saved bank to database: " + ownerName);

			if (bankInventory.getViewers().isEmpty())
				oldBanks.add(ownerName);
		}

		for (String ownerName : oldBanks)
		{
			this.loadedBanks.remove(ownerName);
			this.output.fine("Removing silent bank reference for GC: " + ownerName);
		}
	}

	private HashMap<String, RunsafeInventory> loadedBanks = new HashMap<String, RunsafeInventory>();
	private BankRepository bankRepository;
	private IOutput output;
}
