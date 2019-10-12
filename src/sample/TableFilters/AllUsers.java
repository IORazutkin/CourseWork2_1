package sample.TableFilters;

import java.time.LocalDate;

public class AllUsers {
    private String surname, name, patronymic;
    private LocalDate birthDay;
    private int room;
    public AllUsers(String surname, String name, String patronymic, LocalDate birthDay, int room) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.birthDay = birthDay;
        this.room = room;
    }
    public String getSurname() {
        return surname;
    }
    public String getName() {
        return name;
    }
    public String getPatronymic() {
        return patronymic;
    }
    public String getBirthDay() {
        return String.format("%02d.%02d.%d", birthDay.getDayOfMonth(), birthDay.getMonthValue(), birthDay.getYear());
    }
    public int getRoom() {
        return room;
    }
    public static LocalDate StringToBirthDay(String birthDay) {
        String[] elements = birthDay.split("\\-");
        return LocalDate.of(Integer.parseInt(elements[0]), Integer.parseInt(elements[1]), Integer.parseInt(elements[2]));
    }
    public static int StringToRoom(String room) {
        return Integer.parseInt(room);
    }
}