package sample.Classes;

import java.time.LocalDate;
import java.time.Period;

public class User {
    private String fio;
    private LocalDate birthDay;
    private int room;
    private String telephone;
    private String mail;
    private String login;
    private String password;

    /**
     * Консруктор от строки
     * Позволяет создавать экземпляр класса из строки файла
     */

    public User(String line) {
        String[] atributes = line.split(" ");
        String[] birthDay = atributes[6].split("\\-");
        login = atributes[1];
        password = atributes[2];
        fio = String.format("%s %s %s", atributes[3], atributes[4], atributes[5]);
        this.birthDay = LocalDate.of(Integer.parseInt(birthDay[0]),
                                     Integer.parseInt(birthDay[1]),
                                     Integer.parseInt(birthDay[2]));
        room = Integer.parseInt(atributes[7]);
        telephone = atributes[8];
        mail = atributes[9];
    }

    /**
     * В зависимости от возраста, ставит слово «Год» в нужной форме
     */

    public String getAge() {
        int age = Period.between(birthDay, LocalDate.now()).getYears();
        if (age % 100 < 10 || age % 100 > 20) {
            if (age % 10 == 1) return age + " год";
            else if (age % 10 < 5) return age + " года";
        }
        return age + " лет";
    }

    /**
     * Возвращает информацию о пользователе в нужном формате
     */

    public String getUserInfo() {
        return String.format("%s, %s, кв. %s", fio, getAge(), room);
    }
    public String getLogin() { return login; }
}
