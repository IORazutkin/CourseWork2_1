package sample.Classes;

public class Admin {
    private String fio;
    private String login;
    private String password;

    /**
     * Консруктор от строки
     * Позволяет создавать экземпляр класса из строки файла
     */

    public Admin(String line) {
        String[] atributes = line.split(" ");
        login = atributes[1];
        password = atributes[2];
        fio = String.format("%s %s %s", atributes[3], atributes[4], atributes[5]);
    }

    public String getFio() { return fio; }
}
