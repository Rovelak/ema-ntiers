package components;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;

@ManagedBean(name = "transferView", eager = true)
@SessionScoped
public class TransferView implements Serializable{

	private static final long serialVersionUID = 8141196037911072310L;

	private HtmlInputText date;
	private HtmlInputText amount;
	private HtmlInputText fromAccount;
	private HtmlInputText toAccount;
	
	public HtmlInputText getDate() {
		return date;
	}
	public void setDate(HtmlInputText date) {
		this.date = date;
	}
	public HtmlInputText getAmount() {
		return amount;
	}
	public void setAmount(HtmlInputText amount) {
		this.amount = amount;
	}
	public HtmlInputText getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(HtmlInputText fromAccount) {
		this.fromAccount = fromAccount;
	}
	public HtmlInputText getToAccount() {
		return toAccount;
	}
	public void setToAccount(HtmlInputText toAccount) {
		this.toAccount = toAccount;
	}
}
