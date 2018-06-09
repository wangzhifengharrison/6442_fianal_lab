public class Advertisement extends Item{
    private String company;
    private int earn;
    Advertisement(String company, int time, int earn) {
        super(1, time);
        this.company = company;
        this.earn = earn;
    }
}
