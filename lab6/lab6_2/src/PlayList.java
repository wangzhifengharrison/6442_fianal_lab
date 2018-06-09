


public interface PlayList {
     void addSong(String name, String band, int time); // time in seconds 
     void addAdvertisment(String company, int time, int earn); // time in seconds and earn in dollars
     String longestSong();
     boolean tooManyAds();
}
