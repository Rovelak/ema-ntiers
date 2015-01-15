package ema.tpjee.banking.controler;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import domain.model.Bank;
import ema.tpjee.utils.ServletUtils;
import tp.ejb.BankDaoBean;
import tp.ejb.CustomerDaoBean;

/**
 * Servlet implementation class BankControler
 */
@WebServlet({"/bank_.do","/banks_"})
public class BankControlerSimple extends HttpServlet {
	private static final long serialVersionUID = 149345787L;
   private static final boolean TESTING = true;
	
	@EJB
	private CustomerDaoBean customerDao;
	
	@EJB
	private BankDaoBean bankDao;
	
	//@EJB
	//private CityDaoBean cityDao;
	
	//@EJB
	//private AccountDaoBean accountDao;	
	
	

    public BankControlerSimple() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config); 
		if (TESTING){
		  bankDao.populate();
		  customerDao.populate(bankDao.getList().get(0),0);	
		  customerDao.populate(bankDao.getList().get(1),1);	
		  System.out.println ("populated for test");
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletUtils.debugRequestParameters(this.getClass(), request);
		ServletUtils.debugSessionAttributes(this.getClass(), request);
		
	 
		List<Bank> banks = bankDao.getList();
	    
	    request.getSession().setAttribute("currentbank",banks.get(0));
	    request.getSession().setAttribute("banks",banks);
	    
		String fw = "/pages/bank.jsp";

		System.out.println(banks.get(0).getName());
	//	System.out.println("forwarding to: " + fw);
		getServletContext().getRequestDispatcher(fw).forward(request, response);
	}

}
