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

    public int updateSeat(Server s, String first, String last, String title, String time, int seatNum){
        ResultSet rs;
        int success;
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
            success = ps.executeUpdate();
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
	public ResultSet checkAvailableMovies(Server s){
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
	public ResultSet getMovieTitles(Server s){
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
	public ResultSet getShowtimes(Server s, String name){
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
	public ResultSet getSeats(Server server, String name, String time){
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
	public ResultSet getUsers(Server s){
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
    public ResultSet getUserIds(Server s){
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
	public ResultSet getUserInfo(Server server, String first, String last){
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
    public ResultSet getFilmIds(Server s){
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
    public ResultSet getFunds(Server s, String first, String last){
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
    public ResultSet getPoints(Server s, String first, String last){
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
    public int updatePoints(Server s, String first, String last, int points){
        PreparedStatement ps;
        int rowAffected;
        try{
        ps = s.prepareStatement("UPDATE user SET Reward_Points =? WHERE First_Name =? AND Last_Name=?");
        ps.setInt(1, points);
        ps.setString(2, first);
        ps.setString(3, last);
        rowAffected = ps.executeUpdate();

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
    public int incrementPoints(Server s, String first, String last){
        PreparedStatement ps, ps2;
        ResultSet rs;
        int rowAffected;
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
        rowAffected = ps2.executeUpdate();

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
    public int subtractFunds(Server s, String first, String last) {
        PreparedStatement ps, ps2;
        ResultSet rs;
        int rowAffected;
        double funds;
        try {
            ps = s.prepareStatement("SELECT Funds from user WHERE First_Name=? AND Last_Name=?");
            ps.setString(1, first);
            ps.setString(2, last);
            rs = ps.executeQuery();            
            while (rs.next()) {
                funds = rs.getDouble(1) - 5;
            }

            ps2 = s.prepareStatement("UPDATE user SET Funds =? WHERE First_Name =? AND Last_Name=?");
            ps2.setDouble(1, funds);
            ps2.setString(2, first);
            ps2.setString(3, last);
            rowAffected = ps2.executeUpdate();

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
    public int insertMovie(Server s, int id, String title, String rating, int runtime){
        Statement stmt;
        int success;
        try{
            String sql = "INSERT INTO film VALUES (" + Integer.toString(id) + ", " + title + ", " + rating + ", " + Integer.toString(runtime) + ")";
            System.out.println("SQL statement to be inserted: " + sql);
            stmt = s.createStatement();
            success = stmt.executeUpdate(sql);
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
    public int insertUser(Server s, int id, String first, String last, Date bday, double funds, int points){
        Statement stmt;
        int success;
        try{
            String sql = "INSERT INTO user VALUES (" + Integer.toString(id) + ", " + first + ", " + last + ", " + Date.toString(bday) +  ", " + Double.toString(funds) + ", " + Integer.toString(points) + ")";
            System.out.println("SQL statement to be inserted: " + sql);
            stmt = s.createStatement();
            success = stmt.executeUpdate(sql);
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
    public int deleteMovie(Server s, String title){
        PreparedStatement ps;
        int success;
        try{
            ps = s.prepareStatement("DELETE FROM film WHERE Title =?");
            ps.setString(1, title);
            success = ps.executeUpdate();
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
    public int deleteUser(Server s, String first, String last){
        PreparedStatement ps;
        int success;
        try{
            ps = s.prepareStatement("DELETE FROM user WHERE First_Name =? AND Last_Name =?");
            ps.setString(1, first);
            ps.setString(2, last);
            success = ps.executeUpdate();
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
    //Cancels ticket based on username and title being seen
    public int cancelTicket(Server s, String first, String last, String title){
        PreparedStatement ps, pstmt;
        ResultSet rs;
        int success;
        try{
            pstmt = s.createStatement("SELECT User_ID from user WHERE First_Name=? AND Last_Name=?");
            pstmt.setString(1, first);
            pstmt.setString(2, last);
            rs = pstmt.executeQuery();
            int userID;

            ps = s.prepareStatement("UPDATE seat_showtime SET User_ID = NULL JOIN film_showtime ON seat_showtime.Showtime_Id = film_showtime.Showtime_Id WHERE User_ID =? AND Title=?");
            ps.setInt(1, userID);
            ps.setString(2, title);
            success = ps.executeUpdate();
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

    //Returns the user's current movies that they have tickets for.
    public ResultSet getUserMovies(Server s, String first, String last){
        PreparedStatement ps, pstmt;
        ResultSet rs, rs2;
        int userID;
        try{
            pstmt = s.createStatement("SELECT User_ID from user WHERE First_Name=? AND Last_Name=?");
            pstmt.setString(1, first);
            pstmt.setString(2, last);
            rs = pstmt.executeQuery();
            userID = rs.getInt("User_ID");
            
            ps = s.prepareStatement("SELECT f.Title FROM Film f, Seat_Showtime ss, Film_Showtime fs WHERE fs.Film_ID = f.Film_ID and fs.Showtime_ID = ss.Showtime_ID and ss.user_ID = ?");
            ps.setInt(1, userID);
            rs2 = ps.executeQuery();
            
            return rs2;

        }catch(SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when getting a user's movie tickets.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("Error occurred when getting a user's movie tickets.");
        }
        finally
        {            
            try
            {
                if(rs != null){
                    rs.close();
                }
                if(rs2!= null)
                {
                    rs2.close();
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
    }
}