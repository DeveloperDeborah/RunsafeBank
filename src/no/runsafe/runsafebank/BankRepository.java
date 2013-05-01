package no.runsafe.runsafebank;

import no.runsafe.framework.database.IDatabase;
import no.runsafe.framework.database.Repository;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.inventory.RunsafeInventory;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankRepository extends Repository
{
	public BankRepository(IDatabase database)
	{
		this.database = database;
	}

	@Override
	public String getTableName()
	{
		return "runsafeBanks";
	}

	public RunsafeInventory get(RunsafePlayer player)
	{
		Map<String, Object> data = database.QueryRow(
			"SELECT bankInventory FROM runsafeBanks WHERE playerName=?",
			player.getName()
		);

		RunsafeInventory inventory = RunsafeServer.Instance.createInventory(null, 54, "Bank");

		if (data != null)
			inventory.unserialize((String) data.get("bankInventory"));

		return inventory;
	}

	public void update(RunsafePlayer player, RunsafeInventory inventory)
	{
		String inventoryString = inventory.serialize();
		database.Execute(
			"INSERT INTO `runsafeBanks` (playerName, bankInventory) VALUES(?,?) " +
				"ON DUPLICATE KEY UPDATE bankInventory = ?",
			player.getName(), inventoryString, inventoryString
		);
	}

	@Override
	public HashMap<Integer, List<String>> getSchemaUpdateQueries()
	{
		HashMap<Integer, List<String>> versions = new HashMap<Integer, List<String>>();
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
}
