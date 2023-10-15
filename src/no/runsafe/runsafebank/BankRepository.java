package no.runsafe.runsafebank;

import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.database.ISchemaUpdate;
import no.runsafe.framework.api.database.Repository;
import no.runsafe.framework.api.database.SchemaUpdate;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.inventory.RunsafeInventory;

import javax.annotation.Nonnull;

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
		RunsafeInventory inventory = server.createInventory(null, 54, String.format("%s's Bank Vault", player.getName()));

		String serialized = database.queryString(
			"SELECT bankInventory FROM runsafeBanks WHERE player=?",
			player.getUniqueId().toString()
		);
		if (serialized != null)
			inventory.unserialize(serialized);

		return inventory;
	}

	public void update(IPlayer bankOwner, RunsafeInventory inventory)
	{
		String inventoryString = inventory.serialize();
		database.execute(
			"INSERT INTO `runsafeBanks` (player, bankInventory) VALUES(?,?) " +
				"ON DUPLICATE KEY UPDATE bankInventory = ?",
			bankOwner.getUniqueId().toString(), inventoryString, inventoryString
		);
	}

	public void clear(IPlayer bankOwner)
	{
		database.execute("DELETE FROM `runsafeBanks` WHERE `player` = ?",
			bankOwner.getUniqueId().toString()
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

		// Clean up empty banks before updating UUIDs to reduce issues.
		update.addQueries(String.format("DELETE FROM `%s` where `bankInventory` = 'contents: {}\n'", getTableName()));

		update.addQueries( // Update UUIDs
			String.format(
				"UPDATE IGNORE `%s` SET `player` = " +
					"COALESCE((SELECT `uuid` FROM player_db WHERE `name`=`%s`.`player`), `player`) " +
					"WHERE length(`player`) != 36",
				getTableName(), getTableName()
			)
		);

		return update;
	}

	private final IServer server;
}
