package sample.address.database;

/*Класс для соединения с сервером базы данных.
 * Подключимся в консоли к БД :
 * psql -d postgres -U postgres
 * или
 * psql --username=postgres
 * */
public class Configs {
    protected String bdHost = "localhost"; //127.0.0.1
    protected String dbPort = "5432"; //port
    protected String dbUserName = "postgres"; //username - имя пользователя
    protected String dbPass = "qwerty"; //password - пароль пользователя
    protected String dbName = "postgres"; //имя БД
    protected String schemaName = "address_app"; //название схемы в БД

}
