package no.runsafe.runsafebank;

import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.database.IDatabase;
import no.runsafe.framework.api.database.Repository;
import no.runsafe.framework.minecraft.inventory.RunsafeInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class BankRepository extends Repository
{
	public BankRepository(IDatabase database, IServer server)
	{
		this.database = database;
		this.server = server;
	}

	@Override
	public String getTableName()
	{
		return "runsafeBanks";
	}

	public RunsafeInventory get(String playerName)
	{
		RunsafeInventory inventory = server.createInventory(null, 54, String.format("%s's Bank Vault", playerName));

		String serialized = database.queryString(
			"SELECT bankInventory FROM runsafeBanks WHERE playerName=?",
			playerName
		);
		if (serialized != null)
			inventory.unserialize(serialized);

		return inventory;
	}

	public void update(String bankOwner, RunsafeInventory inventory)
	{
		String inventoryString = inventory.serialize();
		database.execute(
			"INSERT INTO `runsafeBanks` (playerName, bankInventory) VALUES(?,?) " +
				"ON DUPLICATE KEY UPDATE bankInventory = ?",
			bankOwner, inventoryString, inventoryString
		);
	}

	@Override
	public HashMap<Integer, List<String>> getSchemaUpdateQueries()
	{
		HashMap<Integer, List<String>> versions = new LinkedHashMap<Integer, List<String>>(1);
		ArrayList<String> sql = new ArrayList<String>();
		sql.add(
			"CREATE TABLE `runsafeBanks` (" +
				"`playerName` varchar(50) NOT NULL," +
				"`bankInventory` longtext," +
				"PRIMARY KEY (`playerName`)" +
				")"
		);
		versions.put(1, sql);

		return versions;
	}

	private IDatabase database;
	private final IServer server;
}
