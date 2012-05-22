package org.aptivate.web;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectResponse implements Response
{
	private String m_RedirectToUrl;
	
	public RedirectResponse(String redirectToUrl)
	{
		m_RedirectToUrl = redirectToUrl;
	}

	public String getRedirectUrl()
	{
		return m_RedirectToUrl;
	}

	public void render(ServletContext context, HttpServletRequest request,
		HttpServletResponse response)
	throws IOException, ServletException
	{
		response.sendRedirect(m_RedirectToUrl);
	}
	
	public String toString()
	{
		return "redirect to: " + m_RedirectToUrl;
	}
}