package no.runsafe.runsafebank;

import no.runsafe.framework.api.event.plugin.IPluginDisabled;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.Sound;
import no.runsafe.framework.minecraft.inventory.RunsafeInventory;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

public class BankHandler implements IPluginDisabled
{
	public BankHandler(BankRepository bankRepository)
	{
		this.bankRepository = bankRepository;

		Plugin.Scheduler.startAsyncRepeatingTask(this::saveLoadedBanks, 60, 60);
	}

	public void openBank(IPlayer viewer, IPlayer owner)
	{
		if (!this.loadedBanks.containsKey(owner))
			this.loadBank(owner);

		viewer.openInventory(this.loadedBanks.get(owner));
		viewer.playSound(Sound.Chest.EnderChestOpen);
		Plugin.Debugger.debugFine(String.format("Opening %s's bank for %s", owner.getName(), viewer.getName()));
	}

	public void clearBank(IPlayer bankOwner)
	{
		loadedBanks.remove(bankOwner);
		bankRepository.clear(bankOwner);
		Plugin.Debugger.debugFine(String.format("Deleted %s's bank", bankOwner.getName()));
	}

	public boolean isViewingBank(IPlayer inventoryViewer)
	{
		if (loadedBanks.isEmpty() || inventoryViewer == null)
			return false;

		for (Map.Entry<IPlayer, RunsafeInventory> bank : this.loadedBanks.entrySet())
			for (IPlayer bankViewer : bank.getValue().getViewers())
				if (bankViewer.equals(inventoryViewer))
					return true;

		return false;
	}

	private void loadBank(IPlayer owner)
	{
		loadedBanks.put(owner, bankRepository.get(owner));
		Plugin.Debugger.debugFine("Loaded bank from database for " + owner.getName());
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
			if (bankDataSize > Config.getMaxBankDataSize())
			{
				Plugin.Console.logInformation(
					"Player attempted to exceed size limit of bank. Player: " + bankOwner.getName() +
					" Size: " + bankDataSize
				);
				Plugin.Debugger.debugFine(
					"Bank inv size too big. Could not save bank for: " + bankOwner.getName() +
					" Size: " + bankDataSize
				);
				suspiciousBanks.add(bankOwner);
				continue;
			}

			this.bankRepository.update(bankOwner, bankInventory);

			Plugin.Debugger.debugFine("Saved bank to database: " + bankOwner.getName());

			if (bankInventory.getViewers().isEmpty())
				oldBanks.add(bankOwner);
		}

		for (IPlayer suspect: suspiciousBanks)
		{
			suspect.sendColouredMessage(Config.Messages.getOverloadedWarning());
			suspect.closeInventory();
			suspect.playSound(Sound.Creature.Illager.Illusion.CastSpell);
			loadedBanks.remove(suspect);
		}

		for (IPlayer owner: oldBanks)
		{
			this.loadedBanks.remove(owner);
			Plugin.Debugger.debugFine("Removing silent bank reference for GC: " + owner.getName());
		}
	}

	private void forceBanksShut()
	{
		for (Map.Entry<IPlayer, RunsafeInventory> bank : this.loadedBanks.entrySet())
		{
			for (IPlayer viewer : bank.getValue().getViewers())
			{
				viewer.sendColouredMessage(Config.Messages.getServerRestarting());
				viewer.closeInventory();
			}
		}
	}

	@Override
	public void OnPluginDisabled()
	{
		Plugin.Console.logInformation("Shutdown detected, forcing save of all loaded banks.");
		this.forceBanksShut();
		this.saveLoadedBanks();
	}

	private final ConcurrentHashMap<IPlayer, RunsafeInventory> loadedBanks = new ConcurrentHashMap<>();
	private final BankRepository bankRepository;
}
