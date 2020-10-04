package Ser322;

import java.io.*;
import java.util.*;
import java.sql.*;


/* SER322 Fall 2020 Session A
 *   @Author: David Aldridge
 *   @Version :1.000000000
 *
 * This project was to use the JDBC to communicate with a java server. The input arguments from the command line
 * Determined the correct mothod to use and will direct the user to the readme if they are using an known command.
 * The server connection is always closed in a finally block.
 */
public class MovieTheater
{
    private static Connection server;
    private static Scanner input;
    public MovieTheater()
    {

    }

    public static void main(String[] args) {
        //Does a check based on the length of the args to determine if the server will connect before it attempts.
        if (args.length != 4)
        {
            System.out.println("USAGE: java ser322.MovieTheater <url> <user> <pwd> <driver> ");
            System.exit(0);
        }
        else
        {
            try {
                server = DriverManager.getConnection(args[0], args[1], args[2]);
                System.out.println("Connected......");
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
        }
    }

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