package tp.ejb.test;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import domain.model.Account;
import domain.model.Bank;
import domain.model.City;
import domain.model.Customer;
import domain.model.Transfer;
import tp.ejb.AccountDao;
import tp.ejb.CityDao;

//import org.hibernate.collection.internal.PersistentBag;



/**
 * Bean sans etat assurant le dialogue cote serveur avec les beans entites .
 * 
 * @author pfister
 */
@Stateless
@LocalBean
public class TestFacade implements TestFacadeRemote {


	@PersistenceContext(unitName = "BankingPU")
	private EntityManager em;	

	@EJB
	CityDao citydao;

	@EJB
	private AccountDao accountDao;


	private void log(String mesg){
		System.out.println("s*  "+mesg);
	}


	public Bank find(Bank b) {
		return em.find(Bank.class, b.getId());
	}

	public void add(Bank b) {
		if (find(b) == null)
			em.persist(b);
	}

	public void populate() {

		//1 on crée les villes
		City c1 = citydao.create();
		c1.setName("Nîmes");
		c1.setCountry("France");
		c1.setZipCode("30000");
		citydao.merge(c1);

		City c2 = new City("Montpellier", "34000", "France");
		em.persist(c2);
		City c3 = new City("Alès", "30000", "France");
		em.persist(c3);

		//2 on crée une banque, 3 clients (2 étant la banque) et les comptes associés
		List<Bank> bqs = getBanks();
		if (bqs.size() > 2)
			return;

		Bank b = new Bank("Credit Arboricole", "3 rue des sapins", "75000",
				"5646456");
		em.persist(b);

		Customer ca1=new Customer("Caisse", "Credit Arboricole", "3 rue des sapins",
				"75000");

		Customer ca2=new Customer("Banque", "Credit Arboricole", "3 rue des sapins",
				"75000");

		Customer ca=new Customer("Dupont", "Jean", "10, rue des lilas",
				"30000");

		ca1.setCity(c1);
		ca2.setCity(c1);
		ca.setCity(c1);
		em.persist(ca1);
		em.persist(ca2);
		em.persist(ca);
		//testAccount(ca1);
		//testAccount(ca2);
		//testAccount(ca);

		//on ajoute les clients à la banque
		//	List<Customer> custs=b.getCustomers();
		b.addCustomer(ca);
		b.addCustomer(ca1); 
		b.addCustomer(ca2);

		b.addCustomer(new Customer("Poulain", "Amélie", "20, rue des mimosas",
				"34000"));

		em.persist(b);
		
		//On crée des comptes aux clients :
		Account a1 = new Account("100", ca1, 100, 5, 5, 100);
		Account a2 = new Account("100", ca2, 100, 5, 5, 100);
		Account a = new Account("100", ca, 100, 5, 5, 100);

		//On crée des transferts entre les comptes
		Transfer t1 = new Transfer(100, a, a, a1);
		Transfer t2 = new Transfer(-100, a1, a1, a);

		//On crée une deuxième banque
		b = new Bank("Internet Bank", "10, avenue de la Californie", "13000",
				"5646456");
		b.addCustomer(new Customer("Caisse", "Internet Bank",
				"10, avenue de la Californie", "13000"));
		b.addCustomer(new Customer("Banque", "Internet Bank",
				"10, avenue de la Californie", "13000"));
		b.addCustomer(new Customer("Durand", "Jacques",
				"10, rue des violettes", "32000"));
		b.addCustomer(new Customer("Harry", "Cover", "20, rue des salsifis",
				"31000"));
		em.persist(b);



		// should be done by the server
		em.joinTransaction();

		List<Bank>  banks=getBanks();
		for (Bank bank : banks) {
			log(bank.toString());
			List<Customer> customers =bank.getCustomers();
			for (Customer customer : customers) {
				log(customer.toString());
				List<Account> accounts=customer.getAccounts();
				for (Account account : accounts) { //il faudrait utiliser les DAO et ne pas taper directement dans les entity
					try {
						log("compte "+account.getAccountNumber()+" solde="+account.getBalance());
					} catch (Exception e) {
						log("erreur pendant l'interrogation du compte, la banque "+bank.getName()+" vous présente ses excuses");
					}

				}
			}
		}


	}


	private void testAccount(Customer c) {

		Account acnt0 = accountDao.createAccount(c, 100, 10);

		log("account created " + acnt0.getAccountNumber());

		Account acnt1 = accountDao.createAccount(c, 0, 0);
		log("account created " + acnt1.getAccountNumber());

		accountDao.creditAccount(acnt0, 1000);
		accountDao.creditAccount(acnt1, 10000);
		log("balance of " + acnt0.getAccountNumber() + " is "
				+ acnt0.getBalance());
		log("balance of " + acnt1.getAccountNumber() + " is "
				+ acnt1.getBalance());
		double totransfer = 500;
		log("transfer of " + totransfer + " from " + acnt0.getAccountNumber()
				+ " to " + acnt1.getAccountNumber());
		try {
			accountDao.transferAccount(acnt0, acnt1, totransfer);
			log("balance of " + acnt0.getAccountNumber() + " is "
					+ acnt0.getBalance());
			log("balance of " + acnt1.getAccountNumber() + " is "
					+ acnt1.getBalance());

		} catch (Exception e) {
			log("rolled back transfer of " + totransfer + " from "
					+ acnt0.getAccountNumber() + " to "
					+ acnt1.getAccountNumber() + " " + e.getMessage());
		}
	}

	/**
	 * @return la liste des Banks
	 */
	@SuppressWarnings("unchecked")
	public List<Bank> getBanks() {
		return em.createNamedQuery("allbanks").getResultList();
	}

	/**
	 * @return la liste des Customers
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> getCustomers() {
		return em.createNamedQuery("allcustomers").getResultList();
	}

	public void delete(Bank b) {
		// TODO vérifier si c'est nécessaire de supprimer les customers d'une
		// banque

		b = em.find(Bank.class, b.getId());
		/*
		 * Collection<Customer> bcusts = b.getCustomers(); //est-ce nécessaire ??
		 * for (Customer customer : bcusts) { em.remove(customer); }
		 */
		em.remove(b);
	}

	public void delete(Customer c) {
		c = em.find(c.getClass(), c.getId());
		c.getBank().getCustomers().remove(c);
		em.remove(c);
	}

	public void delete(City c) {
		c = em.find(c.getClass(), c.getId());
		em.remove(c);
	}

	public List<City> getCities() {
		return em.createNamedQuery("allcities").getResultList();
	}

	public void save(Customer customer) {
		em.merge(customer);
		em.joinTransaction();	
	}

}