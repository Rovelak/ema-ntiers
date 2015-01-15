package ema.tpjee.banking.controler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tp.ejb.BankDaoBean;
import tp.ejb.CityDaoBean;
import tp.ejb.CustomerDaoBean;
import domain.model.Bank;
import domain.model.City;
import domain.model.Customer;
//import ema.tpjee.banking.session.BankingSession;
//import ema.tpjee.utils.AppLocator;
import ema.tpjee.utils.ServletUtils;

/**
 * Servlet implementation class for Servlet: ClientControler
 * 
 */
@WebServlet({"/customer.do","/clients"})
public class ClientControler extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {
	static final long serialVersionUID = 1L;

	@EJB
    private CustomerDaoBean customerDao;
	
	@EJB
    private BankDaoBean bankDao;
	
	@EJB
    private CityDaoBean cityDao;

	private Map cityMap;
	
	
	@Override
	public void init() throws ServletException {
		super.init();		
	}


	private void getCities(HttpServletRequest request){
		if (request.getSession().getAttribute("cities") ==null){
		  List<City> cities = cityDao.getList();
		  request.getSession().setAttribute("cities",cities);	//pour interface avec les jsp
		}
	}
	
	
	public String getCustomerCity(Customer c) {
		City city = c.getCity();
		if (city != null)
			return Integer.toString(c.getCity().getId());
		else
			return "";
	}

	public void setCustomerCity(String sid,Customer c) {
		if (sid != null && !sid.isEmpty()) {
			City city = cityDao.find(Integer.parseInt(sid));
			c.setCity(city);
		}
	}

	public Map<String, String> getCities(){
		if (cityMap == null){
			cityMap = new HashMap();
			List<City> cities = cityDao.getList();
			cityMap.put("no city", "");
			for (City city : cities) 
				cityMap.put(city.getName(), Integer.toString(city.getId()));		
		}
		return cityMap;
	}	
	
	
	
	/**
	 * reconstituer le nouvel objet � partir des param�tres de la request les
	 * frameworks le font par introspection
	 * 
	 * @param request
	 * @return
	 */
	private Customer formValue(HttpServletRequest request) {
			Customer result = new Customer();
		int cid_ = -1;
		try {
			cid_ = Integer.parseInt(request.getParameter("commune"));
		} catch (Exception e) {
			System.out.println("no cust city");
		}
		result.setId(Integer.parseInt(request.getParameter("id")));
		result.setName(request.getParameter("name"));
		result.setForName(request.getParameter("forName"));
		result.setAddress(request.getParameter("address"));
		result.setZipCode(request.getParameter("zipCode"));
		
		try {
		Bank bank = bankDao.find(Integer.parseInt(request.getParameter("bank")));
		result.setBank(bank);
		} catch (Exception e) {
			System.out.println("err cust bank");
		}
		

		if (cid_!=-1){
			
			City city = cityDao.find(cid_);
		    result.setCity(city);
		    
		    
		}

		return result;
	}
	
	
	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		getCities(request);
		
		ServletUtils.debugRequestParameters(this.getClass(), request);
		ServletUtils.debugSessionAttributes(this.getClass(), request);
		
		//String referer = request.getHeader("referer");
		//referer= referer.substring(referer.lastIndexOf("/")+1);
		String formbeanclassname= request.getParameter("formbeanclass");
		System.out.println("formbeanclass = " + formbeanclassname);
		// ï¿½tape 1 dï¿½coder les ï¿½vï¿½nements depuis la request

		String page_ = request.getParameter("page");
		String cmd = request.getParameter("cmd");

		System.out.println("cmd = " + cmd);

		// ï¿½tape 2 reconstituer l'objet de domaine depuis la request

		Customer oldBean = null; 
		// l'ancienne valeur
		Customer newBean = null; 
		// la nouvelle valeur, celle qui sera prï¿½sentï¿½e ï¿½ la vue
		
		Customer formBean = null;
		
		if (Customer.class.getName().equals(formbeanclassname)) 
			formBean = formValue(request); 
	
		// la valeur reï¿½ue par la requï¿½te (avant validation)

		// ï¿½tape 3 retrouver la valeur de l'objet du domaine avant l'action
		
		if (formBean != null)
			oldBean = customerDao.find(formBean.getBank(),formBean.getId()); 
		// de prï¿½fï¿½rence dans la couche de persistance
		else
			oldBean = (Customer) request.getSession().getAttribute("currentcustomer");
		// sinon dans notre contexte web

		Bank currentBank = (Bank) request.getSession().getAttribute("currentbank");

		if (oldBean == null || oldBean.getBank()==null || !oldBean.getBank().equals(currentBank))//s'il n'y avait pas de bean courant, ou si le bean courant appartenait Ã  un autre parent
			oldBean = customerDao.first(currentBank); 
		// par dï¿½faut, prendre le premier de la liste

		// ï¿½tape 4
		// 4a appliquer les RG
		// 4b gï¿½rer les erreurs de validation, gï¿½nï¿½rer les messages d'erreur
		// 4c appliquer la nouvelle valeur sur le modï¿½le si ok
		// 4d prï¿½parer l'objet validï¿½ et modifiï¿½ ou invalidï¿½ pour correction, ï¿½
		// prï¿½senter dans la vue (newbean)

		if ("editer".equals(cmd))
			newBean = oldBean;
		else if ("suivant".equals(cmd))
			newBean = customerDao.next(currentBank,oldBean);
		else if ("precedent".equals(cmd))
			newBean = customerDao.prior(currentBank,oldBean);
		else if ("dupliquer".equals(cmd))
			newBean = customerDao.clone(currentBank,oldBean);
		else if ("supprimer".equals(cmd))
			newBean = customerDao.delete(currentBank,oldBean);
		else if ("ajouter".equals(cmd))
			newBean = customerDao.create(currentBank);
		else if ("annuler".equals(cmd))
			newBean = oldBean;
		else if ("modifier".equals(cmd))
			newBean = doModify(request, formBean);
		else if ("foobar".equals(cmd))
			newBean = foobar(request,  formBean);


		if (newBean != null)
			request.getSession().setAttribute("currentcustomer", newBean); //pour interface avec les jsp
		else
			request.getSession().setAttribute("currentcustomer", oldBean);
		request.getSession().setAttribute("customers", customerDao.getList(currentBank));// reverseListBank());

		// ï¿½tape 5 appliquer les rï¿½gles de navigation, diriger la requï¿½te vers la vue
		String fw = null;

		
		if ("comptes".equals(cmd))
			fw = "/account.do"; 
		 
		
		// les controleurs ne se connaissent pas entre eux. On passe par l'uri. On
		// utilise le contexte de session ou de requï¿½te pour passer les paramï¿½tres
		else if (page_ != null && !"null".equals(page_) && page_.startsWith("client"))
			fw = "/pages/" + page_ + ".jsp";
		else
			fw = "/pages/client.jsp";

		// ï¿½tape 6 faire suivre l'exï¿½cution vers la vue
		System.out.println("forwarding to: " + fw);
		getServletContext().getRequestDispatcher(fw).forward(request, response);
	}	
	
	
	
	
	/**
	 * modifier l'objet au sein du modï¿½le aprï¿½s validation
	 * 
	 * @param request
	 * @param b
	 * @return
	 */
	private Customer doModify(HttpServletRequest request, Customer c) {
		Customer result = null;
		try {
			validate(c);
			result = customerDao.merge(c); 
			// le modï¿½le est mis ï¿½ jour, si pas d'exception
		} catch (Exception e) {
			result = c; 
			// formBean is returned but not modified in persistence layer
			System.out.println("error " + e.getMessage());
			request.setAttribute("error", "erreur " + e.getMessage());
		}
		return result;
	}

	/**
	 * validation x On souhaiterait une hiï¿½rarchie d'exceptions
	 * 
	 * @param b
	 */
	private void validate(Customer c) {
		if (c.getName().contains("supercrash")
				|| c.getName().contains("forbidden"))
			throw new RuntimeException("the name is forbidden!");
	}


	
	private Customer foobar(HttpServletRequest request, Customer formBean) {
		// TODO Auto-generated method stub
		return null;
	}

	

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}