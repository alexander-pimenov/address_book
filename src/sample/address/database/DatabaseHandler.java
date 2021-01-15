package sample.address.database;

import sample.address.model.Person;

import java.sql.*;

/*
 * Класс отвечает за подключение к БД, за запись,
 * чтение из БД.
 * Он наследуется от класса Configs. Т.обр. может
 * использовать данные из Configs для подключения к БД.
 * Он использует плагин jdbc для соединения с любой БД.
 *
 * В качестве примера подключения к БД с использованием
 * app.properties можно посмотреть в job4j/chapter_007
 *
 * Подключиться к базе данных PostgreSQL можно с помощью драйвера JDBC.
 *
 * Сперва скачаем драйвер PostgreSQL JDBC по этому адресу:
 * http://jdbc.postgresql.org/download.html
 *
 * Далее выбираем нужный драйвер: PostgreSQL JDBC 4.2 Driver, 42.2.14 - я выбрал этот.
 * Скачиваем. У нас качается jar файл -> postgresql-42.2.14.jar
 * Через IDEA подключаемся к этой библиотеке:
 * Project Structure -> Libraries -> + lib Java -> выбираем наш файл -> OK
 * Так мы подключаем postgresql в наш проект. Его мы видим в External Libraries.
 *
 */
public class DatabaseHandler extends Configs {

    //Объект Connection для соединения с БД
    private Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        //стока подключения к БД
        String connectionString = "jdbc:postgresql://" + bdHost + ":"
                + dbPort + "/" + dbName + "?currentSchema=" + schemaName;
        /*Прописываем, какой драйвер будем использовать (jdbc.driver или driver-class-name=org.postgresql.Driver)*/
        Class.forName("org.postgresql.Driver");
        /*Помещаем в переменную dbConnection соединение.*/
        dbConnection = DriverManager.getConnection(connectionString,
                dbUserName, dbPass);
        return dbConnection;
    }

    /**
     * Метод помещающий данные Person в таблицу
     * persons в базе данных
     */
    public void createPersonUnit(Person person) {

        /*Пишем SQL запрос для помещения данных в таблицу базы данных*/
        String insert = "INSERT INTO " + Const.PERSON_TABLE + " ("
                + Const.PERSON_FIRSTNAME + "," + Const.PERSON_LASTNAME + ","
                + Const.PERSON_STREET + "," + Integer.valueOf(Const.PERSON_POST_CODE) + ","
                + Const.PERSON_CITY + "," + Const.PERSON_BIRTHDAY + ") "
                + "VALUES (?,?,?,?,?,?)";

        try {
            /*Создаем объект PreparedStatement и инициализируем знаки ?,?..? в SQL запросе,
             * который написан выше.*/
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setString(3, person.getStreet());
            preparedStatement.setString(4, String.valueOf((person.getPostalCode())));
            preparedStatement.setString(5, person.getCity());
            preparedStatement.setString(6, String.valueOf(person.getBirthday()));

            /*Выполняем наш preparedStatement*/
            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод получающий Person из БД, если он там есть.
     * Вводятся только имя и фамилия person. И если есть совпадания по ним,
     * то вернется объект ResultSet, содержащий такого Person.
     * Если не будет совпадений, то никаих данных не будет возвращено.
     */
    public ResultSet getPersonUnit(Person person) {
        ResultSet resultSet = null;

        String select = "SELECT * FROM " + Const.PERSON_TABLE + " WHERE "
                + Const.PERSON_FIRSTNAME + "=? AND " + Const.PERSON_LASTNAME + "=?";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());

            /*Вызываем метод executeQuery(), который возвращает ResultSet*/
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


}
