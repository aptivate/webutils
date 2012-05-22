package org.aptivate.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspResponse implements Response
{
	private String m_JspPage;
	
	public JspResponse(String page)
	{
		m_JspPage = page;
	}
	
	public String getJspPage()
	{
		return m_JspPage;
	}
	
	public void render(ServletContext context, HttpServletRequest request,
		HttpServletResponse response)
	throws IOException, ServletException
	{
		RequestDispatcher dispatcher = 
			context.getRequestDispatcher(m_JspPage);
		dispatcher.forward(request, response);
	}

	public String toString()
	{
		return "JSP page: " + m_JspPage;
	}
}