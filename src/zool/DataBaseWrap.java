package zool;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseWrap {

	   private static java.sql.Connection connection;
	    private static java.sql.Statement statement;

	    //метод подключения к БД
	    public static void connectBD (String path, String nameBD, String user, String passvord) {
	        try {
	            Class.forName("org.postgresql.Driver");
	            try{
	                connection = DriverManager.getConnection(path + nameBD, user, passvord);
	                try{
	                    statement = connection.createStatement();
	                } catch (SQLException exception) {
	                    System.out.println(exception);
	                }
	            } catch (SQLException exception) {
	                System.out.println("Ошибка в подкючении к БД " + exception);
	            }
	        } catch (ClassNotFoundException e) {
	            System.out.println("Ошибка в подключении Драйверов " + e);
	        }
	    }

	    //метод для записи в таблицу values_​​of_the_vectors
	    public static void writeInTableValuesVectors(long id_document,long id_type_table, List<Double> val_vector) throws SQLException
	    {
	        String tmp = val_vector.toString();//переводим массив в строку
	        tmp = tmp.replace('[', '{');//заменяем символы для коректного запроса
	        tmp = tmp.replace(']', '}');
	        String selectInsert = "INSERT INTO values_​​of_the_vectors (id_document, id_type_table, val_vector) values("+id_document+", " + id_type_table + ",'"+ tmp + "')";
	        System.out.println(selectInsert);
	        PreparedStatement ps = connection.prepareStatement(selectInsert);
	        ps.executeUpdate();//отправка запроса
	    }
	    
	    public static void main(String[] args) throws IOException, SQLException {
	        //1 - путь до базы, 2 - имя БД, 3 - пароль
	        connectBD("jdbc:postgresql://127.0.0.1:5432/", "ver2",  "postgres","1");//подключение к бд

	        long idDoc=1;//id - из базы патента или дргих источников
	        long idTypeTable=1;// id - из таблицы  type_table - для таблицы патентов = 1, для других источников = 2
	        List<Double> valVector = new ArrayList<Double>();// массив значений
	        valVector.add(Math.random()*10001);//заполняем массив рандомными вещественными значениямиж
	        valVector.add(Math.random()*10001);
	        valVector.add(Math.random()*10001);
	        valVector.add(Math.random()*10001);
	        valVector.add(Math.random()*10001);
	        writeInTableValuesVectors(idDoc,idTypeTable,valVector);//записываем новые строки таблицы
	    }
}


