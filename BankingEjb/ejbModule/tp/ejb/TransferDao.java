package tp.ejb;

import java.util.List;

import javax.ejb.Remote;

import domain.model.Account;
import domain.model.Customer;
import domain.model.Transfer;

@Remote
public interface TransferDao {
	
	List<Transfer> getList(Account c);

	 void testTransfer(Account account);
	 Transfer add(Account a, Transfer t);
	 Transfer find(Account a, int id) ;
	 Transfer delete(Account c, Transfer a);
	 Transfer first(Account c);
	 Transfer last(Account c);
	 Transfer prior(Account c, Transfer a);
	 Transfer next(Account c, Transfer a);
	 Transfer clone(Transfer a);
	 Transfer create(Account c);
	 Transfer merge(Account c, Transfer a);
	 Transfer foobar(Transfer a) ;
	 Transfer createTransfer(Account from, Account to, double amount);
	 
	 List<Transfer> populate(Account c);
	 Transfer find(Transfer t);
	 Transfer merge(Transfer currentTransfer);
	 List<Transfer> populate(Account a, int amount);
}
