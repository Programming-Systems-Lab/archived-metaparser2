/*
  Copyright (c) 2000: The Trustees of Columbia University and the City of New York.
  All Rights Reserved.
*/

package psl.tagprocessor.test;

import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Hashtable;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import psl.tagprocessor.*;

/*
 * The test class for tagprocessor.
 *
 * @author: Simin Wang
 * @version: 1.0 
 */

import java.util.*;
import siena.AttributeValue;

public class TagProcessorTest {

    String _xmlfile;
    String _jarfile;

    public TagProcessorTest(String f1, String f2) {
	_xmlfile = f1;
	_jarfile = f2;
    }

    public void test() {
	try {
	    URL aurl[] = { new URL(_jarfile) };
	    System.out.println("Working with " + _xmlfile);
	    Hashtable hashtable = new Hashtable();
	    XMLReader xmlreader = 
		XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
	    TagProcessor tagprocessor = new TagProcessorImpl();
	    tagprocessor.setResource(aurl[0].getPath());
	    tagprocessor.init(hashtable);
	    xmlreader.setContentHandler(tagprocessor.getContentHandler());
	    xmlreader.parse(_xmlfile);
	    tagprocessor.process();
	    printTable(hashtable);
	    //System.out.println(hashtable);
	} catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public void printTable(Hashtable hashtable) {
	Set set = hashtable.keySet();
	Iterator it = set.iterator();
	while(it.hasNext()) {
	    String key = (String) it.next();
	    AttributeValue v = (AttributeValue) hashtable.get(key);
	    int type = v.getType();
	    String value = "";
	    if(type == AttributeValue.BOOL) {
		value += v.booleanValue();
		System.out.print("(boolean) ");
	    } else if(type == AttributeValue.INT) {
		value += v.intValue();
		System.out.print("(int) ");
	    } else if(type == AttributeValue.LONG) {
		value += v.longValue();
		System.out.print("(long) ");
	    } else if(type == AttributeValue.STRING) {
		value += v.stringValue();
		System.out.print("(String) ");
	    } else if(type == AttributeValue.DOUBLE) {
		value += v.doubleValue();
		System.out.print("(double) ");
	    }    
	    System.out.println(key + "=" + value);
	}	
    }

}






