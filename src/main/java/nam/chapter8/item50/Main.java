package nam.chapter8.item50;

import java.util.*;

public class Main {

    static class DayOfWorkByEmployee {

        Map<String, Set<Date>> map = new HashMap<>();

        void addWorkDate(String name, Date date) {
            Date dateCopied = new Date(date.getTime());
            if (map.containsKey(name)) {
                map.get(name).add(dateCopied);
            } else {
                HashSet<Date> workDates = new HashSet<>();
                workDates.add(dateCopied);
                map.put(name, workDates);
            }
        }

        Set<Date> getWorkDates(String name) {
            return new HashSet<>(map.getOrDefault(name, new HashSet<>()));
        }
    }

    public static void main(String[] args) {
        DayOfWorkByEmployee dayOfWorkByEmployee = new DayOfWorkByEmployee();

        // ndy log his work dates
        String ndy = "ndy";
        List<Date> ndyWorkDates = List.of(
                new Date(2024, 3, 25),
                new Date(2024, 3, 26),
                new Date(2024, 3, 27),
                new Date(2024, 3, 28),
                new Date(2024, 3, 29)
        );
        ndyWorkDates.forEach(it -> dayOfWorkByEmployee.addWorkDate(ndy, it));
        System.out.println("ndy work days : " + dayOfWorkByEmployee.getWorkDates(ndy));

        // ndy update his work dates
        ndyWorkDates.get(0).setDate(30);

        // pjp check ndy's work dates
        System.out.println("ndy work days : " + dayOfWorkByEmployee.getWorkDates(ndy));
    }
}
