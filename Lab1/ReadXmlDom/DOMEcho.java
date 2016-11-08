package tp1;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

public class DOMEcho {
	 static final String outputEncoding = "UTF-8";

	    private static void usage() {
	        // ...
	    }

	    public static void main(String[] args) throws Exception {
	        String filename = null;
	        
	        try {
	        	
	        File fXmlFile = new File("map.xml");
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(fXmlFile);
	        
	        
	        }
	    
	        for (int i = 0; i < args.length; i++) {
	            if (...) { 
	                // ...
	            } 
	            else {
	                filename = args[i];
	                if (i != args.length - 1) {
	                    usage();
	                }
	            }
	        }

	        if (filename == null) {
	            usage();
	        }
	    }
}
