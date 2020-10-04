package Ser322;

import java.io.*;
import java.util.*;
import java.sql.*;


/* SER322 Fall 2020 Session A
 *   @Author: David Aldridge, Curtis Kovacs, Christopher Lopez, David Lacombe
 *   @Version :1.000000000
 *
 * This project was to use the JDBC to communicate with a SQL server that contains the Movie theater database. The input arguments from the command line
 * take in the Server URL, username and password to access the server. Then the main method will create a command line user interface and that will be where
 * the user makes the choices. The choices will call predetermined SQL queries from the database and allow the user to do a few things.
 */
public class MovieTheater
{
    private static Connection server;
    private static Scanner input = System.in;;
    public MovieTheater()
    {

    }

    public static void main(String[] args) 
    {
        //Does a check based on the length of the args to determine if the server will connect before it attempts.
        if (args.length != 4)
        {
            System.out.println("USAGE: java ser322.MovieTheater <url> <user> <pwd> <driver> ");
            System.exit(0);
        }
        else
        {
            try 
            {
                server = DriverManager.getConnection(args[0], args[1], args[2]);
                System.out.println("Connected......");
                mainMenu(server);
            }
            catch(SQLException sqlexc)
            {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see above error to help solve.");
            }
            catch (Exception exc)
            {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see above error to help solve.");
            }
            finally 
            {
                try 
                {                  
                    if (server != null)
                    {server.close();}
                }
                catch (SQLException se) 
                {
                    se.printStackTrace();
                }
            }
        }
    }
    private static void mainMenu(Connection server)
    {
        try
        {
            Integer selection = -1;
            String mainMen = "Hello,\n Please select from the following options: \n\n 1. Check available Movies.\n ";
            mainMen = mainMen + "2. Buy Ticket. \n 3. Check showtime for certain movie.\n 4. Check reward points. ";
            mainMen = mainMen +  "5. Add movie.\n 6. Delete Movie. \n 7.Update Showtime of movie.\n 8. Get all Rewards user Names.\n";
            while(selection > 0 && selection < 9)
            {
                System.out.println(mainMen);
                selection = input.nextInt();
                if (selection < 0 || selection > 9)                
                {
                    System.out.println("Please make a valid selection from 1-7.\n Thank you.");
                }
                else
                {
                    switch (selection)
                    {
                    case 1:
                    {
                        // Calls the method that will search the database for all movies and info about them.
                        // Passes connection so the new method will be able to keep using it.
                        checkAvailableMovie(server);
                        break;
                        // POSSIBLE TO SET SElection to -1 after complete or after switch?
                    }
                    case 2:
                    {
                        //Will be calling a second method to buy ticket. Which will pass connection, movie_title (as string), seat (as string), showtime (as string)?? maybe auditorium (as int) too?
                        buyTicket(server);
                        break;
                    }
                    case 3:
                    {
                        // Will show all showtimes of a user entered movie title. If error then message will display and allow user to enter again or give up.
                        //Passes connection and movie_title as string.
                        break;
                    }
                    case 4:
                    {
                        // Will pass connection user's_name(they enter a name? Should we have a method to get user names?), and birthday as string
                        break;
                    }
                    case 5:
                    {
                        break;
                    }
                    case 6:
                    {
                        break;
                    }
                    case 7:
                    {
                        break;
                    }
                    default:
                    {
                        System.out.println("SOMETHING BAD HAPPENED TO GET HERE....");
                        break;
                    }

                    }
                }
            }
            

            System.out.println(mainMen);
        }
        catch(SQLException sqlexc)
        {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see above error to help solve.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see above error to help solve.");
        }
        finally 
        {
            try 
            {                  
                if (server != null)
                {server.close();}
            }
            catch (SQLException se) 
            {
                se.printStackTrace();
            }
        }
    }
    private static void buyTicket(Connection server)
    {
        String userInput, movieChoice, showtimeChoice, seatChoice;
        ResultSet results;
        boolean validMovie = false, validTime = false, validSeat =false;
        try
        {              
            //CALLS QUERY TO GET ALL VALID MOVIES AND PASS THEM AS RESULT SET.
            //Results = getMoviesAsResultSet(Server);
            
            while (validMovie != true)
            {  
                System.out.println("Please enter the name of the movie you wish to buy a ticket for:\n (If you need to see available movies type \"Movies\".\n");
                userInput = input.nextLine();
                if (userInput.toLowerCase().equals("movies"))
                {
                    System.out.println("Current Movies:");
                    while (results.next())
                    {
                        System.out.println(results.getString(1));
                    }
                    continue;
                }
                else
                { 
                    while (results.next())
                    {
                        if(results.getString(1).toLowerCase().equals(userInput.toLowerCase()))
                        {
                            validMovie = true;
                            movieChoice=userInput;
                            break;
                        }
                    }
                    if (validMovie == false)
                    {
                        System.out.println("That was not a valid selection.\nPlease select from this list of movies.\n");
                        while(results.next())
                        {
                            System.out.println(results.getString(1));
                        }
                        continue;
                    }
                }
            }
            //results = Query(server,movieChoice); //Gets the showtimes for the currently selected movie
            while (validTime != true)  
            {
                System.out.printf("Which Showtime would you like to see %s? \n Please enter the time:(Example\"12:20\"\n For a list of times type \"Times\"\n",movieChoice);
                userInput = input.nextLine();
                if (userInputtoLowerCase().equals("times"))
                {
                    System.out.printf("Showtimes for %s:\n",movieChoice);
                    while(results.next())
                        {
                            System.out.println(results.getString(1));
                        }
                    continue;
                }
                else
                {
                    while (results.next())
                    {
                        if(results.getString(1).equals(userInput))
                        {
                            validTime = true;
                            showtimeChoice=userInput;
                            break;
                        }
                    }
                    if (validTime == false)
                    {
                        System.out.printf("That was not a valid selection.\nPlease select from the available showtimes for %s:\n",movieChoice);
                        while(results.next())
                        {
                            System.out.println(results.getString(1));
                        }
                        continue;
                    }
                }
            }
            //results = Query(server,movieChoice,showtimeChoice); //Gets the seats for the currently selected movie and showtime that have a null userid attached?
            while (validSeat != true)
            {
                System.out.printf("Which seat would you like to reserve for %s at %s? \n Please enter the seat number:\n For a list of available seats type \"Seats\"\n",movieChoice, showtimeChoice);
                userInput = input.nextint();
                if (userInputtoLowerCase().equals("seats"))
                {
                    System.out.printf("Available seats for %s at  %s:\n",movieChoice,  showtimeChoice);
                    while(results.next())
                        {
                            System.out.println(results.getString(1));
                        }
                    continue;
                }
                else
                {
                    while (results.next())
                    {
                        if(results.getInt(1) == (userInput))
                        {
                            validTime = true;
                            seatChoice = userInput;
                            break;
                        }
                    }
                    if (validTime == false)
                    {
                        System.out.printf("That was not a valid selection.\nPlease select from the available seats for %s at %s:\n",movieChoice, showtimeChoice);
                        while(results.next())
                        {
                            System.out.println(results.getString(1));
                        }
                        continue;
                    }
                }
            }

        }
        catch(SQLException sqlexc)
        {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see above error to help solve.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see above error to help solve.");
        }
        finally 
        {
            try 
            {                  
                if (server != null)
                {server.close();}
            }
            catch (SQLException se) 
            {
                se.printStackTrace();
            }
        }
    }
}

/*
    private static void export(String arg, String arg1, String arg2, String arg3)
    {
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = null;
        String filename = arg3;

        String _url = arg;

        try {
            //  Connects to the SQL server then creates a statement.
            conn = DriverManager.getConnection(_url, arg1, arg2);
            stmt = conn.createStatement();

            // query for sql to get the results
            rs = stmt.executeQuery("Show Tables");
            while (rs.next())
            {
                System.out.println(rs.getString(1));
            }




        }
        catch (SQLException sqlexc)
        {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see above error to help solve.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see above error to help solve.");
        }
        // closes database resources
        finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            }
            catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    //*****************************************************************************************************************
    // BELOW HERE IS REFERENCE CODE FROM ASSIGN 5
/*
    //The query1 method runs a query on the server that shows all customers id numbers, name and total spent.
      //It is the answer to part 1 of the assignment. And is a very simple query that the user has no adjustments to.
     
     
    private static void query1(String arg, String arg1, String arg2)
    {
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = null;

        String _url = arg;
        try {
            //  Connects to the SQL server then creates a statement.
            conn = DriverManager.getConnection(_url, arg1, arg2);
            stmt = conn.createStatement();

            // query for sql to get the results
            rs = stmt.executeQuery("Select emp.EMPNO, emp.ENAME, dept.DNAME from emp, dept where emp.DEPTNO = dept.DEPTNO");

            // Display the results in a Pretty way
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.print(rsmd.getColumnName(1) + "\t");
            System.out.print(rsmd.getColumnName(2)+ "\t");
            System.out.println(rsmd.getColumnName(3)+ "\t"+ "\n");

            while (rs.next()) {
                System.out.print(rs.getInt(1) + "\t");
                System.out.print(rs.getString(2) + "\t ");
                System.out.print(rs.getString(3) + "\t ");
                System.out.println("");
            }
        }
        catch (SQLException sqlexc)
        {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see above error to help solve.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see above error to help solve.");
        }
        // closes database resources
        finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            }
            catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
     Takes in the args from the command line and it includes the dept no that is being searched for. Returns the dept name
     * name of customer and the total amount spent rounded so it is easier to use.
     
    private static void query2(String arg, String arg1, String arg2, String arg3)
    {
        ResultSet rs = null;
        //Statement stmt = null; NOT USED here
        Connection conn = null;
        PreparedStatement pstmt = null;

        String _url = arg;
        try {
            //  Connects to the SQL server
            conn = DriverManager.getConnection(_url, arg1, arg2);

            // Step 3: Create a statement
            pstmt = conn.prepareStatement

                    ("Select dept.DNAME, customer.NAME, Round(customer.QUANTITY * product.PRICE)\"Total Spent\", " +
                            "dept.DEPTNO from dept, customer, product where customer.PID = product.PRODID and" +
                            " product.MADE_BY = dept.DEPTNO and dept.DEPTNO = ?");
            pstmt.setString(1, arg3);
            rs = pstmt.executeQuery();


            // Display the results "Pretty?"
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.print(rsmd.getColumnName(1) + "\t");
            System.out.print(rsmd.getColumnName(2)+ "\t" + "\t");
            System.out.println(rsmd.getColumnName(3)+ "\t"+ "\n");

            while (rs.next()) {
                System.out.print(rs.getString(1) + "\t");
                System.out.print(rs.getString(2) + "\t ");
                System.out.print(rs.getInt(3) + "\t ");
                System.out.println("");
            }
        }
        catch (SQLException sqlexc2)
        {
            sqlexc2.printStackTrace();
            System.out.println("A SQL error occurred. Please see above error to help solve.");
        }
        catch (Exception exc2)
        {
            exc2.printStackTrace();
            System.out.println("An error occurred. Please see above error to help solve.");
        }
        // closes database resources
        finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            }
            catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private static void dml1(String arg, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6,String arg7)
    {
        ResultSet rs = null;
        //Statement stmt = null;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            String _url = arg;
            conn = DriverManager.getConnection(_url, arg1, arg2);
            // if autocommit is true then a transaction will be executed
            // on each DDL or DML statement immediately, usually you want
            // to set to false to batch within a single transaction.
            conn.setAutoCommit(false);

            // Step 2.1 - get the DB MetaData
            DatabaseMetaData dbmd = (DatabaseMetaData) conn.getMetaData();

            // get the table named customer if it exists
            rs = dbmd.getTables(null, null, "customer", null);
            if (rs.next())
            {

            }
            if (rs != null) rs.close();

            ps = conn.prepareStatement("INSERT INTO customer (CUSTID, PID, NAME, QUANTITY) Values (?,?,?,?)");
            ps.setString(1, arg3);
            ps.setString(2, arg4);
            ps.setString(3, (arg5 + " " + arg6));
            ps.setString(4, arg7);

            if (ps.executeUpdate() > 0) {
                System.out.println("SUCCESS!");
            }
            ps.close();

            // Have to do this to write changes to a DB
            conn.commit();
        }
        catch (SQLException sqlexc3)
        {
            sqlexc3.printStackTrace();
            System.out.println("A SQL error occured. Please see above error to help solve.");
        }
        catch (Exception exc3)
        {
            exc3.printStackTrace();
            System.out.println("An error occured. Please see above error to help solve.");
        }
        finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                {
                    conn.close();
                }
            }
            catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
    }

   Build XML DOcument from database. The XML object is returned to main method where it is written to flat file.
    private static Document buildEmployeeXML(ResultSet rset, String arg, String arg1, String arg2, String arg3) throws Exception
    {
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = null;
        String TableName;
        Document xmlDoc = new DocumentImpl();
        String _url = arg;
         Creating the root element
        //replace employeetable with countries to set a countries tag
        Element rootElement = xmlDoc.createElement("JdbcLab");
        xmlDoc.appendChild(rootElement);

        while(rset.next())
        {
            TableName = rset.getString(1);
            Element dataNode = xmlDoc.createElement(TableName);
            dataNode.setAttribute(TableName, "");
            try {
                //  Connects to the SQL server then creates a statement.
                conn = DriverManager.getConnection(_url, arg1, arg2);
                stmt = conn.createStatement();

                // query for sql to get the results
                rs = stmt.executeQuery("Select * from " + TableName);

                // Display the results in a Pretty way
                ResultSetMetaData rsmd = rs.getMetaData();


                System.out.print(rsmd.getColumnName(1) + "\t");
                System.out.print(rsmd.getColumnName(2)+ "\t");
                System.out.println(rsmd.getColumnName(3)+ "\t"+ "\n");

                while (rs.next()) {
                    System.out.print(rs.getInt(1) + "\t");
                    System.out.print(rs.getString(2) + "\t ");
                    System.out.print(rs.getString(3) + "\t ");
                    System.out.println("");
                }
            }
            catch (SQLException sqlexc)
            {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see above error to help solve.");
            }
            catch (Exception exc)
            {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see above error to help solve.");
            }
            // closes database resources
            finally {
                try {
                    if (rs != null)
                        rs.close();
                    if (stmt != null)
                        stmt.close();
                    if (conn != null)
                        conn.close();
                }
                catch (SQLException se) {
                    se.printStackTrace();
                }
            }


            // Creating elements within node DOM
            Element data1 = xmlDoc.createElement("data1");
            Element data2 = xmlDoc.createElement("data2");

            //Populating node DOM with Data
            data1.appendChild(xmlDoc.createTextNode(rset.getString("data1")));
            data2.appendChild(xmlDoc.createTextNode(rset.getString("data2")));

            dataNode.appendChild(data1);
            dataNode.appendChild(data2);

            // Appending emp to the Root Class
            rootElement.appendChild(dataNode);
        }
        return xmlDoc;
    }*/

    /* printDOM will write the contents of xml document passed onto it out to a file
    private static void printDOM(Document _xmlDoc, File _outputFile) throws Exception
    {
        OutputFormat outputFormat = new OutputFormat("XML","UTF-8",true);
        FileWriter fileWriter = new FileWriter(_outputFile);

        XMLSerializer xmlSerializer = new XMLSerializer(fileWriter, outputFormat);

        xmlSerializer.asDOMSerializer();

        xmlSerializer.serialize(_xmlDoc.getDocumentElement());
    }
    


*/

}