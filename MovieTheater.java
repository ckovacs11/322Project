package Ser322;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.sql.Date;


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
        ResultSet returned;
        try
        {
            Integer selection = -1;
            String mainMen = "Hello,\n Please select from the following options: \n\n1. Check available Movies.\n";
            mainMen = mainMen + "2. Buy Ticket. \n3. Check showtime for certain movie.\n4. Check reward points.";
            mainMen = mainMen + "5. Add movie.\n6. Delete Movie. \n7.Update Showtime of movie.\n8. Get all Rewards user Names.\n";
            mainMen = mainMen + "9. Add user.\n10. Delete user.\n";
            while(selection > 0 && selection < 12)
            {
                System.out.println(mainMen);
                selection = input.nextInt();
                if (selection < 1 || selection > 11)                
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
<<<<<<< HEAD
                        Statement stmt = server.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT title, rating, runtime, time FROM film_showtime JOIN film ON film.Film_ID = film_showtime.Film_ID JOIN showtime ON film_showtime.Showtime_ID = showtime.Showtime_ID");
=======
                        returned = checkAvailableMovies(server);
>>>>>>> Add first Queries method
                        System.out.println("Here are the currently showing movies:");
                        while (rs.next())
                        {
                            System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "+ rx.getString(4));
                        }
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
                        //Insert Movie into theater. Should it also add showtimes?
                        break;
                    }
                    case 6:
                        // Deletes a movie from theater. (Does it need to delete showtimes too?)
                    {
                        break;
                    }
                    case 7:
                    {
                        //Update a showtime of a movie. 
                        break;
                    }  
                    case 8:
                    {
                        //Returns all users.
                        break;
                    }
                    case 9:
                    {
                        //Adds a user.
                        break;
                    }
                    case 10:
                    {
                        //Deletes a user.
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
        String userInput, movieChoice, showtimeChoice, seatChoice, userFirst,userLast;
        ResultSet results;
        boolean validMovie = false, validTime = false, validSeat =false, validUser = false, validFunds = false ;
        try
        {              
            //CALLS QUERY TO GET ALL VALID MOVIES AND PASS THEM AS RESULT SET.
            //Results = getMoviesAsResultSet(Server);
            
            while (validMovie != true)
            {  
                Statement stmt = server.createStatement();
                results = stmt.executeQuery("SELECT title FROM film");

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
                    validMovie = checkMovie(server,userInput);
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
                PreparedStatement ps = server.prepareStatement("SELECT time FROM film_showtime JOIN film ON film.Film_ID = film_showtime.Film_ID JOIN showtime ON film_showtime.Showtime_ID = showtime.Showtime_ID WHERE title =?");
                ps.setString(1, movieChoice);
                results = ps.executeQuery();

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
                PreparedStatement ps = server.prepareStatement("SELECT * FROM seat_showtime, film_showtime JOIN film ON film.Film_ID = film_showtime.Film_ID JOIN showtime ON showtime.Showtime_ID = film_showtime.Showtime_ID WHERE time =? AND title =? AND User_ID = null");
                ps.setString(1, showtimeChoice);
                ps.setString(2, movieChoice);
                results = stmt.executeQuery("SELECT * FROM user");

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
            while(validUser !=true)
            {
                //results = Query(server); //Gets all users in the database to confirm that they can buy a ticket.
                Statement stmt = server.createStatement();
                results = stmt.executeQuery("SELECT * FROM user");

                System.out.printf("To purchase this ticket for %s at %s in seat:%s\nPlease enter your full name. \nNOTE: you must already be registered to purchase a ticket. If you need to check if you are registered then type \"Users\"");
                userInput= input.nextLine();
                if(userInput.toLowerCase().equals("users"))
                {
                    System.out.println("List of Current users:\n");
                    while (results.next())
                    {
                        System.out.print(results.getString(1));
                        System.out.println(" " + results.getString(2));
                    }
                }
                else
                {
                    while (results.next())
                    {
                        if(userInput.toLowerCase().equals(results.getString(1).toLowerCase() +" " + results.getString(2).toLowerCase()))
                        {
                            validUser = true;
                            userFirst = results.getString(1);
                            userLast = results.getString(2);
                            break;
                        }
                    }
                    if(validUser == false)
                    {
                        System.out.println("That user's name was not found.\n Please check the list of users below.\nIf we have an error in your name please contact your system Admin...\n");
                        System.out.println("List of Current users:\n");
                        while (results.next())
                        {
                            System.out.print(results.getString(1));
                            System.out.println(" " + results.getString(2));
                        }
                        continue;
                    }
                }
                
            }
            results = query(server,userFirst,userLast); //Return the funds, rewards points and birthday of user
            Date today = Date; //**********************CHECK THIS!!! Probably Wrong */
            while (validFunds != true)
            {
                if (today = results.getString(3))
                {
                    System.out.println("Happy Birthday!\nEnjoy your free movie on us!\nTell your friends!");                    
                }
                else if(results.getInt(1) > 9)
                {
                    System.out.printf("Using rewards points for 1 ticket for %s\nAt:%s\nSeat:%s\n",movieChoice,showtimeChoice,seatChoice);
                    //QUERY to UPDATE REWARDS POINTS FOR (server,userFirst,UserLast,newpoint value)
                }
                else if(results.getInt(1) > 9)
                {
                    System.out.printf("Purchasing 1 ticket for %s\nAt:%s\nSeat:%s\n",movieChoice,showtimeChoice,seatChoice);
                    //QUERY UPDATE USER FUNDS -10 (server,userFirst,userLast,newFunds amount)
                }
                else 
                {
                    System.out.println("You do not have enough funds in your account.\n Please vist the ticket window or atm to add funds.\n Thank you.");
                    break;
                }
                
                updateSeat(server,userFirst,userLast,movieChoice,showtimeChoice,seatChoice);
                System.out.println("Enjoy your movie, and dont forget the popcorn!");
            }

        }
        catch(SQLException sqlexc)
        {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        }
        /* Since we have the try catch finally in the main method that calls this one, the finally would be called from there?
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
        }*/
    }

    public static boolean checkMovie(Connection server, String Movie)
    {
        ResultSet results;
        try
        {
            //Results = getMoviesAsResultSet(Server);
            while (results.next())
            {
                if(results.getString(1).toLowerCase().equals(userInput.toLowerCase()))
                {
                    return true;
                    break;
                }
            }
            return false;
        }
        catch(SQLException sqlexc)
        {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        }
        /*
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
        }*/
    }
}
//*****************************************************************************************************************
    // BELOW HERE IS REFERENCE CODE FROM ASSIGN 5
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