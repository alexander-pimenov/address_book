package sample.address;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.address.model.Person;
import sample.address.model.PersonListWrapper;
import sample.address.view.BirthdayStatisticsController;
import sample.address.view.PersonEditDialogController;
import sample.address.view.PersonOverviewController;
import sample.address.view.RootLayoutController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

/* Мы работаем с классами-представлениями JavaFX, которые необходимо
 * информировать при любых изменениях в списке адресатов. Это важно,
 * потому что, не будь этого, мы бы не смогли синхронизировать
 * представление данных с самими данными. Для этой цели в JavaFX были
 * введены некоторые новые классы коллекций.
 *
 * Из этих классов нам понадобится класс ObservableList.
 * Мы добавим в код конструктор, который будет создавать некоторые
 * демонстрационный данные и метод-геттер с публичным модификатором доступа.
 *
 * У нас должно работающее приложение адресной книги.
 * Оно позволяет добавлять, изменять и удалять адресатов.
 * Это приложение так же осуществляет проверку всего, что вводит пользователь.
 *
 * Наш класс MainApp ответственный за чтение и запись данных нашего приложения.
 * Для нашей простой модели данных намного легче хранить данные в виде XML.
 * Для этого мы будем использовать библиотеку JAXB (Java Architechture for
 * XML Binding).
 * Пример сгенерированного XML-файла:
 * <persons>
    <person>
        <birthday>1999-02-21</birthday>
        <city>some city</city>
        <firstName>Hans</firstName>
        <lastName>Muster</lastName>
        <postalCode>1234</postalCode>
        <street>some street</street>
    </person>
    <person>
        <birthday>1999-02-21</birthday>
        <city>some city</city>
        <firstName>Anna</firstName>
        <lastName>Best</lastName>
        <postalCode>1234</postalCode>
        <street>some street</street>
    </person>
 * </persons>
 *
 * Посмотрим как это всё работает:
 *
 * 1) Приложение запускается через метод main(...) класса MainApp.
 * 2) Вызывается конструктор public MainApp() и добавляются некоторые тестовые
 * данные.
 * 3) Дальше в классе MainApp запускается метод start(...), который вызывает
 * метод initRootLayout() для инициализации корневого макета из файла
 * rootLayout.fxml. Файл fxml уже знает, какой контроллер следует использовать
 * и связывает представление с RootLayoutController'ом.
 * 4) Класс MainApp из fxml-загрузчика получает ссылку на RootLayoutController
 * и передаёт этому контроллеру ссылку на самого себя. Потом, имея эту ссылку,
 * контроллер может обращаться к публичным методам класса MainApp.
 * 5) В конце метода initRootLayout мы стараемся из настроек Preferences
 * получить путь к последнему открытому файлу адресатов. Если этот файл в
 * настройках описан, то мы загружаем из него данные. Эта процедура
 * перезапишет тестовые данные, которые мы добавляли в конструкторе.
 *
 *
 * JavaFX предоставляет новую возможность развёртывания, называемую “нативная
 * упаковка” (Native Packaging) (другое название - “Автономная упаковка
 * приложения” (Self-Containde Application Package)). Нативный Пакет - это
 * пакет, который вместе с приложением Java содержит и среду выполнения Java
 * для конкретной платформы.
 *
 ******************************************************************
 * В этой программе разбираем туториал:
 * https://code.makery.ch/ru/library/javafx-tutorial/
 *
 * */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * Метод start(Stage primaryStage) запускается при старте программы после
     * создания конструктора класса MainApp.
     * Данные, в виде наблюдаемого списка адресатов.
     */
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        // Устанавливаем иконку приложения.
        this.primaryStage.getIcons().add(new Image("file:resources/images/icon_Address_Book.png"));

        initRootLayout();

        showPersonOverview();
    }

    /**
     * Инициализирует корневой макет.
     */
    private void initRootLayout() {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/rootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Даём контроллеру доступ к главному класса приложения.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Пытается загрузить последний открытый файл с адресатами.
        File file = getPersonFilePath();
        if (file != null) {
            loadPersonDataFromFile(file);
        }
    }

    /**
     * Показывает в корневом макете сведения об адресатах.
     */
    private void showPersonOverview() {
        try {
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/personOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Помещаем сведения об адресатах в центр корневого макета.
            rootLayout.setCenter(personOverview);

            // Даём контроллеру доступ к главному приложению.
            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Конструктор
     */
    public MainApp() {
        // В качестве образца добавляем некоторые данные
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
    }

    /**
     * Возвращает данные в виде наблюдаемого списка адресатов.
     *
     * @return коллекция ObservableList
     */
    public ObservableList<Person> getPersonData() {
        return personData;
    }

    /**
     * Возвращает главную сцену.
     *
     * @return Stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Открывает диалоговое окно для изменения деталей указанного адресата.
     * Если пользователь кликнул OK, то изменения сохраняются в предоставленном
     * объекте адресата и возвращается значение true.
     *
     * @param person - объект адресата, который надо изменить
     * @return true, если пользователь кликнул OK, в противном случае false.
     */
    public boolean showPersonEditDialog(Person person) {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/personEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            // Устанавливаем иконку для окна.
            dialogStage.getIcons().add(new Image("file:resources/images/icon_edit.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаём адресата в контроллер.
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Открывает диалоговое окно для вывода статистики дней рождений.
     */
    public void showBirthdayStatistics() {
        try {
            // Загружает fxml-файл и создаёт новую сцену для всплывающего окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/birthdayStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            // Устанавливаем иконку окна статистики дней рождений.
            dialogStage.getIcons().add(new Image("file:resources/images/icon_calendar.png"));

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаёт адресатов в контроллер.
            BirthdayStatisticsController controller = loader.getController();
            controller.setPersonData(personData);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Благодаря классу Preferences, Java позволяет сохранять некоторую
     * информацию о состоянии приложения. В зависимости от операционной системы,
     * Preferences сохраняются в различных местах (например, в файле реестра Windows).
     * <p>
     * Этот метод возвращает preference файла адресатов, то есть, последний открытый файл.
     * Этот preference считывается из реестра, специфичного для конкретной
     * операционной системы. Если preference не был найден, то возвращается null.
     *
     * @return объект типа File
     */
    public File getPersonFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Задаёт путь текущему загруженному файлу. Этот путь сохраняется
     * в реестре, специфичном для конкретной операционной системы.
     *
     * @param file - файл или null, чтобы удалить путь
     */
    public void setPersonFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Обновление заглавия сцены.
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Обновление заглавия сцены.
            primaryStage.setTitle("AddressApp");
        }
    }

    /**
     * -= Демаршализация =-
     * Загружает информацию об адресатах из указанного файла.
     * Текущая информация об адресатах будет заменена.
     *
     * @param file путь к сохраненному файлу.
     */
    public void loadPersonDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PersonListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Чтение XML из файла и демаршализация.
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());

            // Сохраняем путь к файлу в реестре.
            setPersonFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * -= Маршаллинг =-
     * Сохраняет текущую информацию об адресатах в указанном файле.
     *
     * @param file к файлу
     */
    public void savePersonDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Обёртываем наши данные об адресатах.
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personData);

            // Маршаллируем и сохраняем XML в файл.
            m.marshal(wrapper, file);

            // Сохраняем путь к файлу в реестре.
            setPersonFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

// Инициализация и загрузка основной страницы можно загрузить и таким образом, как
//ниже. Но тогда переход на панель сведений об адресатах нужно еще как то подключать.
//        Parent root = FXMLLoader.load(getClass().getResource("view/rootLayout.fxml"));
//        primaryStage.setTitle("AddressApp");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();


    public static void main(String[] args) {
        launch(args);
    }
}
