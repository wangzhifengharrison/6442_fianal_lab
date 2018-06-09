import java.util.ArrayList;

public class Song extends Item{
    private String name;
    private String band;
    Song(String name, String band, int time) {
        super(0, time);
        this.band = band;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }
}
