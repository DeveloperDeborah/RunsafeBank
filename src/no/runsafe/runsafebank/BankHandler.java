package no.runsafe.runsafebank;

import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.event.plugin.IPluginDisabled;
import no.runsafe.framework.minecraft.inventory.RunsafeInventory;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankHandler implements IPluginDisabled
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

	private void forceBanksShut()
	{
		for (Map.Entry<String, RunsafeInventory> bank : this.loadedBanks.entrySet())
		{
			for (RunsafePlayer viewer : bank.getValue().getViewers())
			{
				viewer.sendColouredMessage("&cServer restarting, you have been forced out of your bank.");
				viewer.closeInventory();
			}
		}
	}

	@Override
	public void OnPluginDisabled()
	{
		this.output.write("Shutdown detected, forcing save of all loaded banks.");
		this.forceBanksShut();
		this.saveLoadedBanks();
	}

	private HashMap<String, RunsafeInventory> loadedBanks = new HashMap<String, RunsafeInventory>();
	private BankRepository bankRepository;
	private IOutput output;
}
