package net.straininfo2.grs.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Wraps the accept header based on the extension added to the url
 * 
 */
public class ExtensionAcceptFilter implements Filter {

	private static class RequestWrapper extends HttpServletRequestWrapper {

		private String accept = null;
		
		final static Map<String, String> acceptStrings = new HashMap<String, String>();
		
		static {
			acceptStrings.put("xml", "application/xml");
			acceptStrings.put("txt", "text/plain");
			acceptStrings.put("json", "application/json");
		}
		
		public RequestWrapper(HttpServletRequest request) {
			super(request);
			Object ext = request.getAttribute("net.straininfo2.ext"); 
			if (ext != null && acceptStrings.containsKey(ext)) {
				this.accept = acceptStrings.get(ext);
			}
		}
	
		@Override
		public String getHeader(String name) {
			if ("Accept".equalsIgnoreCase(name) && this.accept != null) {
				return this.accept;
			}
			else {
				return super.getHeader(name);
			}
		}
		
		@Override
		public Enumeration<String> getHeaders(String name) {
			if ("Accept".equalsIgnoreCase(name) && this.accept != null) {
				return new Vector<String>(Collections.singleton(this.accept)).elements();
			}
			else {
				return super.getHeaders(name);
			}
		}
		
		@Override
		public Enumeration<String> getHeaderNames() {
			Enumeration<String> headerNames = super.getHeaderNames();
			Vector<String> headers = new Vector<String>();
			boolean hasAccept = false;
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				if ("accept".equalsIgnoreCase(name)) {
					hasAccept = true;
				}
				headers.add(name);
			}
			if (!hasAccept) {
				headers.add("Accept");
			}
			return headers.elements();
		}
	}
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new RequestWrapper((HttpServletRequest)request), response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}
	
}
