

import psl.tagprocessor.test.TagProcessorTest;

public class Test {
    
    public static void main(String args[]) {
	if(args.length != 2) {
	    System.err.println("Usage: java Test <xmlfile> <jarpath>");
	    System.exit(1);
	}
	TagProcessorTest test = new TagProcessorTest(args[0], args[1]);
	test.test();
    }
}
