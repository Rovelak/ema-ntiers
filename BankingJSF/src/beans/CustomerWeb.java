package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import tp.ejb.CityDaoBean;
import tp.ejb.CustomerDaoBean;
import components.CustomerView;
import domain.model.Bank;
import domain.model.City;
import domain.model.Customer;

@ManagedBean(name = "customerWeb")
@SessionScoped
public class CustomerWeb implements Serializable {

	private static final long serialVersionUID = 1641687L;

	@EJB
	private CustomerDaoBean customerDao;

	@EJB
	private CityDaoBean cityDao;

	@ManagedProperty(value = "#{bankWeb}")
	private BankWeb bankWeb;

	public BankWeb getBankWeb() {
		return bankWeb;
	}

	public void setBankWeb(BankWeb bankWeb) {
		this.bankWeb = bankWeb;
	}

	@ManagedProperty(value = "#{customerView}")
	private CustomerView customerView;

	public CustomerView getCustomerView() {
		return customerView;
	}

	public void setCustomerView(CustomerView customerView) {
		this.customerView = customerView;
	}

	private Map<String, String> cityMap;
	private int bankId;
	private Customer currentCustomer;

	protected Object getBackingBean(String name) {
		FacesContext context = FacesContext.getCurrentInstance();

		Application app = context.getApplication();

		// ValueExpression expression =
		// app.getExpressionFactory().createValueExpression(context.getELContext(),
		// String.format("#{%s}", name), Object.class);

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
		
		HtmlSelectOneMenu cityMenu = (HtmlSelectOneMenu) vce.getComponent();
		cityMenu = customerView.getCitymenu();
		System.out.println("citymenu=");
		List<UIComponent> childs = cityMenu.getChildren();
		for (UIComponent uiComponent : childs) {
			if (uiComponent instanceof UISelectItems) {
				UISelectItems uISelectItems = (UISelectItems) uiComponent;
				Map<String, Object> atts = (Map<String, Object>) uISelectItems
						.getValue();
				Set<String> keys = atts.keySet();
				for (String key : keys) {
					System.out.println("     " + key + "=" + atts.get(key));
				}
			}
		}
		
		HtmlInputText name = customerView.getName();
		System.out.println("name=" + name.getValue());
	}

	public void setCurrentCustomer(Customer currentCustomer) {
		this.currentCustomer = currentCustomer;
	}

	public void modify() {
		customerDao.merge(currentCustomer);
		MBUtils.redirect("customers.xhtml?id=" + bankId);
	}

	public void next() {
		setCurrentCustomer(customerDao.next(bankWeb.getCurrentBank(),
				getCurrentCustomer()));
		MBUtils.redirect("customers.xhtml?id=" + bankId);
	}

	public void prior() {
		setCurrentCustomer(customerDao.prior(bankWeb.getCurrentBank(),
				getCurrentCustomer()));
		MBUtils.redirect("customers.xhtml?id=" + bankId);
	}

	public void accounts(int customerId) {
		MBUtils.redirect("accounts.xhtml?id=" + customerId);
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		Bank b = new Bank();
		b.setId(bankId);
		bankWeb.setCurrentBank(b);
		this.bankId = bankId;
		getAllCustomers();
	}

	public Customer getCurrentCustomer() {
		if (currentCustomer == null
				|| !currentCustomer.getBank().equals(bankWeb.getCurrentBank()))
			getAllCustomers();
		return currentCustomer;
	}

	public List<Customer> getAllCustomers() {
		Bank b = bankWeb.getCurrentBank();
		List<Customer> customers = customerDao.getList(b);
		int siz = customers.size();
		if (siz>0 && (currentCustomer == null || !currentCustomer.getBank().equals(b)))
			currentCustomer = customers.get(0);
		return customers;
	}

	public String getRowstyle(Customer cust) {
		if (cust.equals(currentCustomer))
			return "list-row-even";
		else
			return "list-row-odd";
	}

	public String getCurrentCustomerCity() {
		if (currentCustomer==null)
			return "";
		City city = currentCustomer.getCity();
		if (city != null)
			return Integer.toString(currentCustomer.getCity().getId());
		else
			return "";
	}

	public void setCurrentCustomerCity(String sid) {
		if (currentCustomer==null)
			return;
		if (sid != null && !sid.isEmpty()) {
			City city = cityDao.find(Integer.parseInt(sid));
			getCurrentCustomer().setCity(city);
		}
	}

	public Map<String, String> getCities() {
		if (cityMap == null) {
			cityMap = new HashMap();
			List<City> cities = cityDao.getList();
			cityMap.put("no city", "");
			for (City city : cities) 
				cityMap.put(city.getName(), Integer.toString(city.getId()));
		}
		return cityMap;
	}

}
