/**
 * 
 */
package org.aptivate.web.controls;

import org.aptivate.web.utils.EditField;
import org.aptivate.web.utils.HtmlIterator;
import org.aptivate.web.utils.HtmlIterator.Attributes;

public class TextArea extends StringValuedControl
{
    public TextArea(String name, String paramValue)
    {
        super(name, paramValue);
    }
    public String toString()
    {
        StringBuffer html = new StringBuffer();
        html.append("<textarea name=\"");
        html.append(EditField.attrib(getName())).append("\"");
        appendAttributes(html);
        html.append(">");
        String value = getValue();
        if (value != null)
        {
            html.append(EditField.escapeEntities(value));
        }
        html.append("</textarea>");
        return html.toString();
    }
    public void assertField(HtmlIterator i) throws Exception
    {
    	i.assertSimple("textarea", getValue(),
    		new Attributes("name", getName()));
    }
}