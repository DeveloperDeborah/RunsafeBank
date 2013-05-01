package no.runsafe.runsafebank.events;

import no.runsafe.framework.event.inventory.IInventoryClosed;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.event.inventory.RunsafeInventoryCloseEvent;
import no.runsafe.runsafebank.BankRepository;

public class CloseInventory implements IInventoryClosed
{
	public CloseInventory(BankRepository bankRepository, IOutput output)
	{
		this.bankRepository = bankRepository;
		this.output = output;
	}

	@Override
	public void OnInventoryClosed(RunsafeInventoryCloseEvent event)
	{
		this.output.write(event.getView().getType().name());
	}

	private BankRepository bankRepository;
	private IOutput output;
}
