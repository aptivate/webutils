/**
 * 
 */
package org.aptivate.web.controls;

import org.aptivate.web.utils.EditField;
import org.aptivate.web.utils.HtmlIterator;

public class TextField extends StringValuedControl
{
    public TextField(String name, String paramValue)
    {
        super(name, paramValue);
    }
    public String toString()
    {
        StringBuffer html = new StringBuffer();
        html.append("<input type=\"text\" name=\"");
        html.append(EditField.attrib(getName()));
        html.append("\" id=\"");
        html.append(EditField.attrib(getName()));
        html.append("\"");
        
        String value = getValue();
        if (value != null)
        {
            html.append(" value=\"").append(EditField.attrib(value)).append("\"");
        }
        
        appendAttributes(html);
        html.append(" />");
        return html.toString();
    }
    public void assertField(HtmlIterator i) throws Exception
    {
    	i.assertInput(getName(), getValue());
    }
}