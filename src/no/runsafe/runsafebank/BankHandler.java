package no.runsafe.runsafebank;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.event.plugin.IPluginDisabled;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.log.IDebug;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.inventory.RunsafeInventory;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

public class BankHandler implements IPluginDisabled, IConfigurationChanged
{
	public BankHandler(BankRepository bankRepository, IDebug output, IScheduler scheduler, IConsole console)
	{
		this.bankRepository = bankRepository;
		this.debugger = output;
		this.console = console;

		scheduler.startAsyncRepeatingTask(this::saveLoadedBanks, 60, 60);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		this.maxBankDataSize = config.getConfigValueAsInt("maxBankDataSize");
	}

	public void openBank(IPlayer viewer, IPlayer owner)
	{
		if (!this.loadedBanks.containsKey(owner))
			this.loadBank(owner);

		viewer.openInventory(this.loadedBanks.get(owner));
		debugger.debugFine(String.format("Opening %s's bank for %s", owner.getName(), viewer.getName()));
	}

	public void clearBank(IPlayer bankOwner)
	{
		loadedBanks.remove(bankOwner);
		bankRepository.clear(bankOwner);
		debugger.debugFine(String.format("Deleted %s's bank", bankOwner.getName()));
	}

	private void loadBank(IPlayer owner)
	{
		loadedBanks.put(owner, bankRepository.get(owner));
		debugger.debugFine("Loaded bank from database for " + owner.getName());
	}

	private void saveLoadedBanks()
	{
		List<IPlayer> oldBanks = new ArrayList<>();
		List<IPlayer> suspiciousBanks = new ArrayList<>();
		for (Map.Entry<IPlayer, RunsafeInventory> bank : this.loadedBanks.entrySet())
		{
			RunsafeInventory bankInventory = bank.getValue();
			IPlayer bankOwner = bank.getKey();
			int bankDataSize = bankInventory.serialize().length();
			if (bankDataSize > maxBankDataSize)
			{
				this.console.logInformation(
					"Player attempted to exceed size limit of bank. Player: " + bankOwner.getName() +
					" Size: " + bankDataSize
				);
				this.debugger.debugFine(
					"Bank inv size too big. Could not save bank for: " + bankOwner.getName() +
					" Size: " + bankDataSize
				);
				suspiciousBanks.add(bankOwner);
				continue;
			}

			this.bankRepository.update(bankOwner, bankInventory);

			this.debugger.debugFine("Saved bank to database: " + bankOwner.getName());

			if (bankInventory.getViewers().isEmpty())
				oldBanks.add(bankOwner);
		}

		for (IPlayer suspect: suspiciousBanks)
		{
			suspect.sendColouredMessage("&cAttempted to exceed bank storage size. Contact a staff member for more information.");
			suspect.closeInventory();
			loadedBanks.remove(suspect);
		}

		for (IPlayer owner: oldBanks)
		{
			this.loadedBanks.remove(owner);
			this.debugger.debugFine("Removing silent bank reference for GC: " + owner.getName());
		}
	}

	private void forceBanksShut()
	{
		for (Map.Entry<IPlayer, RunsafeInventory> bank : this.loadedBanks.entrySet())
		{
			for (IPlayer viewer : bank.getValue().getViewers())
			{
				viewer.sendColouredMessage("&cServer restarting, you have been forced out of your bank.");
				viewer.closeInventory();
			}
		}
	}

	@Override
	public void OnPluginDisabled()
	{
		this.console.logInformation("Shutdown detected, forcing save of all loaded banks.");
		this.forceBanksShut();
		this.saveLoadedBanks();
	}

	private int maxBankDataSize;
	private final ConcurrentHashMap<IPlayer, RunsafeInventory> loadedBanks = new ConcurrentHashMap<>();
	private final BankRepository bankRepository;
	private final IDebug debugger;
	private final IConsole console;
}
