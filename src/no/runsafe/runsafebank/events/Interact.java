package no.runsafe.runsafebank.events;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.block.IBlock;
import no.runsafe.framework.api.event.player.IPlayerInteractEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerInteractEvent;
import no.runsafe.runsafebank.BankHandler;

import java.util.ArrayList;
import java.util.List;

public class Interact implements IPlayerInteractEvent, IConfigurationChanged
{
	public Interact(BankHandler bankHandler)
	{
		this.bankHandler = bankHandler;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		worlds.clear();
		worlds.addAll(config.getConfigValueAsList("worlds"));
	}

	@Override
	public void OnPlayerInteractEvent(RunsafePlayerInteractEvent event)
	{
		IBlock block = event.getBlock();
		if (block == null || !event.isRightClick() || !block.is(Item.Decoration.EnderChest))
			return;

		IPlayer player = event.getPlayer();

		if (!player.hasPermission("runsafe.bank.use"))
			player.sendColouredMessage("&cYou do not have permissions to use the bank.");
		else if (!worlds.contains(player.getWorldName()))
			player.sendColouredMessage("&cYou can not use the bank in this world.");
		else this.bankHandler.openBank(player, player);

		event.cancel();
	}

	private final BankHandler bankHandler;
	private final List<String> worlds = new ArrayList<>(0);
}
