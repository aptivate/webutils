/**
 * 
 */
package org.aptivate.web.controls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aptivate.web.utils.EditField;
import org.aptivate.web.utils.HtmlIterator;

public class SelectBox extends StringValuedControl
{
    private List<Object> m_Options;
    private boolean m_FixAmpersands;
    private int m_MaxLength;
    public SelectBox(String name, List options, HttpServletRequest request)
    {
        super(name, request == null ? null : request.getParameter(name));
        m_Options = options;
    }
    public SelectBox(String name, List options, String paramValue)
    {
        super(name, paramValue);
        m_Options = options;
    }
    public SelectBox(String name, List options)
    {
    	super(name, null);
        m_Options = options;
    }
    public SelectBox setFixAmpersands(boolean value)
    {
        m_FixAmpersands = value;
        return this;
    }
    public SelectBox setMaxLength(int value)
    {
        m_MaxLength = value;
        return this;
    }
    public String toString()
    {
        StringBuffer html = new StringBuffer();
        
        html.append("<select " +
                "id=\"" + getName() + "\" " +
                "name=\"" + getName() + "\"");

        appendAttributes(html);
        html.append(">\n");
        
        String value = getValue();
        // if (value == null) value = "";
        
        for (Iterator j = m_Options.iterator(); j.hasNext(); )
        {
            Object row = j.next();
            String selected = "";
            
            if (value != null && EditField.getRowValue(row).equals(value))
            {
                selected = " selected=\"selected\"";
            }

            html.append("<option value=\"");
            html.append(EditField.attrib(EditField.getRowValue(row)));
            html.append("\"");
            html.append(selected);
            html.append(">");
            html.append(EditField.escapeEntities(EditField.getRowLabel(row), m_FixAmpersands,
                m_MaxLength));
            html.append("</option>\n");
        }
        
        html.append("</select>\n");
        return html.toString();
    }
    public void assertField(HtmlIterator i) throws Exception
    {
    	i.assertSelectFromList(getName(), getValue(),
        	m_Options, m_FixAmpersands, m_MaxLength);
    }
    public List<Object> getOptions()
    {
        return new ArrayList<Object>(m_Options);
    }
}