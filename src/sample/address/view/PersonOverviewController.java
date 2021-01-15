package sample.address.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import sample.address.MainApp;
import sample.address.model.Person;
import sample.address.util.DateUtil;

/*Теперь мы отобразим в нашей таблице некоторые данные.
 *Для этого необходимо создать класс-контроллер для представления
 *PersonOverview.fxml.
 *(Мы должны разместить этот класс-контроллер в том же пакете, где
 * находится файл разметки PersonOverview.fxml, иначе Scene Builder
 * не сможет его найти. Или же прописать в самом fxml файле, кто
 * есть его controller =>
 * fx:controller="sample.address.view.PersonOverviewController"
 * т.е. указать путь и тогда все Контроллеры могут находится в
 * каком нибудь одном пакете.)
 *
 * Для того, чтобы получить доступ к таблице и меткам представления,
 * мы определим некоторые переменные. Эти переменные и некоторые
 * методы имеют специальную аннотацию @FXML. Она необходима для того,
 * чтобы fxml-файл имел доступ к приватным полям и методам. После
 * этого мы настроим наш fxml-файл так, что при его загрузке приложение
 * автоматически заполняло эти переменные данными.
 *
 * - Все поля и методы, к которым fxml-файлу потребуется доступ, должны
 *   быть отмечены аннотацией @FXML. Несмотря на то, что это требование
 *   предъявляется только для полей и методов с модификатором private,
 *   лучше оставить их закрытыми и помечать аннотацией, чем делать публичными!
 * - После загрузки fxml-файла автоматически вызывается метод initialize().
 *   На этот момент все FXML-поля должны быть инициализированы;
 * - Метод setCellValueFactory(...) определяет, какое поле внутри
 *   класса Person будут использоваться для конкретного столбца в таблице.
 * */
public class PersonOverviewController {
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    @FXML
    private Label createdLabel;

    @FXML
    private Label updatedLabel;

    // Ссылка на главное приложение.
    private MainApp mainApp;

    /**
     * Конструктор.
     * Конструктор вызывается раньше метода initialize().
     */
    public PersonOverviewController() {
    }

    /**
     * Инициализация класса-контроллера. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     * Чтобы знать о том, что пользователь выбрал определённую запись в
     * таблице, нам необходимо прослушивать изменения.
     */
    @FXML
    private void initialize() {
        // Инициализация таблицы адресатов с двумя столбцами.
        firstNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().lastNameProperty());

        // Очистка дополнительной информации об адресате.
        // Если мы передаём в параметр метода showPersonDetails(...) значение
        // null, то все значения меток будут стёрты.
        showPersonDetails(null);

        // Слушаем изменения выбора, и при изменении отображаем
        // дополнительную информацию об адресате.
        // В строке personTable.getSelectionModel... мы получаем
        // selectedItemProperty таблицы и добавляем к нему слушателя.
        // Когда пользователь выбирает запись в таблице, выполняется
        // наше лямбда-выражение. Мы берём только что выбранную запись
        // и передаём её в метод showPersonDetails(...).
        // Если мы передаём в параметр метода showPersonDetails(...) значение
        // null, то все значения меток будут стёрты.
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    /**
     * Вызывается главным приложением, которое даёт на себя ссылку.
     *
     * @param mainApp объект класса MainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Добавление в таблицу данных из наблюдаемого списка
        personTable.setItems(mainApp.getPersonData());
    }

    /**
     * Заполняет все текстовые поля, отображая подробности об адресате.
     * Если указанный адресат = null, то все текстовые поля очищаются.
     * Этот метод для правой части нашего приложения.
     * При выборе адресата в таблице, в правой части приложения будет
     * отображаться дополнительная информация об этом адресате.
     *
     * @param person — адресат типа Person или null
     */
    private void showPersonDetails(Person person) {
        if (person != null) {
            // Заполняем метки информацией из объекта person.
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());

            // TODO: Нам нужен способ для перевода дня рождения в тип String!
            birthdayLabel.setText(DateUtil.format(person.getBirthday()));

//            createdLabel.setText(DateUtil.formatDateTime(person.getCreated()));
//            updatedLabel.setText(DateUtil.formatDateTime(person.getUdated()));
        } else {
            // Если Person = null, то убираем весь текст.
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
//            createdLabel.setText("");
//            updatedLabel.setText("");
        }
    }

    /**
     * Вызывается, когда пользователь кликает по кнопке удаления.
     * когда пользователь нажимает на кнопку Delete, а в таблице
     * ничего не выбрано, он увидит простое диалоговое окно.
     */
    @FXML
    private void handleDeletePerson() {
        // selectedIndex - это число будет соответствовать номеру элемента в
        // массиве, т.е. от 0 до конца массива.
        // Если ничего не будет выбрано, то метод getSelectedIndex()
        // вернеи нам -1
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
        } else {
            // Ничего не выбрано.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Вызывается, когда пользователь кликает по кнопке New...
     * Открывает диалоговое окно с дополнительной информацией нового адресата.
     */
    @FXML
    private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getPersonData().add(tempPerson);
        }
    }

    /**
     * Вызывается, когда пользователь кликает по кнопке Edit...
     * Открывает диалоговое окно для изменения выбранного адресата.
     */
    @FXML
    private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked) {
                showPersonDetails(selectedPerson);
            }

        } else {
            // Ничего не выбрано.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");

            alert.showAndWait();
        }
    }
}
