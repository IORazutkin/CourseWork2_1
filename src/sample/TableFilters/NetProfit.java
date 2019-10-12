package sample.TableFilters;

public class NetProfit {
    private String surname, name, patronymic;
    private double net;

    public NetProfit(String surname, String name, String patronymic, double net) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.net = net;
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
    public double getNet() {
        return Math.round(net * 100) / 100.0;
    }
}
