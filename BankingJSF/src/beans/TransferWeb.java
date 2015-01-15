package beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.event.ValueChangeEvent;

import components.TransferView;
import domain.model.Transfer;
import tp.ejb.AccountDaoBean;
import tp.ejb.TransferDaoBean;
import domain.model.Account;


@ManagedBean(name = "transferWeb")
@SessionScoped
public class TransferWeb implements Serializable{
	
	private static final long serialVersionUID = 7814811867970882679L;
	
	@EJB
	private TransferDaoBean transferDao;
	
	@ManagedProperty(value = "#{accountWeb}")
	private AccountWeb accountWeb;
	
	public AccountWeb getAccountWeb() {
		return accountWeb;
	}

	public void setAccountWeb(AccountWeb accountWeb) {
		this.accountWeb = accountWeb;
	}
	
	@ManagedProperty(value = "#{transferView}")
	private TransferView transferView;

	public TransferView getTransferView() {
		return transferView;
	}

	public void setTransferView(TransferView transferView) {
		this.transferView = transferView;
	}
	
	private Transfer currentTransfer;
	
	private int accountId;
	
	protected Object getAccountBean(String name){
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
		
		HtmlInputText date = transferView.getDate();
	    HtmlInputText amount = transferView.getAmount();
		HtmlInputText fromAccount  = transferView.getFromAccount();
		HtmlInputText toAccount = transferView.getToAccount();
	}
	
	public void setCurrentTransfer(Transfer currentTransfer) {
		this.currentTransfer = currentTransfer;
	}

	public void modify() {
		transferDao.merge(currentTransfer);
		MBUtils.redirect("accounts.xhtml?id=" + accountId);
	}

	public void next() {
		setCurrentTransfer(transferDao.next(getAccountWeb().getCurrentAccount(),
				getCurrentTransfer()));
		MBUtils.redirect("accounts.xhtml?id=" + accountId);
	}

	public void prior() {
		setCurrentTransfer(transferDao.prior(getAccountWeb().getCurrentAccount(),
				getCurrentTransfer()));
		MBUtils.redirect("accounts.xhtml?id=" + accountId);
	}
	
	public void transfers(int transferId) {
		MBUtils.redirect("transfers.xhtml?id=" + transferId);
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		Account c = new Account();
		c.setId(accountId);
		getAccountWeb().setCurrentAccount(c);
		this.accountId = accountId;
		getAllTransfers();
	}

	public Transfer getCurrentTransfer() {
		if (currentTransfer == null
				|| !currentTransfer.getAccount().equals(getAccountWeb().getCurrentAccount()))
			getAllTransfers();
		return currentTransfer;
	}
	
	public List<Transfer> getAllTransfers() {
		Account c = getAccountWeb().getCurrentAccount();
		List<Transfer> transfers = transferDao.getList(c);
		int siz = transfers.size();
		if (siz>0 && (currentTransfer == null || !currentTransfer.getAccount().equals(c)))
			currentTransfer = transfers.get(0);
		return transfers;
	}

	public String getRowstyle(Transfer transfer) {
		if (transfer.equals(currentTransfer))
			return "list-row-even";
		else
			return "list-row-odd";
	}

	

}
