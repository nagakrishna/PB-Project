package king;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import java.io.IOException;

//@WebServlet(name = "ServletPB")
@WebServlet("/query1")
public class ServletPB extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, X-Auth-Token, Content-Type");
        response.setContentType("application/json");
        String queryRequest = request.getParameter("get");
        String result = null;
        if(queryRequest.equals("query1")){
            SparkJava obj = new SparkJava();
            result = obj.QueryExecution("LOCATION");
        }
        else if(queryRequest.equals("query2")){
            SparkJava obj = new SparkJava();
            result = obj.QueryExecution("CRAZE");
        }
        else if(queryRequest.equals("query3")){
            SparkJava obj = new SparkJava();
            result = obj.QueryExecution("LANGUAGE");
        }
        else if(queryRequest.equals("query4")){
            SparkJava obj = new SparkJava();
            result = obj.QueryExecution("SOURCE");
        }
        else if(queryRequest.equals("query5")){
            SparkJava obj = new SparkJava();
            result = obj.QueryExecution("CELEBRITIES");
        }
        else if(queryRequest.equals("query6")){
            SparkJava obj = new SparkJava();
            result = obj.QueryExecution("DAY");
        }
        else if(queryRequest.equals("query7")){
            SparkJava obj = new SparkJava();
            result = obj.QueryExecution("STATS");
        }
        else if(queryRequest.equals("query8")){
            SparkJava obj = new SparkJava();
            result = obj.QueryExecution("TIMEZONE");
        }
        else{
            result = "Query is not valid";
        }
//        try {
//            SparkJava obj = new SparkJava();
//            result = obj.QueryExecution();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if(result!= null ){
            response.getWriter().write(result);
        }
        else
        {
            response.getWriter().write("No data found");
        }
    }
}
