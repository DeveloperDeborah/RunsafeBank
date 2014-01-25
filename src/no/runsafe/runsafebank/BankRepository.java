package no.runsafe.runsafebank;

import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.database.IDatabase;
import no.runsafe.framework.api.database.ISchemaUpdate;
import no.runsafe.framework.api.database.Repository;
import no.runsafe.framework.api.database.SchemaUpdate;
import no.runsafe.framework.minecraft.inventory.RunsafeInventory;

public class BankRepository extends Repository
{
	public BankRepository(IServer server)
	{
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
	public ISchemaUpdate getSchemaUpdateQueries()
	{
		ISchemaUpdate update = new SchemaUpdate();

		update.addQueries(
			"CREATE TABLE `runsafeBanks` (" +
				"`playerName` varchar(50) NOT NULL," +
				"`bankInventory` longtext," +
				"PRIMARY KEY (`playerName`)" +
			")"
		);

		return update;
	}

	private final IServer server;
}
