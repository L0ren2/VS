package ourservlet;

import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletPP
 */
@WebServlet(urlPatterns = "/ServletPP", asyncSupported = true)
public class ServletPP extends HttpServlet {
	private Endpoint websock;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletPP() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(System.getProperty("user.dir"));
		FileReader fr = new FileReader("./OurServlet/src/main/webapp/index.html");
		char[] buffer = new char[2000];
		fr.read(buffer);
		fr.close();
		String buffer_to_str = new String(buffer);
		response.getWriter().append(buffer_to_str);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//extract data from request
		String topic = request.getParameter("topic");
		String IP = request.getParameter("ip");
		String PORT = request.getParameter("port");
		
		try {
			// start websocket
			websock = new Endpoint();
			websock.sendMessage("connected to websocket");
			
			// start MqttReceiver
			MqttReceiver rec = new MqttReceiver(topic, IP, PORT, websock);
			rec.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// start websocket before sending back the html with js
		
		System.out.println("Reading html");
		// return html doc (dynamic)
		FileReader fr = new FileReader("./OurServlet/src/main/webapp/messenger.html");
		char[] buffer = new char[2000];
		fr.read(buffer);
		fr.close();
		String buffer_to_str = new String(buffer);
		response.getWriter().append(buffer_to_str);
	}
}


