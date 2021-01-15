package sample.address.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sample.address.util.LocalDateAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import static java.time.LocalDateTime.now;

/*Класс-модель Person необходим для хранения в нашей будущей
 * адресной книге информации об адресатах. В нём будет несколько
 * переменных для хранения информации об имени, адресе и дне рождения.
 *
 * В JavaFX для всех полей класса-модели предпочтительно использовать.
 * Property позволяет нам получать автоматические уведомления при любых
 * изменениях переменных, таких как lastName или любых других. Это
 * позволяет поддерживать синхронность представления и данных.
 */
public class Person {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty street;
    private final IntegerProperty postalCode;
    private final StringProperty city;
    private final ObjectProperty<LocalDate> birthday;
//    private final ObjectProperty<LocalDateTime> created;
//    private final ObjectProperty<LocalDateTime> udated;

    /**
     * Конструктор по умолчанию.
     */
    public Person() {
        this(null, null);
    }

    /**
     * Конструктор с некоторыми начальными данными.
     *
     * @param firstName имя персоны
     * @param lastName  фамилия персоны
     */
    public Person(String firstName, String lastName) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);

        // Какие-то фиктивные начальные данные для удобства тестирования.
        this.street = new SimpleStringProperty("какая-то улица");
        this.postalCode = new SimpleIntegerProperty(1234);
        this.city = new SimpleStringProperty("какой-то город");
        this.birthday = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, Month.AUGUST, 21));
//        this.birthday = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 8, 21));
//        this.created = new SimpleObjectProperty<LocalDateTime>(now());
//        this.udated = new SimpleObjectProperty<LocalDateTime>(null);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getStreet() {
        return street.get();
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public StringProperty streetProperty() {
        return street;
    }

    public int getPostalCode() {
        return postalCode.get();
    }

    public void setPostalCode(int postalCode) {
        this.postalCode.set(postalCode);
    }

    public IntegerProperty postalCodeProperty() {
        return postalCode;
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public StringProperty cityProperty() {
        return city;
    }

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate getBirthday() {
        return birthday.get();
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday.set(birthday);
    }

    public ObjectProperty<LocalDate> birthdayProperty() {
        return birthday;
    }

//    public LocalDateTime getCreated() {
//        return created.get();
//    }

//    public ObjectProperty<LocalDateTime> createdProperty() {
//        return created;
//    }

//    public void setCreated(LocalDateTime created) {
//        this.created.set(created);
//    }

//    public LocalDateTime getUdated() {
//        return udated.get();
//    }

//    public ObjectProperty<LocalDateTime> udatedProperty() {
//        return udated;
//    }

//    public void setUdated(LocalDateTime udated) {
//        this.udated.set(udated);
//    }
}
