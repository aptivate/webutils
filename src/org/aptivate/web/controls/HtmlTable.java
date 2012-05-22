package org.aptivate.web.controls;

import org.aptivate.web.utils.EditField;

public class HtmlTable
{
	private StringBuffer out = new StringBuffer();
	private boolean alternateRow = true;
	
	public HtmlTable(String... headerRow)
	{
		out.append("<table>\n");
		out.append("\t<thead>\n");
		out.append("\t\t<tr>\n");
		for (String column : headerRow)
		{
			out.append("\t\t\t<th>");
			out.append(EditField.escapeEntities(column));
			out.append("</th>\n");
		}
		out.append("\t\t</tr>\n");
		out.append("\t</thead>\n");
		out.append("\t<tbody>\n");
	}
	
	public void addRow(String... rowValues)
	{
		out.append("\t\t<tr ");
		out.append(alternateRow ? "class='alt'" : "");
		out.append(">\n");
		for (String column : rowValues)
		{
			out.append("\t\t\t<td>" + column + "</td>\n");
		}
		out.append("\t\t</tr>\n");
		alternateRow = !alternateRow;
	}
	
	public String toString()
	{
		return out.toString() + "\t</tbody>\n</table>\n";
	}
}