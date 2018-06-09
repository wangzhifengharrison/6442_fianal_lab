import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class Test1 {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    @Test
    public void testLongestName() {
        boolean flag = false;
        PlayList playlist = new BasicPlayList();
        playlist.addSong("Vertigo","U2",210);
        playlist.addSong("Bad","Michael Jackson",230);
        playlist.addAdvertisment("AGL",30,320);
        playlist.addAdvertisment("Holden",20,210);
        playlist.addSong("Little Lamb","Mary",210);
        if(playlist.longestSong().equals("Bad"))
            flag = true;
        assertTrue("Name is not right",flag);


    }

    @Test
    public void testAdTime() {
        boolean flag = false;
        PlayList playlist = new BasicPlayList();
        playlist.addSong("Vertigo","U2",210);
        playlist.addSong("Bad","Michael Jackson",230);
        playlist.addAdvertisment("AGL",30,320);
        playlist.addAdvertisment("Holden",20,210);
        playlist.addSong("Little Lamb","Mary",210);
        if(playlist.tooManyAds() == false)
            flag = true;
        assertTrue("Name is not right",flag);


    }

}