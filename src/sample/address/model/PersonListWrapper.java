package sample.address.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Вспомогательный класс для обёртывания списка адресатов.
 * Используется для сохранения списка адресатов в XML.
 * <p>
 * Данные, которые мы хотим сохранять, находятся в переменной
 * personData класса MainApp. JAXB требует, чтобы внешний класс
 * наших данных был отмечен аннотацией @XmlRootElement (только класс,
 * поле этой аннотацией пометить нельзя). Типом переменной
 * personData является ObservableList, а его мы не можем аннотировать.
 * Для того, чтобы разрешить эту ситуацию, необходимо создать
 * класс-обёртку, который будет использоваться исключительно
 * для хранения списка адресатов, и который мы сможем
 * аннотировать как @XmlRootElement.
 * <p>
 * Для нашей простой модели данных намного легче хранить данные в виде XML.
 * Для этого мы будем использовать библиотеку JAXB (Java Architechture for
 * XML Binding). Написав всего несколько строк кода, JAXB позволит нам
 * сгенерировать XML-файл.
 *
 * @author Marco Jakob
 */

//@XmlRootElement определяет имя корневого элемента.
@XmlRootElement(name = "persons")
public class PersonListWrapper {

    private List<Person> persons;

    //@XmlElement это необязательное имя, которое мы можем задать для элемента.
    @XmlElement(name = "person")
    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}