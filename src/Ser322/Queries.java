package ser322;

import java.sql.*;


/* SER322 Fall 2020 Session A
 *   @Author: David Aldridge, Curtis Kovacs, Christopher Lopez, David Lacombe
 *   @Version :1.000000000
 *
 * This class creates SQL queries to communicate with the mySQL database.
 */

public class Queries{



    //returns the title, runtime, and rating for all movies
	public ResultSet checkAvailableMovies(Connection s){
        Statement stmt = null;
        ResultSet rs = null;
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
	public ResultSet getUserInfo(Connection server, String first, String last){
        PreparedStatement ps = null;
        ResultSet rs = null;
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


    //get user.Funds
    public ResultSet getFunds(Connection s, String first, String last){
        PreparedStatement ps = null;
        ResultSet rs = null;
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
    public ResultSet getPoints(Connection s, String first, String last){
        PreparedStatement ps = null;
        ResultSet rs = null;
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
    public int updatePoints(Connection s, String first, String last, int points){
        PreparedStatement ps = null;
        int rowAffected = 0;
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

    //subract funds due to ticket purchase
    public int subtractFunds(Connection s, String first, String last) {
        PreparedStatement ps = null, ps2 = null;
        ResultSet rs = null;
        int rowAffected = 0;
        double funds = 0;
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
    public int insertMovie(Connection s, int id, String title, String rating, int runtime){
        Statement stmt = null;
        int success = 0;
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
    public int insertUser(Connection s, Integer id, String first, String last, String bday, Integer funds, Integer points){
        Statement stmt = null;
        int success = 0;
        try{
            String sql = "INSERT INTO user VALUES (" + Integer.toString(id) + ", " + first + ", " + last + ", " + bday +  ", " + Double.toString(funds) + ", " + Integer.toString(points) + ")";
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
    public int deleteMovie(Connection s, String title){
        PreparedStatement ps = null;
        int success = 0;
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
    public int deleteUser(Connection s, String first, String last){
        PreparedStatement ps = null;
        int success = 0;
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
    public int cancelTicket(Connection s, String first, String last, String title){
        PreparedStatement ps = null, pstmt = null;
        ResultSet rs = null;
        int success = 0;
        int userID = 0;
        try{
            pstmt = s.prepareStatement("SELECT User_ID from user WHERE First_Name=? AND Last_Name=?");
            pstmt.setString(1, first);
            pstmt.setString(2, last);
            rs = pstmt.executeQuery();

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

     //Gets user funds
     public ResultSet getFunds(Connection server, String first, String last)
     {
         PreparedStatement ps = null;
         ResultSet rs = null;
         try {
             ps = server.prepareStatement("SELECT user.funds FROM user WHERE First_Name=? AND Last_Name=?");
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
}
