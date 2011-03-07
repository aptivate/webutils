package org.aptivate.web.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class BatchUploadHelper
{
	public String  m_errorMsg = null;
	private String m_Data = null;
	public String getData() { return m_Data; }
	
	private List m_FileItems;
	public List getFileItems() { return m_FileItems; }
	
	public boolean parseRequest(HttpServletRequest request)
		throws Exception
	{
		// Check that we have a file upload request
		if (!ServletFileUpload.isMultipartContent(request))
		{
			m_errorMsg = "Wrong encoding type: Expected multipart/form-data";
			return false;
		}
		
		ServletFileUpload servletFileUpload =
			new ServletFileUpload(new DiskFileItemFactory());
		m_FileItems = servletFileUpload.parseRequest(request);

		return true;
	}

	public interface LineHandler
	{
		boolean handleLine(int lineNumber, String [] args) throws Exception;
	}
	
	protected boolean parseFile(LineHandler handler, InputStream stream)
	throws Exception
	{
		BufferedReader bis = new BufferedReader(new InputStreamReader(stream));
		
		int lineNum = 0;
		
		for (String line = bis.readLine(); line != null; line = bis.readLine())
		{
			lineNum++;
			
			if (line.equals("")) { continue; }

			List fields = new ArrayList();
			StringBuffer currentField = new StringBuffer();
			boolean inQuotes = false;

			for (int i = 0; i < line.length(); i++)
			{
				char c = line.charAt(i);
				if (c == '"')			
				{
					inQuotes = !inQuotes;
				}
				else if (c == ',' && !inQuotes)
				{
					fields.add(currentField.toString());
					currentField.setLength(0);
				}
				else
				{
					currentField.append(c);
				}
			}

			fields.add(currentField.toString());

			String [] array = new String[fields.size()];
			fields.toArray(array);
			if (!handler.handleLine(lineNum, array))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public FileItem getFileItemByName(String name)
	{
		for (Iterator i = m_FileItems.iterator(); i.hasNext();)
		{
			FileItem item = (FileItem) i.next();
			
			if (item.getFieldName().equals(name))
				return item;
		}
		
		return null;
	}
	
	public boolean parseData(HttpServletRequest request)
	throws Exception
	{
		m_Data = request.getParameter("data");
		
		return true;
	}
}
