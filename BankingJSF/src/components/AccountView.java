package components;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;

@ManagedBean(name = "accountView", eager = true)
@SessionScoped
public class AccountView implements Serializable{

	private static final long serialVersionUID = 3448316477093187795L;

	private HtmlInputText accountNumber;
	private HtmlInputText interestRate;
	private HtmlInputText overdraftPenalty;
	private HtmlInputText overdraftLimit;
	
	public HtmlInputText getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(HtmlInputText accountNumber) {
		this.accountNumber = accountNumber;
	}
	public HtmlInputText getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(HtmlInputText interestRate) {
		this.interestRate = interestRate;
	}
	public HtmlInputText getOverdraftPenalty() {
		return overdraftPenalty;
	}
	public void setOverdraftPenalty(HtmlInputText overdraftPenalty) {
		this.overdraftPenalty = overdraftPenalty;
	}
	public HtmlInputText getOverdraftLimit() {
		return overdraftLimit;
	}
	public void setOverdraftLimit(HtmlInputText overdraftLimit) {
		this.overdraftLimit = overdraftLimit;
	}

	

	
}
