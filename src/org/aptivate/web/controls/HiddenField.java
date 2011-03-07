/**
 * 
 */
package org.aptivate.web.controls;

import org.aptivate.web.utils.EditField;
import org.aptivate.web.utils.HtmlIterator;
import org.aptivate.web.utils.HtmlIterator.StartElementNode;

public class HiddenField extends StringValuedControl
{
    public HiddenField(String name, String paramValue)
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
        return html.toString();
    }
    public void assertField(HtmlIterator i) throws Exception
    {
    	i.assertInput(getName(), getValue());
    }
}