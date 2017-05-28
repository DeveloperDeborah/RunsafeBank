package no.runsafe.runsafebank;

import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.database.ISchemaUpdate;
import no.runsafe.framework.api.database.Repository;
import no.runsafe.framework.api.database.SchemaUpdate;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.inventory.RunsafeInventory;

import javax.annotation.Nonnull;
import java.util.UUID;

public class BankRepository extends Repository
{
	public BankRepository(IServer server)
	{
		this.server = server;
	}

	@Nonnull
	@Override
	public String getTableName()
	{
		return "runsafeBanks";
	}

	public RunsafeInventory get(IPlayer player)
	{
		String playerName = player.getName();
		RunsafeInventory inventory = server.createInventory(null, 54, String.format("%s's Bank Vault", playerName));

		String serialized = database.queryString(
			"SELECT bankInventory FROM runsafeBanks WHERE player=?",
			playerName
		);
		if (serialized != null)
			inventory.unserialize(serialized);

		return inventory;
	}

	public void update(UUID bankOwner, RunsafeInventory inventory)
	{
		String ownerName = server.getPlayer(bankOwner).getName();
		String inventoryString = inventory.serialize();
		database.execute(
			"INSERT INTO `runsafeBanks` (player, bankInventory) VALUES(?,?) " +
				"ON DUPLICATE KEY UPDATE bankInventory = ?",
			ownerName, inventoryString, inventoryString
		);
	}

	@Nonnull
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
		update.addQueries("ALTER TABLE `runsafeBanks` CHANGE `playerName` `player` varchar(50) NOT NULL");

		return update;
	}

	private final IServer server;
}
