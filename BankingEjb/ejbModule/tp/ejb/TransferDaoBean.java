package tp.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tp.ejb.utils.Paging;
import domain.model.Account;
import domain.model.Transfer;

import java.util.Date;


@Stateless
@LocalBean
public class TransferDaoBean implements TransferDao, Serializable{

	private static final long serialVersionUID = 4085695733239327692L;
	
	@PersistenceContext(unitName = "BankingPU")
	private EntityManager em;
	
	@EJB
	AccountDao accountDao;
	
	public TransferDaoBean(){
		super();
		System.out.println("creating TransferDaoBean");
	}


	
	public void testTransfer(Account account){
		
	}
	
	@Override
	public List<Transfer> getList(Account a) {
		return em.createNamedQuery("alltransfersbyaccount").setParameter("account", a).getResultList();
	}
	
	public Transfer add(Account a, Transfer t) {
		a.addTransfer(t);
		if (find(a,t.getId()) == null) {
			em.merge(t);
			em.merge(a);
			return t;
		} else
			return null;
	}
	
	public Transfer find(Account a, int id) {
		for (Transfer t : getList(a)) {
			if (t.getId() == id)
				return t;
		}
		return null;
	}
	
	public Transfer find(Transfer t) {
		return em.find(Transfer.class, t.getId());
	}
	
	public Transfer delete(Account c, Transfer a) {
		List<Transfer> l = getList(c);
		Transfer result = Paging.prior(l, a);
		if (result == null)
			result = Paging.next(l, a);
		c.removeTransfer(a);
		em.merge(a);
		em.merge(c);
		return result;
	}
	
	public Transfer first(Account c) {
		List<Transfer> l = getList(c);
		if (l.size() > 0)
			return l.iterator().next();
		else
			return null;
	}
	
	public Transfer last(Account c) {
		List<Transfer> l = getList(c);
		if (l.size() > 0)
			return l.get(l.size() - 1);
		else
			return null;
	}
	
	public Transfer prior(Account c, Transfer a) {
		return Paging.prior(getList(c), a);
	}

	public Transfer next(Account c, Transfer a) {
		return Paging.next(getList(c), a);
	}
	
	public Transfer clone(Transfer a) {
		Transfer transfer = create(a.getAccount());
		transfer.setAmount(a.getAmount());
		transfer.setDate(a.getDate());
		transfer.setFromAccount(a.getFromAccount());
		transfer.setToAccount(a.getToAccount());
		em.persist(transfer);
		return transfer;
	}
	
	public Transfer create(Account c) {
		Transfer transfer = new Transfer();
		transfer.setAccount(c);
		transfer.setAmount(0);
		transfer.setDate(new Date());
		transfer.setFromAccount(null);
		transfer.setToAccount(null);
	    c.addTransfer(transfer);
	    em.persist(transfer);
		em.merge(transfer);
		em.merge(c);
		return transfer;
	}
	
	public Transfer merge(Account c, Transfer a) {
		if (find(c,a.getId()) != null) {
			em.merge(a);
			return a;
		} else
			return null;
	}
	
	public Transfer merge(Transfer currentTransfer) {
		if (find(currentTransfer) != null) {
			em.merge(currentTransfer);
			return currentTransfer;
		} else
			return null;
		
	}
	
	public Transfer foobar(Transfer a) {
		boolean tb = true;
		return a;
	}
	
	public Transfer createTransfer(Account from, Account to, double amount) {
		Transfer transfer = create(to);
		
		transfer.setAmount(amount);
		transfer.setFromAccount(from);
		transfer.setToAccount(to);
		
		em.merge(transfer);
		return transfer;
	}



	public List<Transfer> populate(Account a) {
		Transfer t = create(a);
		t.setAmount(10);
		em.merge(t);
		return new ArrayList<Transfer>(a.getTransfers());
	}
	
	public List<Transfer> populate(Account a, int amount) {
		Transfer t = create(a);
		t.setAmount(amount);
		em.merge(t);
		return new ArrayList<Transfer>(a.getTransfers());
	}



	


}
