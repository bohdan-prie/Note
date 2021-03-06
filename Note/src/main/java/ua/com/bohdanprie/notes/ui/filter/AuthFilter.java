package ua.com.bohdanprie.notes.ui.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.ui.WebUtils;

@WebFilter(filterName = "AuthFilter")
public class AuthFilter implements Filter {
	private static final Logger LOG = LogManager.getLogger(AuthFilter.class.getName());
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}


	public AuthFilter() {
		LOG.debug("AuthFilter initialized");
	}

	public void doFilter(ServletRequest requestFilter, ServletResponse responseFilter, FilterChain chain)
			throws IOException, ServletException {
		LOG.trace("Performing filter");
		HttpServletRequest request = (HttpServletRequest) requestFilter;
		HttpServletResponse response = (HttpServletResponse) responseFilter;

		String page = WebUtils.getRequestedPath(request);
		
		HttpSession session = request.getSession(false);
		String UserAgent = request.getHeader("User-Agent");
				
		if (session == null || !UserAgent.equals(session.getAttribute("User-Agent"))) {
			if ("/profile".equals(page) || "/notes".equals(page) || "/toDos".equals(page)) {
				LOG.trace("User is trying to access private pages");
				LOG.trace("Sending redirect to main page");
				response.sendRedirect("/main");
				return;
			}
		} else {
			if("/login".equals(page) || "/reg".equals(page)) {
				LOG.trace("User is already authorised");
				LOG.trace("Sending redirect to main page");
				response.sendRedirect("/main");
				return;
			}
		}
		LOG.trace("Continue performing filters");
		chain.doFilter(requestFilter, responseFilter);
	}
}