
package psl.tagprocessor.parser;

import java.io.*;
import java.util.*;
import siena.AttributeValue;

public class ExpParserTest {
    
    public ExpParserTest() {
	
    }

    public static void main(String [] args) {
	try {
	    BufferedReader reader = new BufferedReader
		(new InputStreamReader(System.in));
	    ExpParser parser = new ExpParser(new Hashtable());
	    System.out.print("Please enter a term for evaluation: ");
	    String term = reader.readLine();
	    while(!term.equals("exit")) {
		AttributeValue result = parser.parse(term);
		if(result != null) {
		    System.out.println("it is evaluated to " 
				       + result.toString());
		} else {
		    System.out.println("null");
		}
		System.out.print("Please enter a term for evaluation: ");
		term = reader.readLine();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
