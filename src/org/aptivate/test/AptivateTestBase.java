package org.aptivate.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

public class AptivateTestBase extends TestCase
{
    public static void assertNotContains(Object item, Collection collection)
    {
    	assertFalse(collection.toString() + " should not contain " +
    		item.toString(), collection.contains(item));
    }

    public static void assertNotContains(Object item, String [] items)
    {
    	assertNotNull(items);
    	assertNotContains(item, Arrays.asList(items));
    }
    
    public void assertEquals(List expected, List actual)
    {
    	List missing = new ArrayList(expected);
    	missing.removeAll(actual);
    	assertEquals("Expected items not found: " + missing.toString(),
        		0, missing.size());

    	List unexpected = new ArrayList(actual);
    	unexpected.removeAll(expected);
    	assertEquals("Unexpected items found: " + unexpected.toString(),
    		0, unexpected.size());
    }
}
