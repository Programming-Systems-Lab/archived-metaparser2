
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Oracle extends HttpServlet {

    public static final String data = "D:\\Inetpub\\wwwroot\\oracle.txt";
    private MyTable table;
    
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException {
        
	 try {
	 	response.setContentType("text/plain");
              PrintWriter out = response.getWriter();

		int[] indices = table.get(request.getParameter("xmlns"));

		for(int i = 0; i < indices.length; i++) {
			String filename = table.index(i);

			if(filename == null) {
				throw new FileNotFoundException();
			}

			FileReader file = new FileReader(filename);
	 
			BufferedReader reader = new BufferedReader(file);

			String line;
	 		while((line = reader.readLine()) != null) {
	 			out.println(line);
	 		}

	 		file.close();
		}
	 } catch (FileNotFoundException fe) {
		response.sendError(response.SC_NOT_FOUND, "file not found");
	 } catch (IOException e) {
		System.out.println("IOException!");
	 }
    }

    public void init() throws ServletException {
    	try {
    		table = new MyTable();
		FileReader file = new FileReader(data);
	 	
		BufferedReader reader = new BufferedReader(file);

		String line;

		while((line = reader.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line);
			if(st.countTokens() != 2) {
				System.out.println("Wrong format!");
			} else {
				table.put(st.nextToken(), st.nextToken());
			}
		}

		file.close();
    	} catch (IOException e) {
		System.out.println("IOException!");
    	}
    }
    
}

