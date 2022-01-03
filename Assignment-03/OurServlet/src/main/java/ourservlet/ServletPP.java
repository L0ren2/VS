package ourservlet;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletPP
 */
@WebServlet(urlPatterns = "/ServletPP", asyncSupported = true)
public class ServletPP extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletPP() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append(
				  "<form method=\"POST\" action=\"/OurServlet/ServletPP\" id=\"form\">"
				+	 "<input type=\"submit\" value=\"submit\" text=\"submit\" />"
				+ "</form>"
		);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		final AsyncContext asyncContext = request.startAsync(request, response);
		new Thread() {
			@Override
			public void run() {
				try {
					ServletResponse response = asyncContext.getResponse();
					response.getWriter().append("Using POST.").flush();
					response.flushBuffer();
					Thread.sleep(1000);
					response.getWriter().append("\n1 Second passed.").flush();
					response.flushBuffer();
					Thread.sleep(1000);
					response.getWriter().append("\n1 Second passed.").flush();;
					asyncContext.complete();
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
}



