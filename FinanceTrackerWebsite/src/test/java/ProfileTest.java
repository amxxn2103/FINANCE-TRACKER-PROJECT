import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.openqa.selenium.By;
//import necessary Selenium WebDriver classes
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.Assert;
import org.testng.annotations.AfterTest;

public class ProfileTest {
	private ProfileServlet profile = new ProfileServlet();

	// declare Selenium WebDriver
	private WebDriver webDriver;
	private ResultSet generatedKeys;
	@BeforeTest
	public void beforeTest() {
		// Setting system properties of ChromeDriver
		// to amend directory path base on your local file path
		String chromeDriverDir = "C:\\Program Files (x86)\\Google\\Chrome\\chromedriver.exe";

		System.setProperty("webdriver.chrome.driver", chromeDriverDir);

		// initialize FirefoxDriver at the start of test
		webDriver = new ChromeDriver();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/financetracker", "root",
					"password");

			// Step 4: implement the sql query using prepared statement
			// (https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html)
			PreparedStatement ps = con.prepareStatement("insert into USER values(?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			// Step 5: parse in the data retrieved from the web form request into the
			// prepared statement accordingly
			ps.setString(1, null);
			ps.setString(2, "test@gmail.com");
			ps.setString(3, "password");
			// Step 6: perform the query on the database using the prepared statement
			int i = ps.executeUpdate();
			if (i > 0) {
				generatedKeys = ps.getGeneratedKeys();
				generatedKeys.next();
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/financetracker", "root",
							"password");
					// Step 4: implement the sql query using prepared statement
					// (https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html)
					PreparedStatement statement = conn.prepareStatement("insert into PROFILE values(?,?,?,?,?)");

					// Step 5: parse in the data retrieved from the web form request into the
					// prepared statement accordingly
					statement.setString(1, null);
					statement.setLong(2, generatedKeys.getLong(1));
					statement.setString(3, "testname");
					statement.setString(4, "testsurname");
					statement.setString(5, null);
					// Step 6: perform the query on the database using the prepared statement
					statement.executeUpdate();
					// Step 7: check if the query had been successfully execute, return “You are
					// successfully registered” via the response,
				} catch (Exception exception) {

				}
			}
		}

		catch (Exception exception) {

		}

	}

	@Test
	public void retrieveTest() throws SQLException {
		
		webDriver = new ChromeDriver();
		
		//check is we are able to navigate to the newly created user's profile page
		webDriver.navigate().to("http://localhost:8090/FinanceTrackerWebsite/ProfileServlet/edit?iduser=" + generatedKeys.getLong(1) );
			
	
	}
	
	@Test
	public void updateTest() throws SQLException {
		
		webDriver = new ChromeDriver();
		
		webDriver.navigate().to("http://localhost:8090/FinanceTrackerWebsite/ProfileServlet/edit?iduser=" + generatedKeys.getLong(1) );
		
		WebElement name = webDriver.findElement(By.name("name"));
		name.sendKeys("NewTestName");
		WebElement surname = webDriver.findElement(By.name("surname"));
		surname.sendKeys("NewTestSurname");
		WebElement bio = webDriver.findElement(By.name("bio"));
		bio.sendKeys("NewTestBio");
		webDriver.findElement(By.id("savebutton")).click();
	}
	
	@Test
	public void deleteTest() throws SQLException {
		
		webDriver = new ChromeDriver();
		
		webDriver.navigate().to("http://localhost:8090/FinanceTrackerWebsite/ProfileServlet/edit?iduser=" + generatedKeys.getLong(1) );
		
		
		webDriver.findElement(By.id("deletebutton")).click();
	}
	

	@AfterTest
	public void afterTest() {
		
	}

}
