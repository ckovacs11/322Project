package ser322;

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
public class MovieTheater {
    private static Connection server;
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        // Does a check based on the length of the args to determine if the server will
        // connect before it attempts.
        if (args.length != 4) {
            System.out.println("USAGE: java ser322.MovieTheater <url> <user> <pwd> <driver> ");
            System.exit(0);
        } else {
            try {
                server = DriverManager.getConnection(args[0], args[1], args[2]);
                System.out.println("Connected......");
                mainMenu(server);
            } catch (SQLException sqlexc) {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see error to help solve.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see error to help solve.");
            } finally {
                try {
                    if (server != null) {
                        server.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    }

    private static void mainMenu(Connection server) {
        Integer selection = 0;
        String mainMen = "\nHello,\n Please select from the following options: \n\n1. Check available Movies.\n";
        mainMen = mainMen + "2. Buy Ticket. \n3. Check showtime for certain movie.\n4. Check reward points.";
        mainMen = mainMen
                + "\n5. Add movie.\n6. Delete Movie. \n7. Get all Rewards user Names.\n";
        mainMen = mainMen + "8. Add user.\n9. Delete user.\n10. Exit.\n";
        while (selection != -1) {
            System.out.println(mainMen);
            selection = input.nextInt();
            if (selection < 1 || selection > 12) {
                System.out.println("Please make a valid selection from 1-11.\n Thank you.");
            } else {
                switch (selection) {
                    case 1: // prints all current movies in the theater.
                    {
                        printMovies(server);
                        break;
                    }
                    case 2: // Walks the user through the process of buying a ticket.
                    {
                        buyTicket(server);
                        break;
                    }
                    case 3: {
                        // Will show all showtimes of a user entered movie title. If error then message
                        // will display and allow user to enter again or give up.
                        // Passes connection and movie_title as string.
                        getShowtime(server);
                        break;
                    }
                    case 4: {
                        // Will pass connection user's_name(they enter a name? Should we have a method
                        // to get user names?)
                        findRewards(server);
                        break;
                    }
                    case 5: {
                        // Insert Movie into theater. Should it also add showtimes?
                        addMovie(server);
                        break;
                    }
                    case 6: {
                        // Deletes a movie from theater. (Does it need to delete showtimes too?)
                        deleteMovie(server);
                        break;
                    }                   
                    case 7: {
                        printUsers(server);
                        break;
                    }
                    case 8: {
                        // Adds a user.
                        addUser(server);
                        break;
                    }
                    case 9: {
                        // Deletes a user.
                        deleteUser(server);
                        break;
                    }
                    case 10: {
                        selection = -1;
                        input.close();
                        System.exit(1);
                        break;
                    }
                    default: {
                        System.out.println(
                                "That is not a valid selection please choose a valid option. Also how did you get here?");
                        break;
                    }
                }
            }
        }

    }

    // Takes in a user's name and returns their reward points.
    public static void findRewards(Connection server) {
        String userInput, userFirst = "", userLast = "";
        boolean valid = false;
        input.nextLine();
        while (!valid) {
            System.out.println(
                    "Please enter the name of the user you wish to see the current reward points for:\n Ex: \'Pointer McGee\'");
            userInput = input.nextLine();
            if (checkUser(server, userInput)) {
                valid = true;
                String[] splited = userInput.split(" ");
                userFirst = splited[0];
                userLast = splited[1];
            } else {
                System.out.println(
                        "That was not a valid name. Here is a list of the current users in the reward system.");
                printUsers(server);
            }
        }
        printUserRewards(server, userFirst, userLast);
    }

    // Adds a movie to the theater based on user input. Will validate that the movie
    // is not already in the theatre before adding.
    public static void addMovie(Connection server) {
        String userInput = "", movieName = "", movieRating = "";
        Integer movieRuntime = 0, movieId = 0, userINTput = 0;
        boolean valid = false;
        input.nextLine();
        while (!valid) {
            System.out.println("Please enter the name of the movie you wish to add:");
            userInput = input.nextLine();
            System.out.println(userInput + checkMovie(server, userInput));
            if (!checkMovie(server, userInput)) // We want the movie to not be found.
            {
                valid = true;
                movieName = userInput;
                movieId = generateFilmId(server);
            } else {
                System.out.println(userInput
                        + " is already in the theatre.\nIf you wish to return to main menu type \'Cancel\'\nHere is a list of current movies to remind you what is playing:");
                printMovies(server);
            }
        }
        valid = false;
        while (!valid) {
            System.out.println("Please enter the Rating of " + movieName + ":");
            userInput = input.nextLine();
            if (userInput.equalsIgnoreCase("G") || userInput.equalsIgnoreCase("PG")
                    || userInput.equalsIgnoreCase("PG-13") || userInput.equalsIgnoreCase("R")
                    || userInput.equalsIgnoreCase("NC-17")) {
                valid = true;
                movieRating = userInput;
            } else {
                System.out.println(
                        "That is not an accepted Rating. Please enter one of the following:\nG\nPG\nPG-13\nR\nNC-17");
            }
        }
        valid = false;
        while (!valid) {
            System.out.println("Please enter the Rating of " + movieName + ":");
            userINTput = input.nextInt();
            if (userINTput > 0) {
                valid = true;
                movieRuntime = userINTput;
            } else {
                System.out.println(movieName
                        + " can not have a negative or zero runtime. Please enter a number greater than zero.....");
            }
        }
        insertMovie(server, movieId, movieName, movieRating, movieRuntime);
        System.out.println(movieName + " has been added to the theater's available movies.");
    }

    // Deletes a movie from the theater based on user input. And it ensures that
    // there is the minimum of 10 movies in the theatre at any one time.
    public static void deleteMovie(Connection server) {
        String userInput = "";
        boolean valid = false;
        input.nextLine();
        while (!valid) {
            System.out.println("Please enter the name of the movie you wish to delete:");
            userInput = input.nextLine();
            if (userInput.equalsIgnoreCase("cancel")) {
                System.out.println("Returning to the Main Menu...");
                return;
            }
            if (checkMovie(server, userInput)) {
                if (countMovies(server) > 10) {
                    valid = true;
                } else {
                    System.out.println(
                            "This theatre is under contract to maintain 10 movies at minimum. It is currently at 10 so we can not delete "
                                    + userInput);
                    return;
                }
            } else {
                System.out.println(userInput
                        + " is not a valid selection. Please choose from the following:\n or type \'Cancel\' to return to the main menu.");
                printMovies(server);
            }
        }
        deleteMovies(server, userInput);
        System.out.println(userInput + " has been deleted.");
    }

    // Prints all showtimes for a user entered movie name.
    public static void getShowtime(Connection server) {
        String userInput = "";
        boolean valid = false;
        input.nextLine();
        while (!valid) {
            System.out.println("Please enter the name of the movie you wish to check the showtimes for:\n");
            userInput = input.nextLine();
            if (checkMovie(server, userInput)) {
                valid = true;
            } else {
                System.out.println(userInput + " is not a movie we are showing. Please chose from our current movies:");
                printMovies(server);
            }
        }
        printShowtimes(server, userInput);
    }

    /*
     * Deletes a rewards user that the user inputs. Validates that it is an actual
     * option before delete attempt.
     */
    public static void deleteUser(Connection server) {
        String userInput = "", userFirst = "", userLast = "";
        boolean valid = false;
        while (!valid) {
            System.out.println("Please enter the name of the Rewards user you wish to remove: Ex:\"Tom Tim\"\n");
            userInput = input.nextLine();
            if (userInput.equalsIgnoreCase("cancel")) {
                System.out.println("Returning to Main Menu");
                return;
            }
            if (checkUser(server, userInput)) {
                valid = true;
            } else {
                System.out.println("That user is not found. Please check this list of users to ensure spelling.");
                printUsers(server);
                System.out.println("If you no longer wish to delete a user, please type \'Cancel\'");
            }
        }
        String[] splited = userInput.split(" ");
        userFirst = splited[0];
        userLast = splited[1];
        deleteUsers(server, userFirst, userLast);
        System.out.println(userInput + " has be deleted. Returning to Main Menu.");
    }

    /*
     * This method adds a user to the Rewards program. It will verify that the user
     * is not already in the system. the user will then be asked to add in
     * information for the user.
     */
    public static void addUser(Connection server) {
        String userInput = "", newUserFirst = "", newUserLast = "", birthday = "";
        Integer userFunds = 0, userRewards = 0, userIntInput = 0, userId = 0;
        Boolean valid = false;
        input.nextLine();
        while (!valid) {
            System.out.println(
                    "Please enter the First and Last name of the new user you wish to add:(Separted by a space)\n Ex: Tim Tom");
            userInput = input.nextLine();
            if (userInput.equalsIgnoreCase("cancel")) {
                System.out.println("Returning to Main Menu....");
                return; // exits the addUser method back to the main menu
            }

            if (!checkUser(server, userInput)) // return of false means the user was not found.
            {
                valid = true;
                String[] splited = userInput.split(" ");
                newUserFirst = splited[0];
                newUserLast = splited[1];
            } else {
                System.out.println(userInput
                        + " is already in the system. Please enter a different name.\nTo cancel adding a new user type \'Cancel\'");
            }
        }
        valid = false;
        while (!valid) {
            System.out
                    .println("Please enter " + newUserFirst + " " + newUserLast + "'s Birthday:\n Ex: \'2000-12-31\'");
            userInput = input.nextLine();
            if (userInput.substring(4,5).matches("-") && userInput.substring(7, 8).matches("-")) {
                valid = true;
                birthday = userInput;
            } else {
                System.out.println(
                        "There was an error with the date you entered, please be sure to follow the Year-Month-Day format. Include the \'-\' in the date.");
            }
        }
        valid = false;
        while (!valid) {
            System.out.println("Please enter the funds that " + newUserFirst + " " + newUserLast
                    + " has:\n(Any positive whole number)\n");
            userIntInput = input.nextInt();
            if (userIntInput >= 0) {
                valid = true;
                userFunds = userIntInput;
            } else {
                System.out.println(newUserFirst + " " + newUserLast
                        + " can not be in debt...\nPlease enter a whole number that is 0 or greater.");
            }
        }
        valid = false;
        while (!valid) {
            System.out.println("Please enter the rewards that " + newUserFirst + " " + newUserLast
                    + " has:\n(Any positive whole number)\n");
            userIntInput = input.nextInt();
            if (userIntInput >= 0) {
                valid = true;
                userRewards = userIntInput;
            } else {
                System.out.println(newUserFirst + " " + newUserLast
                        + " can not have negative reward points...\nPlease enter a whole number that is 0 or greater.");
            }
        }
        userId = generateUserId(server);
        insertUser(server, userId, newUserFirst, newUserLast, birthday, userFunds, userRewards);
        System.out.println("Successfully added " + newUserFirst + " " + newUserLast);
    }

    /*
     * This method walks the user through the process of buying a ticket. It give
     * the user options and if the user is lost it prints out the options so the
     * user can enter the required input to move forward. A user can not buy a
     * ticket if they do not select valid options.
     */
    private static void buyTicket(Connection server) {
        String userInput = "", movieChoice = "", showtimeChoice = "", userFirst = "", userLast = "";
        Integer userIntput = -1, seatChoice = 0;
        boolean validMovie = false, validTime = false, validSeat = false, validUser = false, validFunds = false;
        int success = 0, userID = 0;
       
            input.nextLine();
            while (!validMovie) {
                System.out.println(
                        "Please enter the name of the movie you wish to buy a ticket for:\n(If you need to see available movies type \"Movies\".\n");
                userInput = input.nextLine();
                if (userInput.toLowerCase().equalsIgnoreCase("movies")) {
                    printMovies(server);
                } else {
                    validMovie = checkMovie(server, userInput);
                    movieChoice = userInput;
                    if (!validMovie) {
                        System.out
                                .println("That was not a valid selection.\nPlease select from this list of movies.\n");
                        printMovies(server);
                    }
                }
            }
            while (!validTime) {
                System.out.printf(
                        "Which Showtime would you like to see %s? %nPlease enter the time:(Example\"12:20\"%nFor a list of times type \"Times\"%n",
                        movieChoice);
                userInput = input.nextLine();
                if (userInput.equalsIgnoreCase("times")) {
                    printShowtimes(server, movieChoice);
                } else {
                    if (checkShowtime(server, movieChoice, userInput+":00")) {
                        validTime = true;
                        showtimeChoice = userInput;
                        break;
                    }
                    if (!validTime) {
                        System.out.printf(
                                "That was not a valid selection.%nPlease select from the available showtimes for %s:%n",
                                movieChoice);
                        printShowtimes(server, movieChoice);
                    }
                }
            }
            while (!validSeat) {
                System.out.printf(
                        "Which seat would you like to reserve for %s at %s? %nPlease enter the seat number:%n For a list of available seats type \"-1\"%n",
                        movieChoice, showtimeChoice);
                userIntput = input.nextInt();
                if (userIntput.equals(-1)) {
                    printSeats(server, movieChoice, showtimeChoice);
                } else {
                    if (checkSeat(server, movieChoice, showtimeChoice, userIntput)) {
                        validSeat = true;
                        seatChoice = userIntput;
                        break;
                    }
                }
                if (!validSeat && userIntput != -1) {
                    System.out.printf(
                            "That was not a valid selection.%nPlease select from the available seats for %s at %s:%n",
                            movieChoice, showtimeChoice);
                    printSeats(server, movieChoice, showtimeChoice);
                }
            }
            input.nextLine();
            while (!validUser) {
                System.out.printf(
                        "To purchase this ticket for %s at %s in seat:%s%nPlease enter your full name. %nNOTE: you must already be registered to purchase a ticket. If you need to check if you are registered then type \"Users\"%n",
                        movieChoice, showtimeChoice, seatChoice);
                userInput = input.nextLine();
                if (userInput.equalsIgnoreCase("users")) {
                    printUsers(server);
                } else {
                    if (checkUser(server, userInput)) {
                        validUser = true;
                        String[] splited = userInput.split(" ");
                        userFirst = splited[0];
                        userLast = splited[1];
                        break;
                    }
                }
                if (!validUser) {
                    System.out.println(
                            "That user's name was not found.\n Please check the list of users below.\nIf we have an error in your name please contact your system Admin...\n");
                    printUsers(server);
                }
            }

            while (!validFunds) {
                if (getRewards(server, userFirst, userLast)>9) {
                    System.out.printf("Using rewards points for 1 ticket for %s%nAt:%s%nSeat:%s%n", movieChoice,
                            showtimeChoice, seatChoice);
                    // QUERY to UPDATE REWARDS POINTS FOR buying ticket -10
                    // (server,userFirst,UserLast,newpoint value)
                    usePoints(server, userFirst, userLast);
                    validFunds = true;
                } 
                else if (getFunds(server, userFirst, userLast) > 4) 
                {
                    System.out.printf("Purchasing 1 ticket for %s%nAt:%s%nSeat:%s%n", movieChoice, showtimeChoice,
                            seatChoice);
                    // removes funds from user to purchase ticket.
                    useFunds(server, userFirst, userLast);
                    
                    // Adds 1 reward point to the user for buying a ticket.
                    updatePoints(server, userFirst, userLast);
                }

                    else {
                    System.out.println(
                            "You do not have enough funds in your account.\n Please vist the ticket window or atm to add funds.\n Thank you.");
                    break;
                }

                userID = getUserId(server, userFirst, userLast);

                // reserve seat
                success = reserveSeat(server, userID, seatChoice);
                if (success > 0) {
                    System.out.println("Seat successfully reserved");
                } else {
                    System.out.println("Error in reserving seat");
                }
                System.out.println("Enjoy your movie, and dont forget the popcorn!");
            }

        
        }
    

    // Returns a list of all valid users first, and last names)
    public static ResultSet getAllUsers(Connection server) {
        ResultSet results = null;
        Statement stmt = null;
        try {
            stmt = server.createStatement();
            results = stmt.executeQuery("SELECT * FROM user");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        }
        return results;
    }

    // Prints out all current users on the database
    public static void printUsers(Connection server) {
        ResultSet results = null;
        Statement stmt = null;
        try {
            stmt = server.createStatement();
            results = stmt.executeQuery("SELECT * FROM user");
            System.out.println("List of Current users:\n");
            while (results.next()) {
                System.out.println(results.getString(2) + " " + results.getString(3));
            }
        } catch (SQLException sqlexc) {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqlexc) {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see error to help solve.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see error to help solve.");
            }
        }
    }

    // Checks if the user is in the database
    public static boolean checkUser(Connection server, String name) {
        ResultSet results = null;
        Statement stmt = null;
        boolean toRet = false;
        try {
            stmt = server.createStatement();
            results = stmt.executeQuery("SELECT First_Name, Last_Name FROM user");
            while (results.next()) {
                if (name.equalsIgnoreCase(results.getString(1) + " " + results.getString(2))) {
                    toRet = true;
                }
            }
        } catch (SQLException sqlexc) {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqlexc) {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see error to help solve.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see error to help solve.");
            }
        }
        return toRet;
    }

    // Prints the user rewards points for the user queried
    public static void printUserRewards(Connection server, String first, String last) {
        ResultSet results = null;
        PreparedStatement ps = null;
        try {
            ps = server.prepareStatement("SELECT user.reward_Points FROM user WHERE First_Name=? AND Last_Name=?");
            ps.setString(1, first);
            ps.setString(2, last);
            results = ps.executeQuery();
            System.out.print(first + " " + last + " has ");
            while (results.next()) {
                System.out.print(results.getInt(1));
                System.out.println(" reward points.");
            }
        } catch (SQLException sqlexc) {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqlexc) {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see error to help solve.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see error to help solve.");
            }
        }
    }

    // Prints out all the showtimes for the selected movie
    public static void printShowtimes(Connection server, String movie) {
        ResultSet results = null;
        int size = 0;
        PreparedStatement ps = null;
        try {
            if (checkMovie(server, movie)) {
                ps = server.prepareStatement(
                        "SELECT Time FROM film_showtime JOIN film ON film.Film_ID = film_showtime.Film_ID JOIN showtime ON film_showtime.Showtime_ID = showtime.Showtime_ID WHERE title =?");
                ps.setString(1, movie);
                results = ps.executeQuery();
                System.out.println("The showtimes for " + movie + " are:");
                while (results.next()) {
                    System.out.println(results.getString(1));
                    size++;
                }
                if (size == 0) {
                    System.out.println("There are no showtimes for " + movie + ".");
                }
            } else {
                System.out.println("That is not a valid movie.");
            }
        } catch (SQLException sqlexc) {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        } finally {
            try {
                if (results != null) {
                    results.close();
                }

                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqlexc) {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see error to help solve.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see error to help solve.");
            }
        }
    }

    // checks if the showtime entered is a valid choice
    public static boolean checkShowtime(Connection server, String movie, String time) {
        ResultSet results = null;
        boolean toRet = false;
        PreparedStatement ps = null;
        try {
            if (checkMovie(server, movie)) {
                ps = server.prepareStatement(
                        "SELECT Time FROM film_showtime JOIN film ON film.Film_ID = film_showtime.Film_ID JOIN showtime ON film_showtime.Showtime_ID = showtime.Showtime_ID WHERE title =?");
                ps.setString(1, movie);
                results = ps.executeQuery();
            }
            while (results.next()) {
                if (time.equalsIgnoreCase(results.getString(1))) {
                    toRet = true;
                }
            }
        } catch (SQLException sqlexc) {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqlexc) {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see error to help solve.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see error to help solve.");
            }
        }
        return toRet;
    }

    // Prints out all movies on the database.
    public static void printMovies(Connection server) {
        ResultSet results = null;
        Statement stmt = null;
        try {

            stmt = server.createStatement();
            results = stmt.executeQuery("SELECT Title FROM film");
            System.out.println("Current Movies:");
            while (results.next()) {
                System.out.println(results.getString(1));
            }
        }

        catch (SQLException sqlexc) {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqlexc) {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see error to help solve.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see error to help solve.");
            }
        }
    }

    // Checks if the selected movie is in the database. Uses the connection to the
    // SQL server to check.
    public static boolean checkMovie(Connection server, String movie) {
        ResultSet results = null;
        boolean toRet = false;
        try {

            PreparedStatement ps = server.prepareStatement("SELECT Title FROM film WHERE Title=?");
            ps.setString(1, movie);
            results = ps.executeQuery();

            while (results.next()) {
                if (results.getString(1).equalsIgnoreCase(movie)) {

                    toRet = true;
                }
            }
        } catch (SQLException sqlexc) {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException sqlexc) {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see error to help solve.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see error to help solve.");
            }
        }
        return toRet;
    }

    ///////// ALL SEATS ARE ASSUMING THE SEAT NUMBERS WILL BE INTS if not we need to
    ///////// change it!!!

    // prints out all available seats
    public static void printSeats(Connection server, String movie, String showtime) {
        ResultSet results = null;
        PreparedStatement ps = null;
        try {
            ps = server.prepareStatement(
                    "SELECT ss.Seat_ID FROM seat_showtime ss, showtime s, film f, film_showtime fs WHERE ss.Showtime_ID = fs.showtime_id and s.Showtime_ID = fs.Showtime_ID and s.Time =?  AND f.Title = ? AND ss.User_ID is null And fs.Film_ID = f.Film_ID");
            ps.setString(1, showtime + ":00");
            ps.setString(2, movie);
            results = ps.executeQuery();
            System.out.println("The available seats for " + movie + " at " + showtime + " are:");
            while (results.next()) {
                System.out.println(results.getInt(1));
            }

        } catch (SQLException sqlexc) {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqlexc) {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see error to help solve.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see error to help solve.");
            }
        }
    }

    // checks if the seat selected is a valid choice
    public static boolean checkSeat(Connection server, String movie, String showtime, Integer seat) {
        ResultSet results = null;
        PreparedStatement ps = null;
        boolean toRet = false;
        try {
            ps = server.prepareStatement(
                    "SELECT ss.Seat_ID FROM seat_showtime ss, showtime s, film f, film_showtime fs WHERE ss.Showtime_ID = fs.showtime_id and s.Showtime_ID = fs.Showtime_ID and s.Time =?  AND f.Title = ? AND ss.User_ID is null And fs.Film_ID = f.Film_ID");
            ps.setString(1, showtime);
            ps.setString(2, movie);
            results = ps.executeQuery();
            while (results.next()) {
                if (results.getInt(1) == seat) {
                    toRet = true;
                }
            }
        } catch (SQLException sqlexc) {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqlexc) {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see error to help solve.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see error to help solve.");
            }
        }
        return toRet;
    }

    // Generates a random 5 didgit number for userID and checks that it is not
    // already used. If it is then it will re-generate a new number and check again.
    public static int generateUserId(Connection server) {
        ResultSet results = null;
        Statement stmt = null;
        Integer randId = 00000;
        boolean valid = false, match = false;
        try {
            while (!valid) {
                randId = 10000 + new Random().nextInt(90000); // 10000 ≤ n ≤ 99999
                stmt = server.createStatement();
                results = stmt.executeQuery("SELECT User_ID FROM user");
                while (results.next()) {
                    if (randId == results.getInt(1)) {
                        match = true;
                        break;
                    }

                    if (!match) // if the userId was not found
                    {
                        valid = true;
                        break;
                    } else {
                        match = false; // Resets the match boolean and restarts the loop.
                    }
                }
            }
        } catch (SQLException sqlexc) {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqlexc) {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see error to help solve.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see error to help solve.");
            }
        }
        return randId;
    }

    // Generates a random 5 didgit number for filmID and checks that it is not
    // already used. If it is then it will re-generate a new number and check again.
    public static int generateFilmId(Connection server) {
        ResultSet results = null;
        Statement stmt = null;
        Integer randId = 00000;
        boolean valid = false, match = false;
        try {
            while (!valid) {
                randId = 10000 + new Random().nextInt(90000); // 10000 ≤ n ≤ 99999
                stmt = server.createStatement();
                results = stmt.executeQuery("SELECT Film_ID FROM film");
                while (results.next()) {
                    if (randId == results.getInt(1)) {
                        match = true;
                        break;
                    }

                    if (!match) // if the userId was not found
                    {
                        valid = true;
                        break;
                    } else {
                        match = false; // Resets the match boolean and restarts the loop.
                    }
                }
            }
        } catch (SQLException sqlexc) {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqlexc) {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see error to help solve.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see error to help solve.");
            }
        }
        return randId;
    }

    // Prints out all the movies that the user has a ticket for.
    public static void printUserTickets(Connection server, String userFirst, String userLast) {
        ResultSet results = null, rs = null;
        PreparedStatement ps = null, pstmt = null;
        int count = 0;
        try {
            pstmt = server.prepareStatement("SELECT User_ID from user WHERE First_Name=? AND Last_Name=?");
            pstmt.setString(1, userFirst);
            pstmt.setString(2, userLast);
            rs = pstmt.executeQuery();
            int userID = rs.getInt("User_ID");

            ps = server.prepareStatement(
                    "SELECT f.Title FROM Film f, Seat_Showtime ss, Film_Showtime fs WHERE fs.Film_ID = f.Film_ID and fs.Showtime_ID = ss.Showtime_ID and ss.user_ID = ?");
            ps.setInt(1, userID);
            results = ps.executeQuery();
            System.out.println(userFirst + " " + userLast + " has tickets to:");
            while (results.next()) {
                System.out.println(results.getString(1));
                count++;
            }
            if (count == 0) {
                System.out.println(userFirst + " " + userLast + " does not have any tickets.");
            }
        } catch (SQLException sqlexc) {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqlexc) {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see error to help solve.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see error to help solve.");
            }
        }
    }

    public static int countMovies(Connection server) {
        Integer count = 0;
        ResultSet results = null;
        Statement stmt = null;
        try {

            stmt = server.createStatement();
            results = stmt.executeQuery("SELECT Title FROM film");
            while (results.next()) {
                count++;
            }
        } catch (SQLException sqlexc) {
            sqlexc.printStackTrace();
            System.out.println("A SQL error occurred. Please see error to help solve.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("An error occurred. Please see error to help solve.");
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqlexc) {
                sqlexc.printStackTrace();
                System.out.println("A SQL error occurred. Please see error to help solve.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("An error occurred. Please see error to help solve.");
            }
        }
        return count;
    }

    // insert film
    public static int insertMovie(Connection s, int id, String title, String rating, int runtime) {
        Statement stmt = null;
        int success = 0;
        try {
            String sql = "INSERT INTO film VALUES (" + Integer.toString(id) + ", " + title + ", " + rating + ", "
                    + Integer.toString(runtime) + ")";
            System.out.println("SQL statement to be inserted: " + sql);
            stmt = s.createStatement();
            success = stmt.executeUpdate(sql);
            if (success > 0) {
                System.out.println("Movie successfully added");
            } else {
                System.out.println("Movie not added. Check SQL statement.");
            }

        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when inserting movie.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("Error occurred when inserting movie.");
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return success;
    }

    // delete film based on name
    public static int deleteMovies(Connection s, String title) {
        PreparedStatement ps = null;
        int success = 0;
        try {
            ps = s.prepareStatement("DELETE FROM film WHERE Title =?");
            ps.setString(1, title);
            success = ps.executeUpdate();
            if (success > 0) {
                System.out.println("Movie successfully deleted");
            } else {
                System.out.println("Movie not deleted. Check SQL statement.");
            }

        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when deleting movie.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("Error occurred when deleting movie.");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return success;

    }

    // insert user
    public static int insertUser(Connection s, Integer id, String first, String last, String bday, Integer funds, Integer points) {
        Statement stmt = null;
        int success = 0;
        try {
            PreparedStatement ps = s.prepareStatement("INSERT INTO user (User_ID, First_Name, Last_Name, Birthday, Funds, Reward_Points) VALUES (?,?,?,?,?,?)");
            ps.setInt(1, id);
            ps.setString(2, first);
            ps.setString(3, last);
            ps.setString(4, bday);
            ps.setInt(5, funds);
            ps.setInt(6, points);
            int rs = ps.executeUpdate();
            if (rs > 0) {
                System.out.println("User successfully added");
            } else {
                System.out.println("User not added. Check SQL statement.");
            }

        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when inserting user.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("Error occurred when inserting user.");
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return success;
    }

    // delete user based on name
    public static int deleteUsers(Connection s, String first, String last) {
        PreparedStatement ps = null;
        int success = 0;
        try {
            ps = s.prepareStatement("DELETE FROM user WHERE First_Name =? AND Last_Name =?");
            ps.setString(1, first);
            ps.setString(2, last);
            success = ps.executeUpdate();
            if (success > 0) {
                System.out.println("MoUser successfully deleted");
            } else {
                System.out.println("User not deleted. Check SQL statement.");
            }

        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when deleting user.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("Error occurred when deleting user.");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return success;

    }

    public static int updatePoints(Connection s, String first, String last) {
        PreparedStatement ps = null;
        int rowAffected = 0;
        try {
            ps = s.prepareStatement("UPDATE user SET Reward_Points = Reward_Points + 1 WHERE First_Name =? AND Last_Name=?");
            ps.setString(1, first);
            ps.setString(2, last);
            rowAffected = ps.executeUpdate();

        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when updating reward points.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("Error occurred when updating reward points.");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return rowAffected;

    }
    public static int usePoints(Connection s, String first, String last) {
        PreparedStatement ps = null;
        int rowAffected = 0;
        try {
            ps = s.prepareStatement("UPDATE user SET Reward_Points = Reward_Points - 10 WHERE First_Name =? AND Last_Name=?");
            ps.setString(1, first);
            ps.setString(2, last);
            rowAffected = ps.executeUpdate();

        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when updating reward points.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("Error occurred when updating reward points.");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return rowAffected;

    }

    public static int useFunds(Connection s, String first, String last) {
        PreparedStatement ps = null;
        int rowAffected = 0;
        try {
            ps = s.prepareStatement("UPDATE user SET Funds = Funds - 5 WHERE First_Name =? AND Last_Name=?");
            ps.setString(1, first);
            ps.setString(2, last);
            rowAffected = ps.executeUpdate();

        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when updating funds.");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("Error occurred when updating funds.");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return rowAffected;

    }

    public static int getFunds (Connection server, String first, String last)
    {
        PreparedStatement ps = null;
        ResultSet results = null;
        Integer toRet = 0;
        try
        {
            ps = server.prepareStatement("Select u.Funds from user u where u.First_Name = ? and u.Last_Name = ?");
            ps.setString(1, first);
            ps.setString(2, last);
            results = ps.executeQuery();
            while (results.next())
            {
                toRet = results.getInt(1);
            }
        }
        catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when getting funds.");
        } catch (Exception exc) 
        {
            exc.printStackTrace();
            System.out.println("Error occurred when getting funds.");
        } 
        finally 
        {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        }
    return toRet;
    }

    public static int getRewards(Connection server, String first, String last)
    {
        PreparedStatement ps = null;
        ResultSet results = null;
        Integer toRet = 0;
        try
        {
            ps = server.prepareStatement("Select u.Reward_Points from user u where u.First_Name = ? and u.Last_Name = ?");
            ps.setString(1, first);
            ps.setString(2, last);
            results = ps.executeQuery();
            while (results.next())
            {
                toRet = results.getInt(1);
            }
        }
        catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when updating reward points.");
        } catch (Exception exc) 
        {
            exc.printStackTrace();
            System.out.println("Error occurred when updating reward points.");
        } 
        finally 
        {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        }
    return toRet;
    }

    public static int getUserId(Connection server, String first, String last)
    {
        PreparedStatement ps = null;
        ResultSet results = null;
        Integer toRet = 0;
        try
        {
            ps = server.prepareStatement("Select u.User_Id from user u where u.First_Name = ? and u.Last_Name = ?");
            ps.setString(1, first);
            ps.setString(2, last);
            results = ps.executeQuery();
            while (results.next())
            {
                toRet = results.getInt(1);
            }
        }
        catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when getUserId.");
        } catch (Exception exc) 
        {
            exc.printStackTrace();
            System.out.println("Error occurred when getUserId.");
        } 
        finally 
        {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        }
    return toRet;
    }

    public static Integer reserveSeat(Connection server, Integer userID, Integer seatChoice)
    {
        PreparedStatement ps = null;
        Integer success = 0;
        try
        {
            ps = server.prepareStatement(
            "UPDATE seat_showtime SET User_ID = ? WHERE Seat_ID =?");
            ps.setInt(1, userID);
            ps.setInt(2, seatChoice);
            success = ps.executeUpdate();
    }
        catch (SQLException se) {
            se.printStackTrace();
            System.out.println("Error occurred when reserving seat.");
        } 
        catch (Exception exc) 
        {
            exc.printStackTrace();
            System.out.println("Error occurred when reserving seat.");
        } 
        finally 
        {
            try {
                if (ps != null) {
                    ps.close();
                }
            }
            catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return success;
    }


}