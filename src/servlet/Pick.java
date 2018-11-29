package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import javaUtil.FileReader;

/**
 * Servlet implementation class SendFile
 */
@WebServlet(name = "Pick", urlPatterns = { "/Pick" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class Pick extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String newFileName;
	private String daSaltare;
	private ArrayList<ArrayList<String>> sheetDataStarter;
	private Map<String,String> esito;

	public Pick() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		daSaltare = request.getParameter("daSaltare");
		newFileName = request.getParameter("newFileName");
		for (Part part : request.getParts()) {

			String contentDisp = part.getHeader("content-disposition");
			String[] tokens = contentDisp.split(";");
			for (String token : tokens) {

				// retrieve data from HTTP header content-disposition
				if (token.trim().contains("starter")) {

					InputStream streamStarter = part.getInputStream();
					sheetDataStarter = FileReader.fillTheSheet(streamStarter, daSaltare);
					System.out.println("starter size: " + sheetDataStarter.size());

				} else if (token.trim().contains("ebaySheet")) {

					InputStream streamModel = part.getInputStream();
					if (sheetDataStarter.size() != 0 && streamModel != null)
						esito = FileReader.readTheModel(streamModel, sheetDataStarter, newFileName);
				}
			}
		}
		if(esito.containsKey("true")) {
			response.sendRedirect("Index.jsp");
		}else {
			HttpSession session= request.getSession(true);
			session.setAttribute("errore", esito.get("false"));
			getServletContext().getRequestDispatcher("/Errore.jsp").forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
