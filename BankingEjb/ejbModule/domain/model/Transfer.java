package domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;


@Entity
@NamedQueries({
		@NamedQuery(name = "alltransfers", query = "select t FROM Transfer t"),
		@NamedQuery(name = "alltransfersbyaccount", query = "select t FROM Transfer t where t.account = :account")
		 })


public class Transfer implements Serializable {

	private static final long serialVersionUID = -427673619312214601L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private int id;
	
	private Date date;
	private double amount;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;
	
	@OneToOne
	@JoinColumn(name = "from_account")
	private Account fromAccount;
	
	@OneToOne
	@JoinColumn(name = "to_account")
	private Account toAccount;
	
	public Transfer(){
		super();
	}
	
	public Transfer(Date date, double amount, Account account, Account fromAccount, Account toAccount){
		super();
		this.date = date;
		this.amount = amount;
		this.account = account;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
	}
	
	public Transfer(double amount, Account account, Account fromAccount, Account toAccount){
		super();
		this.date = new Date();
		this.amount = amount;
		this.account = account;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
	}
	
	public Account getAccount(){
		return account;
	}
	
	public void setAccount(Account account){
		this.account = account;
	}
	
	public Account getToAccount(){
		return toAccount;
	}
	
	public void setToAccount(Account account){
		this.toAccount = account;
	}
	
	public Account getFromAccount(){
		return fromAccount;
	}
	
	public void setFromAccount(Account account){
		this.fromAccount = account;
	}
	
	@Override
	public String toString() {
		return "Account [id=" + id + ", date=" + date
				+ ", amount=" + amount + ", account=" + account
				+ ", fromAccount=" + fromAccount + ", toAccount="
				+ toAccount + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	
	
	

}
