package com.healthcare;

import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

/**
 * Servlet Filter implementation class LoggingFilter
 */
@WebFilter("/*")
public class LoggingFilter extends HttpFilter implements Filter {
	private static final Logger logger = LogManager.getLogger(LoggingFilter.class);
    /**
     * @see HttpFilter#HttpFilter()
     */
    public LoggingFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		HttpServletRequest req = (HttpServletRequest) request;
		String uri = req.getRequestURI();
		String method = req.getMethod();
		String ip = req.getRemoteAddr();
		logger.info("Incoming request -> Method: {}, URI: {}, IP: {}",method,uri,ip);
		
		long startTime = System.currentTimeMillis();
		
		chain.doFilter(request, response);
		long endTime = System.currentTimeMillis();
		logger.info("Response sent -> URI: {}, Time Taken: {} ms",uri,(endTime-startTime));
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}