
package psl.tagprocessor.parser;

import java.io.*;
import java.util.Hashtable;
import siena.AttributeValue;

public class MyParserTest {
  
  public static void main(String [] args) {
      Hashtable ht = new Hashtable();
      MyParser parser;

      try {
	  if(args.length == 1) {
	      BufferedReader reader = new BufferedReader
		  (new FileReader(args[0]));
	      parser = new MyParser(new FileReader(args[0]));
	  } else {
	      parser = new MyParser(System.in);
	  }
	  parser.setHashtable(ht);
	  while(true)
	      parser.parseOneLine();
      
      } catch(Exception e) {
	  e.printStackTrace();
      }
  }
}
