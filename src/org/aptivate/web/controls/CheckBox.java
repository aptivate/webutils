/**
 * 
 */
package org.aptivate.web.controls;

import javax.servlet.http.HttpServletRequest;

import org.aptivate.web.utils.EditField;
import org.aptivate.web.utils.HtmlIterator;
import org.aptivate.web.utils.HtmlIterator.StartElementNode;

public class CheckBox extends StringValuedControl
{
    public static final String DEFAULT_CHECKED_VALUE = "1";
    private String m_CheckedValue;
    private String m_Label;
    
    public CheckBox(String name, String checkedValue,
    		HttpServletRequest request)
    {
    	super(name, request == null ? null : request.getParameter(name));
    	m_CheckedValue = checkedValue;
    }

    public CheckBox(String name, String label, String checkedValue, boolean paramValue)
    {
    	super(name, paramValue ? checkedValue : null);
    	m_CheckedValue = checkedValue;
    	m_Label = label;
    }
    
    public CheckBox(String name, String checkedValue, boolean paramValue)
    {
    	super(name, paramValue ? checkedValue : null);
    	m_CheckedValue = checkedValue;
    }
    
    public CheckBox(String name, boolean paramValue)
    {
    	this(name, DEFAULT_CHECKED_VALUE, paramValue);
    }
    public CheckBox(String name, HttpServletRequest request)
    {
    	this(name, DEFAULT_CHECKED_VALUE, request != null &&
    		request.getParameter(name) != null);
    }
    public String toString()
    {
        StringBuffer html = new StringBuffer();
        
        String name = EditField.attrib(getName());
        
        html.append("<input type=\"checkbox\" name=\"")
        	.append(name)
        	.append("\" id=\"")
        	.append(name)
        	.append("\" value=\"")
        	.append(EditField.attrib(m_CheckedValue))
        	.append("\"");
        if (getValue() != null && getValue().equals(m_CheckedValue))
        {
            html.append(" checked=\"checked\"");
        }
        appendAttributes(html);
        html.append(" />");

        if (m_Label != null)
        {
        	html.append("<label for=\"")
        		.append(name)
        		.append("\">")
        		.append(m_Label)
        		.append("</label>\n");
        }
        
        return html.toString();
    }
    
    public void assertField(HtmlIterator i)
    {
    	assertField(i, getName());
    }
    
    public StartElementNode assertField(HtmlIterator i, String id)
    {
    	StartElementNode sen = i.assertInput(getName(), m_CheckedValue);
    	sen.assertAttribute("id", id);
    	if (getValue() == null)
    	{    		
        	sen.assertAttribute("checked", null);
    	}
    	else
    	{
	    	sen.assertAttribute("checked", 
	    		getValue().equals(m_CheckedValue) ? "checked" : null);
    	}
    	return sen;
    }
}