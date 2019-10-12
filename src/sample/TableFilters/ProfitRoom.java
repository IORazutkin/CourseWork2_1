package sample.TableFilters;

public class ProfitRoom {
    private int room, users;
    private double net, average;

    public ProfitRoom(int room, double net) {
        this.room = room;
        this.users = 1;
        this.net = net;
        this.average = net;
    }
    public int getRoom() {
        return room;
    }
    public int getUsers() {
        return users;
    }
    public double getNet() {
        return Math.round(net * 100) / 100.0;
    }
    public double getAverage() {
        return Math.round(average * 100) / 100.0;
    }
    public void addUser(double net) {
        this.users++;
        this.net += net;
        this.average = this.net / this.users;
    }

}
