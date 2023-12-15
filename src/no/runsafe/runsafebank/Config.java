package no.runsafe.runsafebank;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;

import java.util.ArrayList;
import java.util.List;

public class Config implements IConfigurationChanged
{
	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		maxBankDataSize = config.getConfigValueAsInt("maxBankDataSize");
		worlds.clear();
		worlds.addAll(config.getConfigValueAsList("worlds"));

		Messages.overloadedWarning = config.getConfigValueAsString("overloadedWarningMessage");
	}

	public static final class Messages
	{
		public static String getOverloadedWarning()
		{
			return overloadedWarning;
		}

		private static String overloadedWarning;
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

	private static int maxBankDataSize;
	private static final List<String> worlds = new ArrayList<>(0);
}
