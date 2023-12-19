package no.runsafe.runsafebank;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;

import java.util.ArrayList;
import java.util.List;

public class Config implements IConfigurationChanged
{
	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		maxBankDataSize = config.getConfigValueAsInt("maxBankDataSize");
		worlds.clear();
		blacklistedItems.clear();
		worlds.addAll(config.getConfigValueAsList("worlds"));
		blacklistedItems.addAll(config.getConfigValueAsList("blacklistedItems"));

		Messages.overloadedWarning = config.getConfigValueAsString("message.overloadedWarning");
		Messages.noPermissions = config.getConfigValueAsString("message.noPermissions");
		Messages.wrongWorld = config.getConfigValueAsString("message.wrongWorld");
		Messages.serverRestarting = config.getConfigValueAsString("message.serverRestarting");
		Messages.itemNotAllowed = config.getConfigValueAsString("message.itemNotAllowed");

		Messages.commandDeleteBank = config.getConfigValueAsString("message.command.deleteBank");
	}

	public static final class Messages
	{
		public static String getOverloadedWarning()
		{
			return overloadedWarning;
		}

		public static String getNoPermissions()
		{
			return noPermissions;
		}

		public static String getWrongWorld()
		{
			return wrongWorld;
		}

		public static String getServerRestarting()
		{
			return serverRestarting;
		}

		public static String getCommandDeleteBank()
		{
			return commandDeleteBank;
		}

		public static String getItemNotAllowed()
		{
			return itemNotAllowed;
		}

		private static String overloadedWarning;
		private static String noPermissions;
		private static String wrongWorld;
		private static String serverRestarting;
		private static String itemNotAllowed;

		private static String commandDeleteBank;
	}

	public static int getMaxBankDataSize()
	{
		return maxBankDataSize;
	}

	public static boolean isBankWorld(IWorld world)
	{
		if (world == null)
			return false;

		return worlds.contains(world.getName());
	}

	public static boolean isBlacklistedItem(RunsafeMeta item)
	{
		if (item == null)
			return false;

		return blacklistedItems.contains(item.getNormalName());
	}

	private static int maxBankDataSize;
	private static final List<String> worlds = new ArrayList<>(0);
	private static final List<String> blacklistedItems = new ArrayList<>();
}
