package ema.tpjee.banking.controler;

import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tp.ejb.TransferDaoBean;
import tp.ejb.AccountDaoBean;
import domain.model.Account;
import domain.model.Transfer;
import ema.tpjee.utils.ServletUtils;

@WebServlet({"/transfer.do","/transfers"})
public class TransferControler extends javax.servlet.http.HttpServlet implements
javax.servlet.Servlet{

	private static final long serialVersionUID = 61551268566592518L;
	
	@EJB
    private AccountDaoBean accountDao;

	@EJB
    private TransferDaoBean transferDao;

	/**
	 * reconstituer le nouvel objet à partir des paramètres de la request; les
	 * frameworks le font par introspection
	 * 
	 * @param request
	 * @return
	 */
	private Transfer formValue(HttpServletRequest request) {
		Transfer result = new Transfer();

		result.setId(Integer.parseInt(request.getParameter("id")));
		if (request.getParameter("date") != null)
			result.setDate(new Date(request.getParameter("date")));
		if (request.getParameter("amount") != null)
			result.setAmount(Double.parseDouble(request.getParameter("amount")));
		if (request.getParameter("fromAccount") != null){
			Account account = accountDao.find(Integer.parseInt(request.getParameter("fromAccount")));
			result.setFromAccount(account);
		}
		if (request.getParameter("toAccount") != null){
			Account account = accountDao.find(Integer.parseInt(request.getParameter("toAccount")));
			result.setToAccount(account);
		}

		Account account = accountDao.find(Integer.parseInt(request.getParameter("account")));
		result.setAccount(account);
		
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

		Transfer oldBean = null;
		// l'ancienne valeur
		Transfer newBean = null;
		// la nouvelle valeur, celle qui sera présentée à la vue

		Transfer formBean = null;

		if (Transfer.class.getName().equals(formbeanclassname))
			formBean = formValue(request);
		// la valeur reçue par la requête (avant validation)

		// étape 4: retrouver la valeur de l'objet du domaine avant l'action

		if (formBean != null)
			oldBean = transferDao.find(formBean.getAccount(),
					formBean.getId());
		// de préférence dans la couche de persistance
		else
			oldBean = (Transfer) request.getSession().getAttribute(
					"currenttransfer");
		// sinon dans notre contexte web

		Account currentAccount = (Account) request.getSession()
				.getAttribute("currentaccount");

		if (oldBean == null || oldBean.getAccount() == null
				|| !oldBean.getAccount().equals(currentAccount))
			// s'il n'y avait pas de bean courant, ou  si le bean
			// courant appartenait à un autre parent
			oldBean = transferDao.first(currentAccount);
		// par défaut, prendre le premier de la liste

		if ("editer".equals(cmd))
			newBean = oldBean;
		else if ("suivant".equals(cmd))
			newBean = transferDao.next(currentAccount, oldBean);
		else if ("precedent".equals(cmd))
			newBean = transferDao.prior(currentAccount, oldBean);
		else if ("dupliquer".equals(cmd))
			newBean = transferDao.clone(oldBean);
		else if ("supprimer".equals(cmd))
			newBean = transferDao.delete(currentAccount, oldBean);
		else if ("ajouter".equals(cmd))
			newBean = transferDao.create(currentAccount);
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
			request.getSession().setAttribute("currenttransfer", newBean); 
		// pour  interface avec la vue jsp
		else
			request.getSession().setAttribute("currenttransfer", oldBean);
		request.getSession().setAttribute("transfers",
				transferDao.getList(currentAccount));// reverseListBank());

		// étape 6: appliquer les règles de navigation, diriger la requête vers la vue
		String fw = null;

		// les controleurs ne se connaissent pas entre eux. On passe par l'uri.
		// On utilise le contexte de session ou de requête pour passer les paramètres
		if (page != null && !"null".equals(page))
			fw = "/pages/" + page + ".jsp";
		else
			fw = "/pages/transfer.jsp";

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
	private Transfer doModify(HttpServletRequest request,
			 Transfer c) {
		Transfer result = null;

		try {
			result = transferDao.merge(c.getAccount(), c);
			// le modèle est mis à jour, si pas d'exception
		} catch (Exception e) {
			result = c;
			System.out.println("error " + e.getMessage());
			request.setAttribute("error",
					"error saving transfer " + e.getMessage());
			result = transferDao.find(c.getAccount(), c.getId());
			return result;
			// formBean is not returned but the actual value in the persistent layer
		}
		return result;
	}

	private Transfer foobar(HttpServletRequest request,
			Transfer formBean) {
		return null;
	}


	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}
