package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class LoginFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest req, 
			             ServletResponse resp, 
			             FilterChain chain)
			throws IOException, ServletException {
		
		var httpReq = (HttpServletRequest) req;
		var httpResp = (HttpServletResponse) resp;
		
		String uri = httpReq.getRequestURI();
		boolean isPublicResource = uri.endsWith("/login") 
				|| uri.endsWith("login.jsp") 
				|| uri.contains("/css/") || uri.contains("/js/");
		
		boolean isLoggedUser = 
				httpReq.getSession().getAttribute("usuario_logado") != null;
		
		if (isLoggedUser || isPublicResource)
			chain.doFilter(req, resp);
		else httpResp.sendRedirect("/crud-manager/login.jsp");
	}
}