package ema.tpjee.banking.controler;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tp.ejb.AccountDaoBean;
import tp.ejb.CustomerDaoBean;
import domain.model.Account;
import domain.model.Customer;


import ema.tpjee.utils.ServletUtils;

/**
 * Servlet implementation class for Servlet: AccountControler
 * 
 */
@WebServlet({"/account.do","/comptes"})
public class AccountControler extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {
	static final long serialVersionUID = 1L;
	

	@EJB
    private CustomerDaoBean customerDao;

	@EJB
    private AccountDaoBean accountDao;

	/**
	 * reconstituer le nouvel objet à partir des paramètres de la request; les
	 * frameworks le font par introspection
	 * 
	 * @param request
	 * @return
	 */
	private Account formValue(HttpServletRequest request) {
		Account result = new Account();

		result.setId(Integer.parseInt(request.getParameter("id")));
		result.setAccountNumber(request.getParameter("accountNumber"));
		
		if (request.getParameter("balance") != null)
			result.setBalance(Double.parseDouble(request
					.getParameter("balance")));
		if (request.getParameter("interestRate") != null)
			result.setInterestRate(Double.parseDouble(request
					.getParameter("interestRate")));
		if (request.getParameter("overdraftLimit") != null)
			result.setOverdraftLimit(Double.parseDouble(request
					.getParameter("overdraftLimit")));
		if (request.getParameter("overdraftPenalty") != null)
			result.setOverdraftPenalty(Double.parseDouble(request
					.getParameter("overdraftPenalty")));

		Customer customer = customerDao.find(
				Integer.parseInt(request.getParameter("customer")));
		result.setOwner(customer);

		return result;
	}

//Cycle de vie de la requête dans un paradigme MVC, incluant une gestion d'erreurs
//Ce problème est résolu de manière générique par les frameworks (Struts, JSF, etc...)	
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		//étape 1: 
	
		// tracer
		ServletUtils.debugRequestParameters(this.getClass(), request);
		ServletUtils.debugSessionAttributes(this.getClass(), request);

		String formbeanclassname = request.getParameter("formbeanclass");
		System.out.println("formbeanclass = " + formbeanclassname);
		// étape 2: décoder les événements depuis la request

		String page = request.getParameter("page");
		String cmd = request.getParameter("cmd");

		System.out.println("cmd = " + cmd);

		// étape 3: reconstituer l'objet de domaine depuis la request

		Account oldBean = null;
		// l'ancienne valeur
		Account newBean = null;
		// la nouvelle valeur, celle qui sera présentée à la vue

		Account formBean = null;

		if (Account.class.getName().equals(formbeanclassname))
			formBean = formValue(request);
		// la valeur reçue par la requête (avant validation)

		// étape 4: retrouver la valeur de l'objet du domaine avant l'action

		if (formBean != null)
			oldBean = accountDao.find(formBean.getOwner(),
					formBean.getId());
		// de préférence dans la couche de persistance
		else
			oldBean = (Account) request.getSession().getAttribute(
					"currentaccount");
		// sinon dans notre contexte web

		Customer currentCustomer = (Customer) request.getSession()
				.getAttribute("currentcustomer");

		if (oldBean == null || oldBean.getOwner() == null
				|| !oldBean.getOwner().equals(currentCustomer))
			// s'il n'y avait pas de bean courant, ou  si le bean
			// courant appartenait à un autre parent
			oldBean = accountDao.first(currentCustomer);
		// par défaut, prendre le premier de la liste

		if ("editer".equals(cmd))
			newBean = oldBean;
		else if ("suivant".equals(cmd))
			newBean = accountDao.next(currentCustomer, oldBean);
		else if ("precedent".equals(cmd))
			newBean = accountDao.prior(currentCustomer, oldBean);
		else if ("dupliquer".equals(cmd))
			newBean = accountDao.clone(oldBean);
		else if ("supprimer".equals(cmd))
			newBean = accountDao.delete(currentCustomer, oldBean);
		else if ("ajouter".equals(cmd))
			newBean = accountDao.create(currentCustomer);
		else if ("annuler".equals(cmd))
			newBean = oldBean;
		
		// étape 5: en cas de modification de l'objet
		// 5a appliquer les Règles de Gestion
		// 5b gérer les erreurs de validation, générer les messages d'erreur
		// 5c appliquer la nouvelle valeur sur le modèle si ok
		// 5d préparer l'objet validé et modifié ou invalidé pour
		//      correction, à représenter dans la vue (newbean)	
		
		else if ("modifier".equals(cmd))
			newBean = doModify(request, formBean);
		else if ("foobar".equals(cmd))
			newBean = foobar(request,  formBean);

		if (newBean != null)
			request.getSession().setAttribute("currentaccount", newBean); 
		// pour  interface avec la vue jsp
		else
			request.getSession().setAttribute("currentaccount", oldBean);
		request.getSession().setAttribute("accounts",
				accountDao.getList(currentCustomer));// reverseListBank());

		// étape 6: appliquer les règles de navigation, diriger la requête vers la vue
		String fw = null;
		
		
		if ("transferts".equals(cmd))
			fw = "/transfer.do"; 

		// les controleurs ne se connaissent pas entre eux. On passe par l'uri.
		// On utilise le contexte de session ou de requête pour passer les paramètres
		else if (page != null && !"null".equals(page))
			fw = "/pages/" + page + ".jsp";
		else
			fw = "/pages/account.jsp";

		// étape 7: faire suivre l'exécution vers la vue
		System.out.println("forwarding to: " + fw);
		getServletContext().getRequestDispatcher(fw).forward(request, response);
	}

	
	/**
	 * modifier l'objet au sein du modèle après validation
	 * 
	 * @param request
	 * @param b
	 * @return
	 */
	private Account doModify(HttpServletRequest request,
			 Account c) {
		Account result = null;

		try {
			validate(c);
		} catch (Exception e) {
			result = c;
			System.out.println("error " + e.getMessage());
			request.setAttribute("error",
					"error validating account " + e.getMessage());
			return result;
			// formBean is returned to be corrected
		}

		try {
			result = accountDao.merge(c.getOwner(), c);
			// le modèle est mis à jour, si pas d'exception
		} catch (Exception e) {
			result = c;
			System.out.println("error " + e.getMessage());
			request.setAttribute("error",
					"error saving account " + e.getMessage());
			result = accountDao.find(c.getOwner(), c.getId());
			return result;
			// formBean is not returned but the actual value in the persistent layer
		}
		return result;
	}

	/**
	 * validation x On souhaiterait une hiérarchie d'exceptions fonctionnelles
	 * et un langage d'expression pour les contraintes et règles métier
	 * 
	 */
	private void validate(Account account) {
		if (account.getAccountNumber() != null
				&& account.getAccountNumber().contains("00-00"))
			throw new RuntimeException("the account number is forbidden!");
	}

	private Account foobar(HttpServletRequest request,
			Account formBean) {
		return null;
	}


	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}