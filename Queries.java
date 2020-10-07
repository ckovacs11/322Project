package Ser322;

import java.io.*;
import java.util.*;
import java.sql.*;

/* SER322 Fall 2020 Session A
 *   @Author: David Aldridge, Curtis Kovacs, Christopher Lopez, David Lacombe
 *   @Version :1.000000000
 *
 * This class creates SQL queries to communicate with the mySQL database.
 */

public class Queries {

    private int updateSeat(Server s, String first, String last, String title, String time, int seatNum){
        ResultSet rs;
        try{
            PreparedStatement pstmt = s.createStatement("SELECT User_ID from user WHERE First_Name=? AND Last_Name=?");
            pstmt.setString(1, first);
            pstmt.setString(2, last);
            rs = pstmt.executeQuery();
            int userID;
            while(rs.next()){
                userID = rs.getInt(1);
            }

            PreparedStatement ps = s.createStatement("UPDATE seat_showtime SET User_ID = ? JOIN showtime ON seat_showtime.Showtime_ID = showtime.Showtime_ID JOIN film_showtime ON film_showtime.Showtime_ID = showtime.Showtime_ID WHERE title =? AND time=? AND Seat_ID =?");
            ps.setInt(1, userID);
            ps.setString(2, title);
            ps.setString(3, time);
            ps.setInt(4, seatNum);
            int success = ps.executeUpdate();
            if(success > 0){
                System.out.println("Seat successfully reserved");
            } else {
                System.out.println("Error in reserving seat");
            }
        }catch(SQLException se){
            se.printStackTrace();
            System.out.println("Error occurred when updating Seat.");
        }        
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when updating Seat.");
        }
        finally
        {
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(pstmt!=null){
                    pstmt.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return success;
    }

    //returns the title, runtime, and rating for all movies
	private ResultSet checkAvailableMovies(Server s){
        Statement stmt;
        ResultSet rs;
        try{
		stmt = s.createStatement();
		rs = stmt.executeQuery("SELECT Title, Rating, Runtime FROM film");

       }catch(SQLException se){
            se.printStackTrace();
            System.out.println("Error occurred when checking movies.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when checking movies.");
        }
        finally
        {
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(stmt!=null){
                    stmt.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
		return rs;
	}

    //returns all movie titles
	private ResultSet getMovieTitles(Server s){
        Statement stmt;
        ResultSet rs;
        try{
		stmt = s.createStatement();
        rs = stmt.executeQuery("SELECT Title FROM film");
        
       }catch(SQLException se){
            se.printStackTrace();
            System.out.println("Error occurred when getting movie titles.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when getting movie titles.");
        }
        finally
        {
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(stmt!=null){
                    stmt.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
		return rs;
	}

    //returns the showtimes for the given movie
	private ResultSet getShowtimes(Server s, String name){
        PreparedStatement ps;
        ResultSet rs;
        try{
		ps = s.prepareStatement("SELECT Time FROM film_showtime JOIN film ON film.Film_ID = film_showtime.Film_ID JOIN showtime ON film_showtime.Showtime_ID = showtime.Showtime_ID WHERE title =?");
		ps.setString(1, name);
		rs = ps.executeQuery();

       }catch(SQLException se){
            se.printStackTrace();
            System.out.println("Error occurred when getting showtimes.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when getting showtimes.");
        }
        finally
        {
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
		return rs;
	}

    //returns the seat IDs and Auditorium number
	private ResultSet getSeats(Server server, String name, String time){
        PreparedStatement ps;
        ResultSet rs;
        try{
		ps = server.prepareStatement("SELECT * FROM seat_showtime, film_showtime JOIN film ON film.Film_ID = film_showtime.Film_ID JOIN showtime ON showtime.Showtime_ID = film_showtime.Showtime_IDWHERE Time =? AND Title =? AND User_ID = null");
		ps.setString(1, time);
		ps.setString(2, name);
		rs = ps.executeQuery();

        }catch(SQLException se){
            se.printStackTrace();
            System.out.println("Error occurred when getting seats.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when getting seats.");
        }
        finally
        {
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(pstmt!=null){
                    pstmt.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
		 return rs;

	}

    //get all users
	private ResultSet getUsers(Server s){
        Statement stmt;
        ResultSet rs;
        try {
            stmt = s.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user");

        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when checking movies.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when checking movies.");
        }
        finally
        {
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(stmt!=null){
                    stmt.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return rs;

	}

    //get user ids
    private ResultSet getUserIds(Server s){
        Statement stmt;
        ResultSet rs;
        try {
            stmt = s.createStatement();
            rs = stmt.executeQuery("SELECT User_ID FROM user");

        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when getting user Ids.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when getting user Ids.");
        }
        finally
        {
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(stmt!=null){
                    stmt.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return rs;
    }

    //get specific user info
	private ResultSet getUserInfo(Server server, String first, String last){
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = server.prepareStatement("SELECT * FROM user WHERE First_Name=? AND Last_Name=?");
            ps.setString(1, first);
            ps.setString(2, last);
            rs = ps.executeQuery();

        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when getting seats.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when getting seats.");
        }
        finally
        {
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return rs;
	}

    //get all film ids
    private ResultSet getFilmIds(Server s){
        Statement stmt;
        ResultSet rs;
        try {
            stmt = s.createStatement();
            rs = stmt.executeQuery("SELECT Film_ID FROM film");

        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when getting user Ids.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when getting user Ids.");
        }
        finally
        {
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(pstmt!=null){
                    stmt.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return rs;
    }

    //get user.Funds
    private ResultSet getFunds(Server s, String first, String last){
        PreparedStatement ps;
        ResultSet rs;
        try{
            ps = s.prepareStatement("SELECT Funds from user WHERE First_Name=? AND Last_Name=?");
            ps.setString(1, first);
            ps.setString(2, last);
            rs = ps.executeQuery();
       
        }catch(SQLException se){
            se.printStackTrace();
            System.out.println("Error occurred when getting funds.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when getting funds.");
        }
        finally
        {
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return rs;
    }

    //get user.Reward_Points
    private ResultSet getPoints(Server s, String first, String last){
        PreparedStatement ps;
        ResultSet rs;
        try{
            ps = s.prepareStatement("SELECT Reward_Points from user WHERE First_Name=? AND Last_Name=?");
            ps.setString(1, first);
            ps.setString(2, last);
            rs = ps.executeQuery();
 
        }catch(SQLException se){
            se.printStackTrace();
            System.out.println("Error occurred when getting reward points.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when getting reward points.");
        }
        finally
        {
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return rs;
    }

    //returns 1 if the method successfully updates the Reward Points.
    private int updatePoints(Server s, String first, String last, int points){
        PreparedStatement ps;
        try{
        ps = s.prepareStatement("UPDATE user SET Reward_Points =? WHERE First_Name =? AND Last_Name=?");
        ps.setInt(1, points);
        ps.setString(2, first);
        ps.setString(3, last);
        int rowAffected = ps.executeUpdate();

       }catch(SQLException se){
            se.printStackTrace();
            System.out.println("Error occurred when updating reward points.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when updating reward points.");
        }
        finally
        {
            try
            {
                if(ps!=null){
                    ps.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
       
        return rowAffected;

    }

    //returns 1 if the user's Reward Points have been successfully incremented
    private int incrementPoints(Server s, String first, String last){
        PreparedStatement ps, ps2;
        ResultSet rs;

        try{
        ps = s.prepareStatement("SELECT Reward_Points from user WHERE First_Name=? AND Last_Name=?");
        ps.setString(1, first);
        ps.setString(2, last);
        rs = ps.executeQuery();
        int new_points;
        while (rs.next())
            {
                new_points = rs.getInt(1) + 1;
            }



        ps2 = s.prepareStatement("UPDATE user SET Reward_Points =? WHERE First_Name =? AND Last_Name=?");
        ps2.setInt(1, new_points);
        ps2.setString(2, first);
        ps2.setString(3, last);
        int rowAffected = ps2.executeUpdate();

        //close resources
        if(ps != null){
            ps.close();
        }
        if(ps2 != null){
            ps2.close();
        }

        }catch(SQLException se){
            se.printStackTrace();
            System.out.println("Error occurred when incrementing reward points.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when incrementing reward points.");
        }
        finally
        {
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(ps2 != null){
                    ps2.close();
                }
                if(ps != null){
                    ps.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return rowAffected;

    }

    //subract funds due to ticket purchase
    private int subtractFunds(Server s, String first, String last) {
        PreparedStatement ps, ps2;
        ResultSet rs;

        try {
            ps = s.prepareStatement("SELECT Funds from user WHERE First_Name=? AND Last_Name=?");
            ps.setString(1, first);
            ps.setString(2, last);
            rs = ps.executeQuery();
            double funds;
            while (rs.next()) {
                funds = rs.getDouble(1) - 5;
            }



            ps2 = s.prepareStatement("UPDATE user SET Funds =? WHERE First_Name =? AND Last_Name=?");
            ps2.setDouble(1, funds);
            ps2.setString(2, first);
            ps2.setString(3, last);
            int rowAffected = ps2.executeUpdate();

        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when subtracting funds.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when subtracting funds.");
        }
        finally
        {
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(ps2!=null){
                    ps2.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return rowAffected;

    }

    //insert film
    private int insertMovie(Server s, int id, String title, String rating, int runtime){
        Statement stmt;
        try{
            String sql = "INSERT INTO film VALUES (" + Integer.toString(id) + ", " + title + ", " + rating + ", " + Integer.toString(runtime) + ")";
            System.out.println("SQL statement to be inserted: " + sql);
            stmt = s.createStatement();
            int success = stmt.executeUpdate(sql);
            if(success > 0){
                System.out.println("Movie successfully added");
            } else{
                System.out.println("Movie not added. Check SQL statement.");
            }

        }catch(SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when inserting movie.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when inserting movie.");
        }
        finally
        {
            try
            {
                if(stmt!=null){
                    stmt.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return success;
    }

    //insert user
    private int insertUser(Server s, int id, String first, String last, Date bday, double funds, int points){
        Statement stmt;
        try{
            String sql = "INSERT INTO user VALUES (" + Integer.toString(id) + ", " + first + ", " + last + ", " + Date.toString(bday) +  ", " + Double.toString(funds) + ", " + Integer.toString(points) + ")";
            System.out.println("SQL statement to be inserted: " + sql);
            stmt = s.createStatement();
            int success = stmt.executeUpdate(sql);
            if(success > 0){
                System.out.println("User successfully added");
            } else{
                System.out.println("User not added. Check SQL statement.");
            }

        }catch(SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when inserting user.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when inserting user.");
        }
        finally
        {
            try
            {
                if(stmt!=null){
                    stmt.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return success;
    }

     


    //delete film based on name
    private int deleteMovie(Server s, String title){
        PreparedStatement ps;

        try{
            ps = s.prepareStatement("DELETE FROM film WHERE Title =?");
            ps.setString(1, title);
            int success = ps.executeUpdate();
            if(success > 0){
                System.out.println("Movie successfully deleted");
            } else{
                System.out.println("Movie not deleted. Check SQL statement.");
            }

        }catch(SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when deleting movie.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when deleting movie.");
        }
        finally
        {
            try
            {
                if(ps!=null){
                    ps.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return success;

    }

    //delete user based on name
    private int deleteUser(Server s, String first, String last){
        PreparedStatement ps;
        try{
            ps = s.prepareStatement("DELETE FROM user WHERE First_Name =? AND Last_Name =?");
            ps.setString(1, first);
            ps.setString(2, last);
            int success = ps.executeUpdate();
            if(success > 0){
                System.out.println("MoUser successfully deleted");
            } else{
                System.out.println("User not deleted. Check SQL statement.");
            }

        }catch(SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when deleting user.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when deleting user.");
        }
        finally
        {
            try
            {
                if(ps!=null){
                    ps.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return success;

    }

    private int cancelTicket(Server s, String first, String last, String title){
        PreparedStatement ps, pstmt;
        ResultSet rs;
        try{
            pstmt = s.createStatement("SELECT User_ID from user WHERE First_Name=? AND Last_Name=?");
            pstmt.setString(1, first);
            pstmt.setString(2, last);
            rs = pstmt.executeQuery();
            int userID;

            ps = s.prepareStatement("UPDATE seat_showtime SET User_ID = NULL JOIN film_showtime ON seat_showtime.Showtime_Id = film_showtime.Showtime_Id WHERE User_ID =? AND Title=?");
            ps.setInt(1, userID);
            ps.setString(2, title);
            int success = ps.executeUpdate();
            if(success > 0){
                System.out.println("Ticket successfully cancelled");
            } else{
                System.out.println("Ticket not cancelled. Check SQL statement.");
            }

        }catch(SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when cancelling ticket.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when cancelling ticket.");
        }
        finally
        {
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(pstmt!=null){
                    pstmt.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return success;
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
