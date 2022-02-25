import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReturnFinanceServlet
 */
@WebServlet("/ReturnFinanceServlet")
public class ReturnFinanceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Step 1: Prepare list of variables used for database connections
	private String jdbcURL = "jdbc:mysql://localhost:3306/financetracker";
	private String jdbcUsername = "root";
	private String jdbcPassword = "password";
	// Step 2: Prepare list of SQL prepared statements to perform CRUD to our
	// database
	private static final String INSERT_FINANCE_SQL = "INSERT INTO finance" + " (id,iduser, income, saving) VALUES "+ " (?, ?, ?);";
	private static final String SELECT_FINANCE_BY_IDUSER = "select id,iduser,income,saving from finance where iduser =?";
	private static final String SELECT_FINANCE_BY_ID = "select id,iduser,income,saving from finance where id =?";
	private static final String SELECT_EXPENDITURE_BY_ID = "select id,idfinance,type,amount,date from expenditure where id =?";
	private static final String SELECT_ALL_FINANCE = "select * from finance";
	private static final String DELETE_FINANCE_SQL = "delete from finance where id = ?;";
	private static final String DELETE_EXPENDITURE_SQL = "delete from expenditure where id = ?;";
	private static final String UPDATE_FINANCE_SQL = "update finance set id = ?,iduser= ?, income =?,saving =? where id = ?;";
	private static final String UPDATE_EXPENDITURE_SQL = "update expenditure set id = ?,idfinance= ?, type =?,amount =?, date=? where id = ?;";
	private Expenditure expenditure = new Expenditure();
	
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
	public ReturnFinanceServlet() {
		super();

		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Step 4: Depending on the request servlet path, determine the function to
		// invoke using the follow switch statement.
		String action = request.getServletPath();
		try {
			switch (action) {
			case "/ReturnFinanceServlet/delete":
				deleteFinance(request, response);
				break;
			case "/ReturnFinanceServlet/deleteexpenditure":
				deleteexpenditure(request, response);
				break;
			case "/ReturnFinanceServlet/edit":
				showEditForm(request, response);
				break;
			case "/ReturnFinanceServlet/update":
				updateFinance(request, response);
				break;
			case "/ReturnFinanceServlet/dashboard":
				listFinance(request, response);
				break;
			
			default:
				listFinance(request, response);
				break;
			case "/ReturnFinanceServlet/editexpenditure":
				showExpenditureEditForm(request, response);
				break;
			case "/ReturnFinanceServlet/updateExpenditure":
				updateExpenditure(request, response);
				break;
			
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	private void listFinance(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		
		try (Connection connection = getConnection();

				// Step 5.1: Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FINANCE_BY_IDUSER);) {
			// Step 5.2: Execute the query or update query
			String userid = "";
			Cookie[] cookies = request.getCookies();
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("USERID") ) {

					userid = cookies[i].getValue();
					
				}
				}
			preparedStatement.setString(1, userid);
			ResultSet rs = preparedStatement.executeQuery();

			
			// Step 5.3: Process the ResultSet object.
			if (rs.next()) {
				String id = rs.getString("id");
				String iduser = rs.getString("iduser");
				String income = rs.getString("income");
				String saving = rs.getString("saving");
				request.setAttribute("expenditure", expenditure.listExpenditure(request, response,id));
				request.setAttribute("listFinance", new Finance(id, iduser, income, saving));
				
				PreparedStatement preparedStatement2 = connection.prepareStatement("select name,surname,bio from profile where iduser =?"); 
				preparedStatement2.setString(1, userid);
				ResultSet rs2 = preparedStatement2.executeQuery();
				if (rs2.next()) {
					request.setAttribute("name", rs2.getString("name") );
					request.setAttribute("surname", rs2.getString("surname"));

				}

				

				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Step 5.4: Set the users list into the listUsers attribute to be pass to the
		// userManagement.jsp
		request.getRequestDispatcher("/returnFinance.jsp").forward(request, response);
	}

	// method to get parameter, query database for existing user data and redirect
	// to user edit page
	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		// get parameter passed in the URL
		String id = request.getParameter("id");
		Finance existingFinance = new Finance("", "", "", "");
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FINANCE_BY_ID);) {
			preparedStatement.setString(1, id);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();
			// Step 4: Process the ResultSet object
			while (rs.next()) {
				id = rs.getString("id");
				String iduser = rs.getString("iduser");
				String income = rs.getString("income");
				String saving = rs.getString("saving");
				existingFinance = new Finance(id, iduser, income, saving);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Step 5: Set existingUser to request and serve up the userEdit form
		request.setAttribute("finance", existingFinance);
		request.getRequestDispatcher("/FinanceEdit.jsp").forward(request, response);
	}

	// method to update the user table base on the form data
	private void updateFinance(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		// Step 1: Retrieve value from the request

		String id = request.getParameter("id");
		String iduser = request.getParameter("iduser");
		String income = request.getParameter("income");
		String saving = request.getParameter("saving");

		// Step 2: Attempt connection with database and execute update user SQL query
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_FINANCE_SQL);) {
			statement.setString(1, id);
			statement.setString(2, iduser);
			statement.setString(3, income);
			statement.setString(4, saving);
			statement.setString(5, id);

			int i = statement.executeUpdate();
		}
		// Step 3: redirect back to UserServlet (note: remember to change the url to
		// your project name)
		response.sendRedirect("http://localhost:8090/FinanceTrackerWebsite/ReturnFinanceServlet/dashboard");
	}

	// method to delete user
	private void deleteFinance(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		// Step 1: Retrieve value from the request
		String id = request.getParameter("id");
		// Step 2: Attempt connection with database and execute delete user SQL query
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_FINANCE_SQL);) {
			statement.setString(1, id);
			int i = statement.executeUpdate();
		}
		// Step 3: redirect back to UserServlet dashboard (note: remember to change the
		// url to your project name)
		response.sendRedirect("http://localhost:8090/FinanceTrackerWebsite/ReturnFinanceServlet/dashboard");
	}
	
	private void deleteexpenditure(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		// Step 1: Retrieve value from the request
		String id = request.getParameter("id");
		// Step 2: Attempt connection with database and execute delete user SQL query
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_EXPENDITURE_SQL);) {
			statement.setString(1, id);
			int i = statement.executeUpdate();
		}
		// Step 3: redirect back to UserServlet dashboard (note: remember to change the
		// url to your project name)
		response.sendRedirect("http://localhost:8090/FinanceTrackerWebsite/ReturnFinanceServlet/dashboard");
	}
	private void showExpenditureEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		// get parameter passed in the URL
		String id = request.getParameter("id");
		ListExpenditure existingExpenditure = new ListExpenditure("", "", "", "", "");
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EXPENDITURE_BY_ID);) {
			preparedStatement.setString(1, id);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();
			// Step 4: Process the ResultSet object
			while (rs.next()) {
				id = rs.getString("id");
				String idfinance = rs.getString("idfinance");
				String type = rs.getString("type");
				String amount = rs.getString("amount");
				String date = rs.getString("date");
				existingExpenditure = new ListExpenditure(id, idfinance, type, amount, date);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Step 5: Set existingUser to request and serve up the userEdit form
		request.setAttribute("expenditure", existingExpenditure);
		request.getRequestDispatcher("/ExpenditureEdit.jsp").forward(request, response);
	}
	
	private void updateExpenditure(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		// Step 1: Retrieve value from the request

		String id = request.getParameter("id");
		String idfinance = request.getParameter("idfinance");
		String type = request.getParameter("type");
		String amount = request.getParameter("amount");
		String date = request.getParameter("date");
		

		// Step 2: Attempt connection with database and execute update user SQL query
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_EXPENDITURE_SQL);) {
			statement.setString(1, id);
			statement.setString(2, idfinance);
			statement.setString(3, type);
			statement.setString(4, amount);
			statement.setString(5, date);
			statement.setString(6, id);

			int i = statement.executeUpdate();
		}
		// Step 3: redirect back to UserServlet (note: remember to change the url to
		// your project name)
		response.sendRedirect("http://localhost:8090/FinanceTrackerWebsite/ReturnFinanceServlet/dashboard");
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
