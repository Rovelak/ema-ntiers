package ema.tpjee.banking.controler;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tp.ejb.BankDaoBean;
import tp.ejb.CityDaoBean;
import tp.ejb.CustomerDaoBean;
import domain.model.Bank;
import domain.model.City;
import ema.tpjee.utils.ServletUtils;


/**
 * v141212
 */


/**
 * Servlet implementation class for Servlet: BankControler
 * les dao sont détenus par les servlets, 
 * ils sont partagés pas toutes les sessions, ils sont stateless
 * Le cycle de vie de la servlet effectue la gestion d'erreur
 * 
 */
@WebServlet({"/bank.do","/banks"})
public class BankControler extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {
	   private static final long serialVersionUID = 149345365454L;
	   private static final boolean TESTING = true;
		
		@EJB
		private BankDaoBean bankDao;
		
		@EJB
		private CityDaoBean cityDao;
		
	
		public void init(ServletConfig config) throws ServletException {
			super.init(config); 
			if (TESTING){
			  bankDao.populate();
			  //customerDao.populate(bankDao.getList().get(0),0);	
			  //customerDao.populate(bankDao.getList().get(1),1);	
			  System.out.println ("populated for test");
			}
		}		
		
	/**
	 * reconstituer le nouvel objet à partir des paramètres de la request les
	 * frameworks le font par introspection
	 * 
	 * @param request
	 * @return
	 */
	private Bank formValue(HttpServletRequest request) {
		if (request.getParameter("id") == null)
			return null;
	
		Bank formBank = new Bank();
		formBank.setId(Integer.parseInt(request.getParameter("id")));
		formBank.setName(request.getParameter("name"));
		formBank.setAddress(request.getParameter("address"));
		formBank.setPhone(request.getParameter("phone"));
		try {
			formBank.setZipCode(request.getParameter("zipCode"));
		} catch (Exception e) {
			System.out.println("bad zipcode for bank "+ formBank.getName());
		}
		
		try {
			formBank.setCity(cityDao.find(Integer.parseInt(request.getParameter("commune"))));
		} catch (Exception e) {
			System.out.println("no city for bank "+ formBank.getName());
		}

		return formBank;
	}

	/**
	 * validation x On souhaiterait une hiérarchie d'exceptions
	 * 
	 * @param b
	 */
	private void validate(Bank b) {
		if (b.getName().toLowerCase().contains("bernard madoff"))
			throw new RuntimeException("the name is forbidden!");
	}

	/**
	 * validation y On souhaiterait une hiérarchie d'exceptions
	 * 
	 * @param b
	 */
	private void validateZipCode(Bank b) {
		if (Integer.parseInt(b.getZipCode()) > 150000)
			throw new RuntimeException("zipcode is out of limits!");
	}

	/**
	 * modifier l'objet au sein du modèle après validation
	 * 
	 * @param request
	 * @param b
	 * @return
	 */
	private Bank doModify(HttpServletRequest request,  Bank bank) {
		Bank result = null;
		try {
			validate(bank);
			result = bankDao.merge(bank); 
			// le modèle est mis à jour, si pas d'exception
		} catch (Exception e) {
			result = bank; 
			// formBean is returned but not modified in persistence layer
			System.out.println("error " + e.getMessage());
			request.setAttribute("error", "erreur " + e.getMessage());
		}
		return result;
	}

	/**
	 * foobar est un squelette d'action métier (autre qu'une simple opération CRUD) exemple
	 * d'une contrainte susceptible de lever une exception sémantique, pour
	 * tester les mécanismes de validation
	 * 
	 * A noter: Adhérence de la couche métier avec la couche web (paramétre
	 * request) On souhaiterait un mécanisme générique de gestion d'erreurs
	 * 
	 * @param request
	 * @param b the bank to foobar
	 * @return the foobared bank
	 */
	private Bank foobar(HttpServletRequest request, Bank b) {
		Bank result = null;
		try {
			result = bankDao.foobar(b); 
			// la RG peut étre déléguée au composant
			validateZipCode(b); 
			// la RG peut étre vérifiée par le contrôleur
			result = bankDao.merge(b); 
			// si pas d'exception, le modéle est mis à jour
		} catch (Exception e) { //il faudrait faire une hiérarchie d'exceptions pour la validation
			result = b; 
			// formBean is returned but not modified in persistence layer
			System.out.println("error " + e.getMessage());
			request.setAttribute("error", "erreur " + e.getMessage());
		}
		return result;
	}

	private void getCities(HttpServletRequest request){
		if (request.getSession().getAttribute("cities") ==null){
		  List<City> cities = cityDao.getList();
		  request.getSession().setAttribute("cities",cities);	//pour interface avec les jsp
		}
	}	
	
	
	/**
	 * Méthode recevant l'action en provenance du client On souhaiterait
	 * généraliser le mécanisme Cette généralisation est réalisée par les
	 * frameworks (struts, spring, etc..) et par JSF
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		ServletUtils.debugRequestParameters(this.getClass(), request);
		ServletUtils.debugSessionAttributes(this.getClass(), request);
		
		// étape 0 récupérer les données nécessaires aux relations 1-N
		getCities(request);
		
		// étape 1 décoder les événements depuis la request

		String page = request.getParameter("page");
		String cmd = request.getParameter("cmd");
		
	
		System.out.println("cmd = " + cmd);

		// étape 2 reconstituer l'objet de domaine depuis la request

		Bank oldBean = null; 
		// l'ancienne valeur
		
		Bank newBean = null; 
		// la nouvelle valeur, celle qui sera présentée à  la vue
		
		Bank formBean = formValue(request); 
		// la valeur reçue par la requête (avant validation)

		// étape 3 retrouver la valeur de l'objet du domaine avant l'action

		if (formBean != null)
			oldBean = bankDao.find(formBean.getId()); 
		else
			oldBean = (Bank) request.getSession().getAttribute("currentbank");
		// de préférence dans la couche de persistance sinon dans notre contexte web

		if (oldBean == null)
			oldBean = bankDao.first(); 
		// par défaut, prendre le premier de la liste

		// étape 4
		// 4a appliquer les RG
		// 4b gérer les erreurs de validation, générer les messages d'erreur
		// 4c appliquer la nouvelle valeur sur le modéle si ok
		// 4d préparer l'objet validé et modifié ou invalidé pour correction, é
		// présenter dans la vue (newbean)

		if ("editer".equals(cmd))
			newBean = oldBean;
		else if ("suivant".equals(cmd))
			newBean = bankDao.next(oldBean);
		else if ("precedent".equals(cmd))
			newBean = bankDao.prior(oldBean);
		else if ("dupliquer".equals(cmd))
			newBean = bankDao.clone(oldBean);
		else if ("supprimer".equals(cmd))
			newBean = bankDao.delete(oldBean);
		else if ("ajouter".equals(cmd))
			newBean = bankDao.create();
		else if ("annuler".equals(cmd))
			newBean = oldBean;
		else if ("modifier".equals(cmd))
			newBean = doModify(request, formBean);
		else if ("foobar".equals(cmd))
			newBean = foobar(request, formBean);


		if (newBean != null)
			request.getSession().setAttribute("currentbank", newBean); //pour interface avec les jsp
		else
			request.getSession().setAttribute("currentbank", oldBean);
		request.getSession().setAttribute("banks", bankDao.getList());

		// étape 5 appliquer les régles de navigation, diriger la requéte vers la vue
		String fw = null;

		if ("clients".equals(cmd))
			fw = "/customer.do"; 
		// les controleurs ne se connaissent pas entre eux. On passe par l'uri. On
		// utilise le contexte de session ou de requéte pour passer les paramétres
		else if (page != null && !"null".equals(page))
			fw = "/pages/" + page + ".jsp";
		else
			fw = "/pages/bank.jsp";

		// étape 6 faire suivre l'exécution vers la vue
		System.out.println("forwarding to: " + fw);
		getServletContext().getRequestDispatcher(fw).forward(request, response);
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