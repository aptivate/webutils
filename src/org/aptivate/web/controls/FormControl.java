/**
 * 
 */
package org.aptivate.web.controls;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.aptivate.web.utils.EditField;
import org.aptivate.web.utils.HtmlIterator;

public abstract class FormControl
{
    private String m_Name;
    private Map m_Attributes = new HashMap();
    protected FormControl(String name)
    {
        m_Name = name;
    }
    public final String getName()
    {
        return m_Name;
    }
    public final FormControl setAttribute(String name, String value)
    {
        m_Attributes.put(name, value);
        return this;
    }
    public final FormControl setAttributes(Map attributes)
    {
        m_Attributes = attributes;
        return this;
    }
    protected final void appendAttributes(StringBuffer buf)
    {
        for (Iterator i = m_Attributes.keySet().iterator(); i.hasNext();)
        {
            String attName  = (String)i.next();
            String attValue = (String)m_Attributes.get(attName);
            buf.append(" ").append(attName).append("=\"")
                .append(EditField.attrib(attValue)).append("\"");
        }
    }
    public abstract void assertField(HtmlIterator i) throws Exception;
}