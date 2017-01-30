package eProject.utility;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import eProject.domain.master.GemsUserToken;
import eProject.service.master.MasterService;


public class ServiceFilter implements Filter {

	// FilterConfig filterConfig;

	@Autowired
	private MasterService masterService;

	// Over-riding the doFilter method to process our logic for Session Filter.
	// Over-riding the doFilter method to process our logic for Session Filter.
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		// Get an HttpServletRequest and Response.
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

		// Set up the context relative request path.
		String contextRelativeUri = httpRequest.getRequestURI();
		contextRelativeUri = contextRelativeUri.replaceFirst(httpRequest.getContextPath(), "");
		// String userTokenValue = servletRequest.getParameter("userToken");
		String userTokenValue = httpRequest.getHeader("userToken");
		String loggedInUserId = httpRequest.getParameter("userId");

		boolean isContextUriMatches = true;
		if (contextRelativeUri.equalsIgnoreCase("/login.action")) {
			isContextUriMatches = false;
		} else if (contextRelativeUri.equalsIgnoreCase("/customer/saveCustomer.action")) {
			isContextUriMatches = false;
		} else if (contextRelativeUri.equalsIgnoreCase("/user/forgotPassword.action")) {
			isContextUriMatches = false;
		} else if (contextRelativeUri.equalsIgnoreCase("/employee/downloadDocument.action")) {
			isContextUriMatches = false;
		} else if (contextRelativeUri.equalsIgnoreCase("/employee/downloadPaySlip.action")) {
			isContextUriMatches = false;
		} else if (contextRelativeUri.equalsIgnoreCase("/employee/generateTimeSheetReport.action")) {
			isContextUriMatches = false;
		} else if (contextRelativeUri.equalsIgnoreCase("/timesheet/exportTimeSheetTOExcel.action")) {
			isContextUriMatches = false;
		} else if (contextRelativeUri.equalsIgnoreCase("/customer/downloadCustomerDocument.action")) {
			isContextUriMatches = false;
		} else if (contextRelativeUri.equalsIgnoreCase("/recruitment/downloadResume.action")) {
			isContextUriMatches = false;
		}
		// if
		// (!(contextRelativeUri.equalsIgnoreCase("/user/loginVerification.action"))
		// || (!(contextRelativeUri.equals("/customer/saveCustomer.action"))))
		// if (
		// !((contextRelativeUri.equalsIgnoreCase("/user/loginVerification.action")))
		// ||
		// !((contextRelativeUri.equalsIgnoreCase("/customer/saveCustomer.action")))
		// ||
		// !((contextRelativeUri.equalsIgnoreCase("/user/forgotPassword.action")))
		// )
		// ApplicationContext appCtx;
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

		if (isContextUriMatches) {
			
			
			HttpSession session = httpRequest.getSession(false);
			if (session != null)
			{
				if (userTokenValue != null) {
					try {

						boolean status = masterService.validateToken(userTokenValue, loggedInUserId);
						if (status) {

						} else {
							System.out.println("Authorization denied");
							httpResponse.setStatus(403);
							httpResponse.sendRedirect("../../view/login/sessionoutlogin.html");
							//return;
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}

				} else {
					System.out.println("Authorization denied");
					httpResponse.setStatus(403);
					httpResponse.sendRedirect("../../view/login/sessionoutlogin.html");
					//return;
				}
			}
			else
			{
				GemsUserToken userToken = new GemsUserToken();
				String decodedToken = userToken.getDecodedToken(userTokenValue);

				String[] tokenParams = decodedToken.split(":");

				userToken.setUserTokenId(Integer.parseInt(tokenParams[0]));
				userToken.setUserId(Integer.parseInt(loggedInUserId));

				userToken = masterService.getGemsUserByToken(userToken);
				
				if (userToken != null)
				{
					masterService.removeGemsUserToken(userToken);
				}
				

				System.out.println("Authorization denied.");
				httpResponse.setStatus(403);
				httpResponse.sendRedirect("../../view/login/sessionoutlogin.html");
				//return;
			}
			
			
			
			
		}
		// Continuing with the doFilter Method.
		// filterChain.doFilter(new AtocXssFilter((HttpServletRequest)
		// servletRequest), servletResponse);
		filterChain.doFilter(servletRequest, servletResponse);

	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// this.filterConfig = filterConfig;
	}

	public void destroy() {
	}
}