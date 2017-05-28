package no.runsafe.runsafebank;

import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.event.plugin.IPluginDisabled;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.log.IDebug;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.inventory.RunsafeInventory;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.List;
import java.util.Map;

public class BankHandler implements IPluginDisabled
{
	public BankHandler(BankRepository bankRepository, IDebug output, IScheduler scheduler, IConsole console, IServer server)
	{
		this.bankRepository = bankRepository;
		this.debugger = output;
		this.console = console;
		this.server = server;

		scheduler.startAsyncRepeatingTask(new Runnable()
		{
			@Override
			public void run()
			{
				saveLoadedBanks();
			}
		}, 60, 60);
	}

	public void openBank(IPlayer viewer, IPlayer owner)
	{
		UUID ownerUUID = owner.getUniqueId();
		if (!this.loadedBanks.containsKey(ownerUUID))
			this.loadBank(owner);

		viewer.openInventory(this.loadedBanks.get(ownerUUID));
		debugger.debugFine(String.format("Opening %s's bank for %s", owner.getName(), viewer.getName()));
	}

	private void loadBank(IPlayer owner)
	{
		loadedBanks.put(owner.getUniqueId(), bankRepository.get(owner));
		debugger.debugFine("Loaded bank from database for " + owner.getName());
	}

	private void saveLoadedBanks()
	{
		List<UUID> oldBanks = new ArrayList<UUID>();
		for (Map.Entry<UUID, RunsafeInventory> bank : this.loadedBanks.entrySet())
		{
			RunsafeInventory bankInventory = bank.getValue();
			UUID ownerUUID = bank.getKey();
			this.bankRepository.update(server.getPlayer(ownerUUID), bankInventory);

			String ownerName = server.getPlayer(ownerUUID).getName();
			this.debugger.debugFine("Saved bank to database: " + ownerName);

			if (bankInventory.getViewers().isEmpty())
				oldBanks.add(ownerUUID);
		}

		for (UUID ownerUUID : oldBanks)
		{
			this.loadedBanks.remove(ownerUUID);
			String ownerName = server.getPlayer(ownerUUID).getName();
			this.debugger.debugFine("Removing silent bank reference for GC: " + ownerName);
		}
	}

	private void forceBanksShut()
	{
		for (Map.Entry<UUID, RunsafeInventory> bank : this.loadedBanks.entrySet())
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

	private ConcurrentHashMap<UUID, RunsafeInventory> loadedBanks = new ConcurrentHashMap<UUID, RunsafeInventory>();
	private BankRepository bankRepository;
	private IDebug debugger;
	private IConsole console;
	private final IServer server;
}
