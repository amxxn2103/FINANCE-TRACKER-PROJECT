import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;    
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class Expenditure
 */
@WebServlet("/Expenditure")
public class Expenditure extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Step 1: Prepare list of variables used for database connections
	private String jdbcURL = "jdbc:mysql://localhost:3306/financetracker";
	private String jdbcUsername = "root";
	private String jdbcPassword = "password";
	// Step 2: Prepare list of SQL prepared statements to perform CRUD to our
	// database
	private static final String INSERT_EXPENDITURE_SQL = "INSERT INTO expenditure" + " (id,idfinance, type, amount, date) VALUES " + " (?, ?, ?, ?);";
	private static final String SELECT_EXPENDITURE_BY_ID = "select id,idfinance,type,amount,date from expenditure where idfinance =?";
	private static final String SELECT_ALL_EXPENDITURE = "select * from expenditure";
	private static final String DELETE_EXPENDITURE_SQL = "delete from expenditure where id = ?;";
	private static final String UPDATE_EXPENDITURE_SQL = "update expenditure set id = ?,idfinance= ?, type =?,amount =?, date =? where id = ?;";

	// Step 3: Implement the getConnection method which facilitates connection to
	// the database via JDBC
	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Expenditure() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		try {
			switch (action) {
			
			case "/Expenditure/add":
				addExpenditure(request, response);
				break;
			
			case "/Expenditure/create":
				createExpenditure(request, response);
				break;
				
			
		
			
			
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	public void addExpenditure(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		LocalDateTime now = LocalDateTime.now();  
		String date = dtf.format(now);
		String idfinance = request.getParameter("idfinance");
		request.setAttribute("currentDate", date);
		request.setAttribute("idfinance", idfinance);
		
		request.getRequestDispatcher("/expenditureadd.jsp").forward(request, response);
		
	}
	private void createExpenditure(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		response.setContentType("text/html");
		//Step 1: Initialize a PrintWriter object to return the html values via the response
				PrintWriter out = response.getWriter();
				//Step 2: retrieve the 3 parameters from the request from the web form
				String idfinance = request.getParameter("idfinance");
				request.setAttribute("idfinance", idfinance);
				String n = request.getParameter("idfinance");
				String p = request.getParameter("type");
				String e = request.getParameter("amount");
				String c = request.getParameter("date");
				
				//Step 3: attempt connection to database using JDBC, you can change the username and password accordingly using the phpMyAdmin > User Account dashboard
				try {
				 Class.forName("com.mysql.jdbc.Driver");
				 Connection con = DriverManager.getConnection(
				jdbcURL, jdbcUsername, jdbcPassword);
				//Step 4: implement the sql query using prepared statement (https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html)
				 PreparedStatement ps = con.prepareStatement("insert into EXPENDITURE values(?,?,?,?,?)");
				
				//Step 5: parse in the data retrieved from the web form request into the prepared statement accordingly
				 ps.setString(1, null);
				 ps.setString(2, n);
				 ps.setString(3, p);
				 ps.setString(4, e);
				 ps.setString(5, c);
				//Step 6: perform the query on the database using the prepared statement
				 int i = ps.executeUpdate();
				 //Step 7: check if the query had been successfully execute, return “You are successfully registered” via the response,
				 
				 
				 if (i > 0){
					 response.sendRedirect("http://localhost:8090/FinanceTrackerWebsite/ReturnFinanceServlet/dashboard");
				 }
				 }
				 //Step 8: catch and print out any exception
				 catch (Exception exception) {
				 System.out.println(exception);
				 out.close();
				 }
				 
				 }
		public List listExpenditure(HttpServletRequest request, HttpServletResponse response,String userid)
			throws SQLException, IOException, ServletException {
			
			List <ListExpenditure> expenditure = new ArrayList <>();
			try (Connection connection = getConnection();
					
				// Step 5.1: Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EXPENDITURE_BY_ID);) {
				preparedStatement.setString(1, userid);
				// Step 5.2: Execute the query or update query
				ResultSet rs = preparedStatement.executeQuery();
				// Step 5.3: Process the ResultSet object.
				while (rs.next()) {
				String id = rs.getString("id");
				String idfinance = rs.getString("idfinance");
				String type = rs.getString("type");
				String amount = rs.getString("amount");
				String date = rs.getString("date");
				expenditure.add(new ListExpenditure(id, idfinance, type, amount, date));
				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Step 5.4: Set the users list into the listUsers attribute to be pass to the
		// userManagement.jsp
		
		return expenditure;
	}
				
	}
				
		

