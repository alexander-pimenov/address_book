package sample.address.view;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import sample.address.model.Person;

/**
 * Контроллер для представления статистики дней рождений.
 * <p>
 * Как работает этот контроллер:
 * 1) Контроллеру нужен доступ к двум элементам из нашего fxml-файла:
 * - barChart использует типы данных String и Integer. Тип данных String
 * отображает название месяцев на оси X, а тип данных Integer - количество
 * записей в конкретном месяце.
 * - Ось xAxis мы используем для добавления названий месяцев.
 * 2) Метод initialize() заполняет ось X строковыми значениями названий всех месяцев.
 * 3) Метод setPersonData(...) будет доступен классу MainApp для передачи
 * данных об адресатах. Он проходится по всем адресатам и подсчитывает
 * количество дней рождений в каждом месяце. Потом он добавляет
 * XYChart.Data для каждого месяца в серию данных XYChart.Series.
 * Каждый объект XYChart.Data будет представлять один столбец диаграммы.
 *
 * @author Marco Jakob
 */
public class BirthdayStatisticsController {

    @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis xAxis;

    private ObservableList<String> monthNames = FXCollections.observableArrayList();

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл был загружен.
     */
    @FXML
    private void initialize() {
        // Получаем массив с английскими именами месяцев. Короткие названия
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getShortMonths();

        //Если есть необходимость, то можно брать полные названия месяцев:
//        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();

        // Преобразуем его в список и добавляем в наш ObservableList месяцев.
        monthNames.addAll(Arrays.asList(months));

        // Назначаем имена месяцев категориями для горизонтальной оси.
        xAxis.setCategories(monthNames);
    }

    /**
     * Задаёт адресатов, о которых будет показана статистика.
     *
     * @param persons список адресатов
     */
    public void setPersonData(List<Person> persons) {
        // Считаем адресатов, имеющих дни рождения в указанном месяце.
        int[] monthCounter = new int[12];
        for (Person p : persons) {
            int month = p.getBirthday().getMonthValue() - 1;
            monthCounter[month]++;
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        // Создаём объект XYChart.Data для каждого месяца.
        // Добавляем его в серии.
        for (int i = 0; i < monthCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
        }

        barChart.getData().add(series);
    }
}