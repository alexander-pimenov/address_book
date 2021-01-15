package sample.address.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Адаптер (для JAXB) для преобразования между типом LocalDate и строковым
 * представлением даты в стандарте ISO 8601, например как '2012-12-03'.
 *
 * @author Marco Jakob
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        return LocalDate.parse(v, formatter);
//        return LocalDate.parse(v);
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        return v.format(pattern);
//        return v.toString();
    }
}