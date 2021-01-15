package sample.address.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Класс для преобразование дня рождения в строку.
 * Т.к. мы просто так не можем присвоить текстовой метке значение
 * поля birthday, т.к. тип его значения LocalDate, а не String.
 * Для этого нам надо сначала отформатировать дату.
 * Мы будем преобразовывать тип LocalDate в тип String и обратно
 * в нескольких местах программы, поэтому, создадим для этой цели
 * вспомогательный класс, содержащий статические методы.
 * Формат даты можно поменять, просто изменив константу DATE_PATTERN.
 * Все возможные форматы описаны в документации к классу
 * [DateTimeFormatter](http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html)
 * <p>
 * * Для того, чтобы отображать дату и время в удобном формате
 * * можно использовать класс SimpleDataFormat.
 * * При создании шаблона представления даты SimpleDateFormat
 * * использовались следующие параметры :
 * * dd — означает день;
 * * MM — месяц (номер месяца);
 * * MMM — месяц (сокращенное название месяца в соответствии с локализацией);
 * * MMMM — месяц (полное название месяца в соответствии с локализацией);
 * * yyyy — год;
 * * hh — часы;
 * * HH - 2 цифры для часов в 24-часовом формате;
 * * mm — минуты;
 * * !!! В КАЧЕСТВЕ РАЗДЕЛИТЕЛЯ МОЖНО ИСПОЛЬЗОВАТЬ ЛЮБОЙ ТЕКСТ !!!
 * * Символы форматирования строки
 * * A - AM или PM
 * * d - день месяца (1-31)
 * * D - день в году (1-366)
 * * H - часы в формате AM/PM (1-12)
 * * K - часы в формате суток (1-24)
 * * M - минуты (0-59)
 * * S - секунды (0-59)
 * * W - неделя в году (1-53)
 * * y - год
 * * z - часовой пояс
 * * GG - эра
 * * E - день недели (сокращение) - Вт
 * * EEEE - день недели (полностью) - пятница
 */
public class DateUtil {
    /**
     * Шаблон даты, используемый для преобразования. Можно поменять на свой.
     */
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm";

    /**
     * Форматировщик даты.
     */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * Форматировщик даты и времени.
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    /**
     * Возвращает полученную дату в виде хорошо отформатированной строки.
     * Используется определённый выше {@link DateUtil#DATE_PATTERN}.
     *
     * @param date - дата, которая будет возвращена в виде строки
     * @return отформатированную строку
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return date.format(pattern);
//        return DATE_FORMATTER.format(date);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter patten = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateTime.format(patten);
    }

    /**
     * Преобразует строку, которая отформатирована по правилам
     * шаблона {@link DateUtil#DATE_PATTERN} в объект {@link LocalDate}.
     * <p>
     * Возвращает null, если строка не может быть преобразована.
     *
     * @param dateString - дата в виде String
     * @return объект даты или null, если строка не может быть преобразована
     */
    public static LocalDate parse(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH);

            return LocalDate.parse(dateString, formatter);
//            return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Проверяет, является ли строка корректной датой.
     *
     * @param dateString - дата в виде String
     * @return true, если строка является корректной датой
     */
    public static boolean validDate(String dateString) {
        // Пытаемся разобрать строку.
        return DateUtil.parse(dateString) != null;
    }
}
