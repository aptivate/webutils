package org.aptivate.web.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aptivate.web.controls.CheckBox;
import org.aptivate.web.controls.DateField;
import org.aptivate.web.controls.HiddenField;
import org.aptivate.web.controls.ReadOnlyField;
import org.aptivate.web.controls.SelectBox;
import org.aptivate.web.controls.SelectTable;
import org.aptivate.web.controls.SubmitButton;
import org.aptivate.web.controls.TextArea;
import org.aptivate.web.controls.TextField;
import org.htmlparser.util.Translate;

public class EditField
{
    private HttpServletRequest m_Request;
    
    public EditField(HttpServletRequest request)
    {
        m_Request = request;
    }

    /**
     * Escapes any entities in a string, e.g. replaces '&' with '&amp;',
     * for inclusion in HTML pages.
     * 
     * @param input - the string to have entities escaped.
     * @return - the string containing escaped entities.
     */
    public static String escapeEntities(Object input)
    {
        return escapeEntities(input, true, 0);
    }

    /**
     * Converts a string which may or may not contain entities into one
     * which only uses the entities &amp, &lt and &gt, and is therefore
     * compact but can be used safely in HTML.
     *  
     * Optionally tries to detect and fixing raw ampersands, and
     * optionally truncates a string to a specified maximum length.
     * 
     * @param input the string to be processed
     * @param fixAmpersands set to true to replace bare ampersands (that
     * don't appear to be part of an HTML entity) with ampersand entities 
     * @param maxLength the length to which values will be truncated, or 0
     * for no truncation
     * @return the resulting string
     */
    public static String escapeEntities(Object inputObject,
    	boolean fixAmpersands, int maxLength)
    {
        if (inputObject == null)
        {
            return null;
        }
        
        String input = inputObject.toString();

        if (fixAmpersands)
        {
            input = input.replaceAll("&([^&; ]*)&", "&amp;$1&amp;");
            input = input.replaceAll("&([^&; ]*) ", "&amp;$1 ");
            input = input.replaceAll("&([^&; ]*)$", "&amp;$1");
            input = Translate.decode(input);
        }
        
        if (maxLength > 0)
        {
            input = input.replaceFirst("^(.{" + maxLength + "}).{2}.*", "$1...");
        }

        return input.replaceAll("&", "&amp;")
        	.replaceAll("<", "&lt;")
        	.replaceAll(">", "&gt;");
    }

    /**
     * Returns a suitably-escaped string for an attribute value (having
     * changed double-quotes to entities as well as escapeEntities)
     * @see http://www.w3.org/TR/REC-xml/#syntax 
     * @param value the attribute value to be escaped
     * @return the escaped value
     */
    public static String attrib(String value)
    {
        return escapeEntities(value).replaceAll("\"", "&quot;");
    }
    
    /**
     * Returns the HTML code for a text edit field in the main table on the Edit/Export 
     * page, corresponding to a DB field. 
     * @deprecated use the two-argument form instead
     * @param name - the name of the field.
     * @param value - the value of the field - either the current DB entry, or new
     * data typed in by the user.
     * @param editable - is the cell marked for editing or not?
     * @param width - the width of the table cell
     * @return - the HTML code for the table cell contents.
     */
    public String text(String name, String value, boolean editable, int width)
    {
        if (m_Request != null && m_Request.getParameter(name) != null)
        {
            value = m_Request.getParameter(name);
        }
        
        if (editable)
        {
            return "<input type=\"text\" name=\"" + name + "\" " +
                "value=\"" + attrib(value) + "\" size=\"" + width + 
                "\" />";
        }
        else
        {
            return escapeEntities(value);
        }
    }

    /**
     * Returns a TextField object which can generate HTML code for a 
     * text field in an HTML form. 
     * @param name the name of the field.
     * @param defaultValue the default value of the field, used unless
     * overridden by a parameter in the request.
     * @return a new TextField object
     */
    public TextField text(String name)
    {
    	if (m_Request == null)
    	{
    		return new TextField(name, null);
    	}
    	else
    	{
    		return new TextField(name, m_Request.getParameter(name));
    	}
    }
    
    /**
     * Returns a DateField object which can generate HTML code for a 
     * date field in an HTML form. 
     * @param name the name of the field.
     * @param defaultValue the default value of the field, used unless
     * overridden by a parameter in the request.
     * @return a new DateField object
     */
    public DateField date(String name)
    {
    	if (m_Request == null)
    	{
    		return new DateField(name, null);
    	}
    	else
    	{
    		return new DateField(name, m_Request.getParameter(name));
    	}
    }
    

    /**
     * Returns a ReadOnlyField object which can generate HTML code for a 
     * read-only field (hidden field + static text) in an HTML form. 
     * @param name the name of the field.
     * @param defaultValue the default value of the field, used unless
     * overridden by a parameter in the request.
     * @return a new TextField object
     */
    public ReadOnlyField readOnlyField(String name)
    {
    	if (m_Request == null)
    	{
    		return new ReadOnlyField(name, null);
    	}
    	else
    	{
    		return new ReadOnlyField(name, m_Request.getParameter(name));
    	}
    }

    /**
     * Returns a HiddenField object which can generate HTML code for a 
     * hidden field in an HTML form. 
     * @param name the name of the field.
     * @param defaultValue the default value of the field, used unless
     * overridden by a parameter in the request.
     * @return a new HiddenField object
     */
    public HiddenField hidden(String name)
    {
    	if (m_Request == null)
    	{
    		return new HiddenField(name, null);
    	}
    	else
    	{
    		return new HiddenField(name, m_Request.getParameter(name));
    	}
    }

    /**
     * Returns a TextArea object which can generate HTML code for a 
     * text area in an HTML form. 
     * @param name the name of the field.
     * @param defaultValue the default value of the field, used unless
     * overridden by a parameter in the request.
     * @return a new TextArea object
     */
    public TextArea getTextArea(String name)
    {
    	if (m_Request == null)
    	{
    		return new TextArea(name, null);
    	}
    	else
    	{
    		return new TextArea(name, m_Request.getParameter(name));
    	}
    }

    public String hidden(String name, String value)
    {
        if (m_Request != null && m_Request.getParameter(name) != null)
        {
        	value = m_Request.getParameter(name);
        }
        
        if (value == null) value = "";
        
        return "<input type=\"hidden\" name=\"" + name + "\" " +
            "value=\"" + attrib(value) + "\" />";
    }

    public static String getRowValue(Object row)
    {
        if (row instanceof String)
        {
            return (String)row;
        }
        else if (row instanceof String[])
        {
            String [] values = (String[])row;
            return values[0];
        }
        
        throw new IllegalArgumentException("row must be String or String[]");
    }
    
    public static String getRowLabel(Object row)
    {
        if (row instanceof String)
        {
            return (String)row;
        }
        else if (row instanceof String[])
        {
            String [] values = (String[])row;
            if (values.length == 1)
            {
                return values[0];
            }
            else
            {
                return values[1];
            }
        }
        
        throw new IllegalArgumentException("row must be String or String[]");
    }
    
    /**
     * Returns the HTML code for a drop down box edit field, or static text,
     * corresponding to a database field.
     * @deprecated prefer the two-argument form
     * @param name the name of the field
     * @param value the default value for the field, if not overridden by
     * a value provided in the request
     * @param editable true to display a control, false to display static text
     * (the Label corresponding to the current Value in the available options)
     * @param options a List of Strings, or String arrays of [value] or
     * [value, label], from which the user may choose (for an editable
     * control), or from which the value will be looked up and the label
     * displayed (for non-editable text)
     * @param fixAmpersands set to true to replace bare ampersands (that
     * don't appear to be part of an HTML entity) with ampersand entities 
     * @param maxLength the length to which values will be truncated, or 0
     * for no truncation
     * @return the HTML code for the select control or static text
     */

    public String select(String name, String value, boolean editable, 
        List options, boolean fixAmpersands, int maxLength)
    {
        /*
        if (m_Request.getParameter(name) != null)
        {
            value = m_Request.getParameter(name);
        }
        
        if (value == null && options.size() > 0)
        {
        	String [] row = (String[])options.get(0);
        	value = row[0];
        }
        */

        if (!editable)
        {
            if (m_Request != null && m_Request.getParameter(name) != null)
            {
                value = m_Request.getParameter(name);
            }

            for (Iterator j = options.iterator(); j.hasNext(); )
            {
                Object row = j.next();
                if (value != null && getRowValue(row).equals(value))
                {
                    return escapeEntities(getRowLabel(row),
                        fixAmpersands, maxLength);
                }
            }
            
            return "";
        }

        SelectBox sb = new SelectBox(name, options, m_Request);
        sb.setDefaultValue(value);
        sb.setFixAmpersands(fixAmpersands);
        sb.setMaxLength(maxLength);
        
        return sb.toString();
    }

    /**
     * Returns a SelectBox object that can generate the HTML code for a
     * drop down box edit field.
     * @param name the name of the field
     * @param options a List of Strings, or String arrays of [value] or
     * [value, label], from which the user may choose (for an editable
     * control)
     * @return a new SelectBox object
     */

    public SelectBox select(String name, List options)
    {
        return new SelectBox(name, options, m_Request);
    }

    /**
     * Returns the HTML code for a multi-select list box form field.
     * @param name the name of the field.
     * @param options a List of String arrays of [value, label] from which
     * the user may choose (for an editable control) or from which the value
     * will be looked up and the label displayed (for non-editable text)
     * @param selected the default initially selected values of the field,
     * if none are provided in the request to override the default.
     * @param fixAmpersands set to true to replace bare ampersands (that
     * don't appear to be part of an HTML entity) with ampersand entities 
     * @param maxLength the length to which values will be truncated, or 0
     * for no truncation
     * @return the HTML code for the select control
     */

    public String selectMulti(String name, List options, List selected,
        boolean fixAmpersands, int maxLength)
    {
        String [] requestSel = null;
        
        if (m_Request != null)
        {
        	requestSel = m_Request.getParameterValues(name); 
        }
        
        if (requestSel != null)
        {
            selected = Arrays.asList(requestSel);
        }

        List selected2 = new ArrayList();
        for (Iterator i = selected.iterator(); i.hasNext();)
        {
            String oldValue = (String)i.next();
            selected2.add(escapeEntities(oldValue));
        }
        selected = selected2;

        StringBuffer html = new StringBuffer();
        html.append("<select name=\"" + name + "\" multiple=\"1\">\n");
        
        for (Iterator i = options.iterator(); i.hasNext(); )
        {
            String [] row = (String[])i.next();
            
            html.append("<option value=\"" + row[0] + "\" " +
                (selected.contains(row[0]) ? "selected=\"1\" " : "") + ">" +
                escapeEntities(row[1], fixAmpersands, maxLength) +
                "</option>\n");
        }
        html.append("</select>\n");
        return html.toString();
    }

    /**
     * Returns the HTML code for a multi-select list based on a table 
     * with checkboxes, and a drop-down list in the bottom row to add
     * a new entry. Requires special support from the container form
     * to return to the same page if a new entry is added to the list.
     * May be easier to use than the browser's multi-select box.
     * @param name the name of the field.
     * @param values the currently selected values of the field,
     * used as a default if none are provided in the request.
     * @param fixAmpersands set to true to replace bare ampersands (that
     * don't appear to be part of an HTML entity) with ampersand entities 
     * @param maxLength the length to which values will be truncated, or 0
     * for no truncation
     * @return the HTML code for the table rows, excluding the wrapping table
     */

    public String selectTable(String name, List options, List selected,
        boolean fixAmpersands, int maxLength)
    {
        return selectTable(name, options).toString();
    }

    public SelectTable selectTable(String name, List options)
    {
    	return selectTable(name, options, new String[] {}, new String[] {});
    }

    public SelectTable selectTable(String name, List options,
    		String[] checkBoxNames, String[] checkBoxLabels)
    {
    	String [] requestSel = null;

    	if (m_Request != null)
    	{
    		requestSel = m_Request.getParameterValues(name); 
    	}

    	return new SelectTable(name, requestSel, options, checkBoxNames, 
    		checkBoxLabels, m_Request);
    }
    
    /**
     * Returns the HTML code for a checkbox edit field in the main table on the
     * Edit/Export page.
     * @deprecated use checkbox() instead
     * @param name - the name of the field.
     * @param value - the value of the field - either the current DB entry, or new
     * data typed in by the user.
     * @param editable - is the cell marked for editing or not?
     * @return - the HTML code for the table cell contents.
     */
   
    public String check(String name, boolean value, boolean editable)
    {
        if (editable)
        {
            return "<input type=\"checkbox\" name=\"" + name + "\" " +
            		"value=\"1\" />";
        }
        else
        {
            return value ? "Yes" : "";
        }
    }

    /**
     * Returns a CheckBox object which can generate HTML code for a checkbox
     * form field.
     * @param name the name of the field.
     * @param value the default value of the field, used unless overridden
     * by a form parameter.
     * @return a new CheckBox object.
     */
   
    public CheckBox checkbox(String name)
    {
        return new CheckBox(name, m_Request);
    }

    /**
     * Returns a SubmitButton object which can generate HTML code for a form
     * submit button. 
     * @param name the parameter name of the button.
     * @param value the value and label of the button.
     * @return a new SubmitButton object.
     */
   
    public SubmitButton submit(String name, String value)
    {
        return new SubmitButton(name, value);
    }

    public String yesNo(String name, boolean value)
    {
        List options = new ArrayList();
        options.add(new String[]{"1","Yes"});
        options.add(new String[]{"0","No"});
        String valuestr = "";
        if (value )
        {
            valuestr = "1";
        }
        else
        {
            valuestr = "0";
        }
        return select(name, valuestr, true, options, false, 0);
    }

    public String yesNoUndef(String name, String value)
    {
        List options = new ArrayList();
        options.add(new String[]{"",""});
        options.add(new String[]{"1","Yes"});
        options.add(new String[]{"0","No"});
        return select(name, value, true, options, false, 0);
    }

    public SelectBox yesNoUndef(String name)
    {
        List options = new ArrayList();
        options.add(new String[]{"",""});
        options.add(new String[]{"1","Yes"});
        options.add(new String[]{"0","No"});
        return new SelectBox(name, options);
    }
    
    public static String booleanValue(Object value)
    {
    	return ((Boolean)value).booleanValue() ? "1" : "0";
    }
}
