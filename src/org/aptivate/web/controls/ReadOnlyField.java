/**
 * 
 */
package org.aptivate.web.controls;

import org.aptivate.web.utils.EditField;
import org.aptivate.web.utils.HtmlIterator;

public class ReadOnlyField extends StringValuedControl
{
    public ReadOnlyField(String name, String paramValue)
    {
        super(name, paramValue);
    }
    public String toString()
    {
        String value = getValue();
        
        if (value == null)
        {
        	return "";
        }
        
        StringBuffer html = new StringBuffer();
        html.append("<input type=\"hidden\" name=\"");
        html.append(EditField.attrib(getName())).append("\"");
        html.append(" value=\"").append(EditField.attrib(value));
        html.append("\"");
        appendAttributes(html);
        html.append("/>");
    	html.append(EditField.escapeEntities(value));
        return html.toString();
    }
    public void assertField(HtmlIterator i)
    {
    	String value = getValue();
    	if(value == null) {
    		return;
    	}
    	i.assertInput(getName(), getValue());
    	i.assertText(getValue());
    }
}