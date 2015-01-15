package beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import components.AccountView;
import domain.model.Account;
import domain.model.Customer;
import tp.ejb.AccountDaoBean;

@ManagedBean(name = "accountWeb")
@SessionScoped
public class AccountWeb implements Serializable{

	private static final long serialVersionUID = 2208562223414727822L;

	@EJB
	private AccountDaoBean accountDao;
	
	@ManagedProperty(value = "#{customerWeb}")
	private CustomerWeb customerWeb;
	
	public CustomerWeb getCustomerWeb() {
		return customerWeb;
	}

	public void setCustomerWeb(CustomerWeb customerWeb) {
		this.customerWeb = customerWeb;
	}
	
	@ManagedProperty(value = "#{accountView}")
	private AccountView accountView;

	public AccountView getAccountView() {
		return accountView;
	}

	public void setAccountView(AccountView accountView) {
		this.accountView = accountView;
	}
	
	
	private Account currentAccount;
	
	private int customerId;
	
	protected Object getBackingBean(String name){
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		
		ValueExpression expression = app.getExpressionFactory()
				.createValueExpression(context.getELContext(), name,
						Object.class);

		Object result = null;
		try {
			result = expression.getValue(context.getELContext());
		} catch (ELException e) {
			System.out.println("error " + e.toString());
		}
		
		return result;
	}
	
	public void menuValueChanged(ValueChangeEvent vce) {
		String oldValue = vce.getOldValue().toString(), newValue = vce
				.getNewValue().toString();
		System.out.println("Value changed from "
				+ (oldValue.isEmpty() ? "null" : oldValue) + " to " + newValue);
		
		HtmlInputText accountNumber = accountView.getAccountNumber();
	    HtmlInputText interestRate = accountView.getInterestRate();
		HtmlInputText overdraftPenalty  = accountView.getOverdraftPenalty();
		HtmlInputText overdraftLimit = accountView.getOverdraftLimit();
	}
	
	public void setCurrentAccount(Account currentAccount) {
		this.currentAccount = currentAccount;
	}

	public void modify() {
		accountDao.merge(currentAccount);
		MBUtils.redirect("customers.xhtml?id=" + customerId);
	}

	public void next() {
		setCurrentAccount(accountDao.next(getCustomerWeb().getCurrentCustomer(),
				getCurrentAccount()));
		MBUtils.redirect("customers.xhtml?id=" + customerId);
	}

	public void prior() {
		setCurrentAccount(accountDao.prior(getCustomerWeb().getCurrentCustomer(),
				getCurrentAccount()));
		MBUtils.redirect("customers.xhtml?id=" + customerId);
	}
	
	public void transfers(int accountId) {
		MBUtils.redirect("transfers.xhtml?id=" + accountId);
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		Customer c = new Customer();
		c.setId(customerId);
		getCustomerWeb().setCurrentCustomer(c);
		this.customerId = customerId;
		getAllAccounts();
	}

	public Account getCurrentAccount() {
	//	if (currentAccount == null || !currentAccount.getOwner().equals(getCustomerWeb().getCurrentCustomer()))
	//	customerWeb.getCurrentCustomer();
	//	currentAccount.getOwner();
		
	//	if (currentAccount == null || !currentAccount.getOwner().equals(customerWeb.getCurrentCustomer()))
	//		getAllAccounts();
		return currentAccount;
	}
	
	public List<Account> getAllAccounts() {
		Customer c = getCustomerWeb().getCurrentCustomer();
		List<Account> accounts = accountDao.getList(c);
	//	int siz = accounts.size();
	//	if (siz>0 && (currentAccount == null || !currentAccount.getOwner().equals(c)))
	//		currentAccount = accounts.get(0);
		return accounts;
	}

	public String getRowstyle(Account account) {
		if (account.equals(currentAccount))
			return "list-row-even";
		else
			return "list-row-odd";
	}


	
}
