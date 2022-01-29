

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//Import these libraries from java.io and java.sql
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet implementation class LoginRegisterServlet
 */
@WebServlet("/LoginRegisterServlet")
public class LoginRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginRegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("login") != null) {
			response.setContentType("text/html");
			//Step 1: Initialize a PrintWriter object to return the html values via the response
			PrintWriter out = response.getWriter();
			//Step 2: retrieve the four parameters from the request from the web form
			String p = request.getParameter("password");
			String e = request.getParameter("email");
			//Step 3: attempt connection to database using JDBC, you can change the username and password accordingly using the phpMyAdmin > User Account dashboard
			try {
			 Class.forName("com.mysql.jdbc.Driver");
			 Connection con = DriverManager.getConnection(
			 "jdbc:mysql://localhost:3306/financetracker", "root", "password");
			 
			//Step 4: implement the sql query using prepared statement (https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html)
			  PreparedStatement ps = con.prepareStatement("select id from user where email = ? and password = ?");
			//Step 5: parse in the data retrieved from the web form request into the prepared statement accordingly
			   ps.setString(1, e);
			   ps.setString(2, p);
			 //Step 6: perform the query on the database using the prepared statement
			   ResultSet i = ps.executeQuery();
			 //Step 7: check if the query had been successfully execute, return �You are successfully registered� via the response,
			   if (i.next()) {
				   
				   String id = i.getString("id");
				   if(id!=""){
				   Cookie ck=new Cookie("USERID",id);//creating cookie object  
				   response.addCookie(ck);//adding cookie in the response  
				   out.println("<script type=\"text/javascript\">");
		            out.println("alert('user"+id+" have logged in!');");
		            out.println("window.location.href = \"index.jsp\";");
		            out.println("</script>");
		            
				   }
				   
			   } 
			   else {
				   out.println("<script type=\"text/javascript\">");
		            out.println("alert('Wrong email or password');");
		            out.println("window.location.href = \"login.jsp\";");
		            out.println("</script>");
					 out.close();
			   }
			   }
			//Step 8: catch and print out any exception
			catch (Exception exception) {
				 System.out.println(exception);
				 out.close();
				}

			// TODO Auto-generated method stub
			doGet(request, response);			
		}
		else if (request.getParameter("register") != null) {
			response.setContentType("text/html");
			//Step 1: Initialize a PrintWriter object to return the html values via the response
			PrintWriter out = response.getWriter();
			//Step 2: retrieve the four parameters from the request from the web form
			String p = request.getParameter("password");
			String e = request.getParameter("email");
			//Step 3: attempt connection to database using JDBC, you can change the username and password accordingly using the phpMyAdmin > User Account dashboard
			
			try {
			 Class.forName("com.mysql.jdbc.Driver");
			 Connection con = DriverManager.getConnection(
			 "jdbc:mysql://localhost:3306/financetracker", "root", "password");
			 
			//Step 4: implement the sql query using prepared statement (https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html)
			  PreparedStatement ps = con.prepareStatement("insert into USER values(?,?,?)");
			//Step 5: parse in the data retrieved from the web form request into the prepared statement accordingly
			   ps.setString(1, null);
			   ps.setString(2, e);
			   ps.setString(3, p);
			 //Step 6: perform the query on the database using the prepared statement
			   int i = ps.executeUpdate();
			 //Step 7: check if the query had been successfully execute, return �You are successfully registered� via the response,
			   if (i > 0){

				   out.println("<script type=\"text/javascript\">");
		            out.println("alert('User is now registered');");
		            out.println("window.location.href = \"login.jsp\";");
		            out.println("</script>");
			   }
			   }
			//Step 8: catch and print out any exception
			catch (Exception exception) {
				 System.out.println(exception);
				 out.println("<script type=\"text/javascript\">");
		            out.println("alert('Email already registered');");
		            out.println("window.location.href = \"register.jsp\";");
		            out.println("</script>");

				 out.close();
				}

			// TODO Auto-generated method stub
			doGet(request, response);
		}
		else if (request.getParameter("logout") != null) {
			
			Cookie ck=new Cookie("USERID","");//creating cookie object 
			ck.setMaxAge(0);
			 response.addCookie(ck);//adding cookie in the response  
			// TODO Auto-generated method stub
			doGet(request, response);
			response.sendRedirect("http://localhost:8090/FinanceTrackerWebsite/login.jsp");
		}
		
	}

}
