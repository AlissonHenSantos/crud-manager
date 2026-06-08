package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ModelException;
import model.User;
import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.utils.PasswordEncryptor;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginController extends HttpServlet {

	// /login	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		
		String userLogin = req.getParameter("user_login");
		String userPW = req.getParameter("user_pw");
		
		UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
		User authenticatedUser = null;
		
		try {
			authenticatedUser = userDAO.findByEmail(userLogin);
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
		}
		
		if (authenticatedUser != null) {
			
			req.getSession().setAttribute("usuario_logado", authenticatedUser);
			resp.sendRedirect("/crud-manager");
		} else {
			resp.sendRedirect("/crud-manager/login.jsp?erro=true");
		}
	}
	
	// /logout
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		System.out.println(req.getRequestURI());
		
		// Pega sessão
		// false para não criar sessão nova se não houver sessão já criada
		HttpSession session = req.getSession(false);
	
		// Ivalida a sessão
		if (session != null) {
			session.invalidate();
		}
		
		// Redireciona para o login
		resp.sendRedirect("/crud-manager/login.jsp");
	}
}