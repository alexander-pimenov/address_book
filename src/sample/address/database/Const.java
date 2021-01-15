package sample.address.database;

/*
 * В классе хранятся все константы нужные для
 * связи с конкретной таблицей, полями, чтобы не
 * думать, как они там прописаны, а просто
 * использовать эти константы.
 * public static final String - общедоступные статические
 * (т.е. используем без создания объета) не изменяемые
 * поля.
 *
 * Вместо этого класса можно использовать enum.
 */

public class Const {
    public static final String PERSON_TABLE = "persons";

    // поля в таблице persons (вводим эти соответсвия чтоб не
    // сделать ошибок при наборе имени полей)
    public static final String PERSON_ID = "id_person";
    public static final String PERSON_FIRSTNAME = "first_name";
    public static final String PERSON_LASTNAME = "last_name";
    public static final String PERSON_STREET = "street";
    public static final String PERSON_POST_CODE = "postal_code";
    public static final String PERSON_CITY = "city";
    public static final String PERSON_BIRTHDAY = "birthday";
    public static final String PERSON_CREATED = "created";
    public static final String PERSON_UPDATED = "updated";

// | id
// | first_name
// | last_name
// | street
// | postal_code
// | city
// | birthday
// | created
// | updated
}
