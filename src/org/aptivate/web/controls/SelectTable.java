/**
 * 
 */
package org.aptivate.web.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aptivate.web.utils.EditField;
import org.aptivate.web.utils.HtmlIterator;


public class SelectTable extends FormControl
{
	private List m_RequestSelection, m_DefaultSelection;
	private List m_Options;
	private HttpServletRequest m_Request;
	private String[] m_CheckBoxNames;
	private String[] m_CheckBoxLabels;
	
    public SelectTable(String name, String [] requestSelection,
    	List options, String[] checkBoxNames, String[] checkBoxLabels, 
    	HttpServletRequest request)
    {
        super(name);
        if (requestSelection != null)
        {
            m_RequestSelection = new ArrayList(
                	Arrays.asList(requestSelection));            	
        }
        else
        {
        	m_RequestSelection = null;
        }
        m_Options = options;
        m_Request = request;
        m_CheckBoxNames = checkBoxNames;
        m_CheckBoxLabels = checkBoxLabels;
    }
    
    public SelectTable setDefaultSelection(List selected)
    {
    	m_DefaultSelection = new ArrayList(selected);
    	return this;
    }

    public String toString()
    {
        List selected = null;
        
        if (m_RequestSelection != null)
        {
        	selected = m_RequestSelection;
        }
        else
        {
        	selected = m_DefaultSelection;
        }
         
        List unselectedOptions = new ArrayList();
        for (Iterator i = m_Options.iterator(); i.hasNext();)
        {
        	String [] row = (String[])i.next();
            String value = row[0];
            if (selected == null || !selected.contains(value))
            {
            	unselectedOptions.add(row);
            }
        }

        StringBuffer html = new StringBuffer();
        html.append("<table>");
        
        if (selected != null && selected.size() > 0)
        {
	        html.append("<tr><th colspan=\"2\">Remove</th></tr>\n");
	        
	        for (Iterator i = m_Options.iterator(); i.hasNext(); )
	        {
	            String [] row = (String[])i.next();
	            String value = row[0];
	            String label = row[1];

	            if (!selected.contains(value))
	            {
	            	continue;
	            }
	            
	            html.append("<tr>\n" +
	            		"<td>" +
	            		"<input type=\"hidden\"" +
	            		" name=\"" + getName() + "_old\"" +
	            		" value=\"" + value + "\" />" +
	            		new CheckBox(getName() + "_del", value,
	            				m_Request).toString() +
	            		"</td>\n" + 
	            		"<td>" + EditField.escapeEntities(label) + "</td>\n" +
	            		"</tr>\n");
	        }
        }
        
        html.append("<tr><th colspan=\"2\">Add</th></tr>\n");
        html.append("<tr>\n<td>");
        html.append(new CheckBox(getName() + "_en", "1", false)
			.setAttribute("onclick", "return enable_" + getName() + "()")
			.toString());
        html.append("</td>\n<td>\n");
        html.append(new SelectBox(getName() + "_add", unselectedOptions,
        	m_Request).toString());
        html.append("<script type=\"text/javascript\">//<![CDATA[\n" +
			"function enable_" + getName() + "() {\n" +
			"document.getElementById('" + getName() + "_add').disabled = " +
			"!document.getElementById('" + getName() + "_en').checked;\n" +
			"return true; }\n" +
			"enable_" + getName() + "(); //]]></script>\n");
        
        if (m_CheckBoxNames.length > 0)
        {
        	html.append("<div class=\"checkboxes\">");
            for (int i=0; i < m_CheckBoxNames.length; i++)
            {
            	html.append(new CheckBox(m_CheckBoxNames[i], m_CheckBoxLabels[i],
            			"1", false));
            }
            html.append("</div>");
        }
        
        html.append("</td>\n</tr>\n</table>\n");
        
        return html.toString();
    }
    
    public void assertField(HtmlIterator i)
    {
    	i.assertInput(getName(), (String)null); // fixme
    }
}