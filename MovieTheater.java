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
    private static Scanner input = System.in;
    private static Queries queries;

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
                queries = new Queries();
                mainMenu(server);
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
            Integer selection = 0;
            String mainMen = "Hello,\n Please select from the following options: \n\n1. Check available Movies.\n";
            mainMen = mainMen + "2. Buy Ticket. \n3. Check showtime for certain movie.\n4. Check reward points.";
            mainMen = mainMen + "5. Add movie.\n6. Delete Movie. \n7.Update Showtime of movie.\n8. Get all Rewards user Names.\n";
            mainMen = mainMen + "9. Add user.\n10. Delete user.\n11. Exit.\n";
            while(selection != -1)
            {
                System.out.println(mainMen);
                selection = input.nextInt();
                if (selection < 1 || selection > 12)
                {
                    System.out.println("Please make a valid selection from 1-11.\n Thank you.");
                }
                else
                {
                    switch (selection)
                    {
                    case 1: //prints all current movies in the theater.
                    {
                        printMovies(server);
                        break;                        
                    }
                    case 2: //Walks the user through the process of buying a ticket.
                    {                        
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
                        // Will pass connection user's_name(they enter a name? Should we have a method to get user names?)
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
                        printUsers(server);
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
                    case 11:
                    {
                        selection = -1;
                        break;
                    }
                    default:
                    {
                        System.out.println("That is not a valid selection please choose a valid option. Also how did you get here?");
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
            System.out.println("A SQL error occurred. Please see error to help solve.");
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
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
    /*This method adds a user to the Rewards program. It will verify that the user is not already in the system.
    * the user will then be asked to add in information for the user.
    */
    public static void addUser(Conneciton server)
    {
        Sys
    }
    /*
    *This method walks the user through the process of buying a ticket. It give the user options and if the user is lost it prints out the options so the user can enter the required
    *input to move forward. A user can not buy a ticket if they do not select valid options.
    */
    private static void buyTicket(Connection server)
    {
        String userInput, movieChoice, showtimeChoice, seatChoice, userFirst,userLast;
        ResultSet results;
        boolean validMovie = false, validTime = false, validSeat =false, validUser = false, validFunds = false ;
        try
        {
            while (!validMovie)
            {
                System.out.println("Please enter the name of the movie you wish to buy a ticket for:\n(If you need to see available movies type \"Movies\".\n");
                userInput = input.nextLine();
                if (userInput.toLowerCase().equalsIgnoreCase("movies"))
                {
                    printMovies(server);
                }
                else
                {
                    validMovie = checkMovie(server,userInput);
                    if (!validMovie)
                    {
                        System.out.println("That was not a valid selection.\nPlease select from this list of movies.\n");
                        printMovies(server);
                    }
                }
            }            
            while (!validTime)
            {
                System.out.printf("Which Showtime would you like to see %s? %nPlease enter the time:(Example\"12:20\"%nFor a list of times type \"Times\"%n",movieChoice);
                userInput = input.nextLine();
                if (userInput.equalsIgnoreCase("times"))
                {
                    printShowtimes(server, movieChoice);
                }
                else
                {
                        if(checkShowtime(server, movieChoice, userInput))
                        {
                            validTime = true;
                            showtimeChoice=userInput;
                            break;
                        }
                    if (!validTime)
                    {
                        System.out.printf("That was not a valid selection.%nPlease select from the available showtimes for %s:%n",movieChoice);
                        printShowtimes(server, movieChoice);
                    }
                }
            }            
            while (!validSeat)
            {
                System.out.printf("Which seat would you like to reserve for %s at %s? %nPlease enter the seat number:%n For a list of available seats type \"Seats\"%n",movieChoice, showtimeChoice);
                userInput = input.nextint();
                if (userInputtoLowerCase().equals("seats"))
                {
                    printSeats(server, movieChoice, showtimeChoice);      
                }
                else
                {
                    if(checkSeat(server, movieChoice, showtimeChoice, Integer.parseInt(userInput)))
                    {
                        validSeat = true;
                        seatChoice = userInput;
                        break;
                    }
                }
                if (!validSeat)
                {
                    System.out.printf("That was not a valid selection.%nPlease select from the available seats for %s at %s:%n",movieChoice, showtimeChoice);
                    printSeats(server, movieChoice, showtimeChoice);
                }
            }
            while(!validUser)
            {
                System.out.printf("To purchase this ticket for %s at %s in seat:%s%nnPlease enter your full name. %nNOTE: you must already be registered to purchase a ticket. If you need to check if you are registered then type \"Users\"%n",movieChoice,showtimeChoice,seatChoice);
                userInput= input.nextLine();
                if(userInput.equalsIgnoreCase("users"))
                {
                    printUsers(server);
                }
                else
                {
                    while (results.next())
                    {
                        if(userInput.equalsIgnoreCase(results.getString(1) +" " + results.getString(2)))
                        {
                            validUser = true;
                            userFirst = results.getString(1);
                            userLast = results.getString(2);
                            break;
                        }
                    }
                    if(!validUser)
                    {
                        System.out.println("That user's name was not found.\n Please check the list of users below.\nIf we have an error in your name please contact your system Admin...\n");
                        printUsers(server);                                         
                    }
                }
            }
            results = queries.getUserInfo(server,userFirst,userLast); //Return the funds, rewards points and birthday of user
            Date today = Date; //**********************CHECK THIS!!! Probably Wrong */
            while (!validFunds)
            {
                if (today == results.getString(3))
                {
                    System.out.println("Happy Birthday!\nEnjoy your free movie on us!\nTell your friends!");
                    validFunds = true;
                }
                else if(results.getInt(2) > 9)
                {
                    System.out.printf("Using rewards points for 1 ticket for %s%nAt:%s%nSeat:%s%n",movieChoice,showtimeChoice,seatChoice);
                    //QUERY to UPDATE REWARDS POINTS FOR buying ticket -10 (server,userFirst,UserLast,newpoint value)
                    validFunds = true;
                }
                else if(results.getInt(1) > 9)
                {
                    System.out.printf("Purchasing 1 ticket for %s%nAt:%s%nSeat:%s%n",movieChoice,showtimeChoice,seatChoice);
                    queries.updateFunds(server, userFirst, userLast); // removes funds from user to purchase ticket.
                    queries.incrementPoints(server, userFirst, userLast); //Adds 1 reward point to the user for buying a ticket.
                    validFunds = true;
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
    }

    // Returns a list of all valid users first, and last names)
    public static ResultSet getAllUsers(Connection server)
    {
        try
        {
            return (queries.getUsers(server));
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
    }

    //Prints out all current users on the database
    public static void printUsers(Connection server)
    {
        ResultSet results;
        try
        {
            results = getAllUsers(server);
            System.out.println("List of Current users:\n");
            while (results.next())
            {
            System.out.println(results.getString(1) + " " + results.getString(2));
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
        finally
        {
            try
            {
                if (results != null)
                {
                    results.close();
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
        }
    }

    //Checks if the user is in the database
    public static boolean checkUser(Connection server, String name)
    {
        ResultSet results;
        try
        {
            results = getAllUsers(server);
            while (results.next())
            {
                if (name.equalsIgnoreCase(results.getString(1) +" " + results.getString(2)))
                {
                    return true;
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
        finally
        {
            try
            {
                results.close();
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
        }
    }

    //Returns the user's reward points based on first,last and birthday.
    public static ResultSet getUserRewards(Connection server, String first, String last)
    {
        try
        {
            return (queries.getRewards(server,first,last,bDay));
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
    }

    // Prints out all the showtimes for the selected movie 
    public static void printShowtimes(Connection server, String movie)
    {
        ResultSet results;
        int size = 0;
        try
        {
            if (checkMovie(server, movie))
            {
                results = getShowtimes(server, movie);
                size = results.getlastRow();
                System.out.println("The showtimes for " + movie + " are:");
                while (results.next())
                {
                    System.out.println(results.getString(1));
                    size++;
                }
                if (size == 0)
                {
                    System.out.println("There are no showtimes for " + movie + ".");
                }
            }
            else
            {
                System.out.println("That is not a valid movie.");
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
        finally
        {
            try
            {
                if (results != null)
                {
                    results.close();
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
        }
    }
    //checks if the showtime entered is a valid choice
    public static boolean checkShowtime(Connection server, String movie, String time)
    {
        ResultSet results;
        try
        {
            results = getShowtimes(server, movie);
            while (results.next())
            {
                if (time.equalsIgnoreCase(results.getString(1)))
                {
                    return true;
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
        finally
        {
            try
            {
                if (results != null)
                {
                    results.close();
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
        }
    }


    //gets the showtimes of the user requested movie
    public static ResultSet getShowtimes(Connection server, String movie)
    {
        try
        {
            if (checkMovie(server, movie))
            {
                return queries.getShowtimes(server,movie);
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
    }
    // Prints out all movies on the database.
    public static void printMovies(Connection server)
    {
        ResultSet results;
        try
        {
            results = getAvailableMovies(server);
            System.out.println("Current Movies:");
            while(results.next())
            {
                System.out.println(results.getString(1));
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
        finally
        {
            try
            {
                if (results != null)
                {
                    results.close();
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
        }
    }

    //Returns the result set of all movies titles on the database.
    public static ResultSet getAvailableMovies(Connection server)
    {
        try
        {
            return (queries.getMovieTitles(server));
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
    }

    //Checks if the selected movie is in the database. Uses the connection to the SQL server to check.
    public static boolean checkMovie(Connection server, String movie)
    {
        ResultSet results;
        try
        {
            results = getAvailableMovies(server);
            while (results.next())
            {
                if(results.getString(1).equalsIgnoreCase(movie))
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
        finally
        {
            try
            {
                if (results != null)
                {
                    results.close();
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
        }
    }

/////////ALL SEATS ARE ASSUMING THE SEAT NUMBERS WILL BE INTS if not we need to change it!!!

    //prints out all available seats
    public static void printSeats(Connection server, String movie, String showtime)
    {
        ResultSet results;
        try
        {
            results = getSeats(server,movie,showtime);
            System.out.println("The available seats for " + movie + " at " + showtime + " are:");
            while (results.next())
            {
                System.out.println(results.getInt(1));
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
        finally
        {
            try
            {
                results.close();
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
        }
    }


    //checks if the seat selected is a valid choice
    public static boolean checkSeat(Connection server, String movie, String showtime, Integer seat)
    {
        ResultSet results;
        try
        {
            results = getSeats(server,movie,showtime);
            while (results.next())
            {
                if(results.getInt(1) == seat)
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
        finally
        {
            try
            {
                if (results != null)
                {
                    results.close();
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
        }
    }

    //gets all available seats for the selected movie and showtime. where the userId is Null
    public static ResultSet getSeats(Connection server, String movie, String showtime)
    {
        try
            {
                return (queries.getSeats(server,movie,showtime));
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
    }

}