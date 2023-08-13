package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/skillbox";
        String user = "root";
        String pass = "Bogdanovvladislav4@";
        String sqlRequest1 = "CREATE TEMPORARY TABLE `tmp_table` CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci\n" +
                "SELECT pl.course_name, timestampdiff(MONTH, MIN(pl.subscription_date), MAX(pl.subscription_date)) \n" +
                "AS 'sales_period', \n" +
                "COUNT(pl.course_name) AS 'sales_quantity', \n" +
                "ROUND((COUNT(pl.course_name) / timestampdiff(MONTH, MIN(pl.subscription_date)\n" +
                ", MAX(pl.subscription_date))), 2) \n" +
                "AS 'average_sales_value'\n" +
                "FROM purchaseList pl \n" +
                "GROUP BY pl.course_name\n" +
                "ORDER BY pl.course_name;";

        String sqlRequest2 = "select t.course_name, MONTH(pl.subscription_date) AS 'months',\n" +
                "IF(pl.course_name = t.course_name, t.average_sales_value, 0) 'average_sales_value'\n" +
                "FROM tmp_table t\n" +
                "JOIN purchaseList pl ON pl.course_name = t.course_name\n" +
                "GROUP BY pl.subscription_date\n" +
                "ORDER BY pl.subscription_date";
        String sqlRequest3 = "DROP TEMPORARY TABLE tmp_table";

        try {
            Connection connection = DriverManager.getConnection(url, user, pass);

            Statement statement = connection.createStatement();

            statement.execute(sqlRequest1);

            ResultSet resultSet = statement.executeQuery(sqlRequest2);

            while (resultSet.next()){
                String courseName = "Наименование курса: " + resultSet.getNString("course_name");
                String months = "Номер месяца: " + resultSet.getString("months");
                String averageSalesValue = "Среднее количество покупок за месяц: " + resultSet.getString("average_sales_value");
                System.out.println(courseName + " - " + months + " - " + averageSalesValue);
            }

            statement.execute(sqlRequest3);
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
