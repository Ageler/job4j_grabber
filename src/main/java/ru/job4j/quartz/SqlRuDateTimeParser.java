package ru.job4j.quartz;


import java.time.*;
import java.util.Map;
import static java.util.Map.entry;

public class SqlRuDateTimeParser implements DateTimeParse {

    private static final Map<String, String> MONTH = Map.ofEntries(
                entry("янв", "01"),
            entry("фев", "02"),
            entry("мар", "03"),
            entry("апр", "04"),
            entry("май", "05"),
            entry("июн", "06"),
            entry("июл", "07"),
            entry("авг", "08"),
            entry("сен", "09"),
            entry("окт", "10"),
            entry("ноя", "11"),
            entry("дек", "12")
    );

    @Override
    public LocalDateTime parse(String parse) {
        String[] jobDate = parse.split(",");
        int year;
        int day;
        int month;
        String time = jobDate[1].substring(1, 6);
        int hours = Integer.parseInt(time.split(":")[0]);
        int minutes = Integer.parseInt(time.split(":")[1]);
        if (jobDate[0].equals("сегодня")) {
            LocalDateTime thisDay = LocalDateTime.now();
             day = thisDay.getDayOfMonth();
             month = thisDay.getMonth().getValue();
             year = thisDay.getYear();

        } else if (jobDate[0].equals("вчера")) {
            LocalDateTime localDate = LocalDateTime.now().minusDays(1);
            year = localDate.getYear();
            month = localDate.getMonthValue();
            day = localDate.getDayOfMonth();
        } else {
            String[] date = jobDate[0].split(" ");
            day = Integer.parseInt(date[0]);
            month = Integer.parseInt(MONTH.get(date[1]));
            year = Integer.parseInt("20" + date[2]);
        }
        return LocalDateTime.of(year, month, day, hours, minutes);
    }
}
