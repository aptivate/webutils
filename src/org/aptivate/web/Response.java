package org.aptivate.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Response
{
	public void render(ServletContext context, HttpServletRequest request,
		HttpServletResponse response)
	throws Exception;
}