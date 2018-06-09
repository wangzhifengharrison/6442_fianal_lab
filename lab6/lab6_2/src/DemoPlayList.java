
public class DemoPlayList {

	public static void main(String[] args) {
		PlayList playlist = new BasicPlayList();
		playlist.addSong("Vertigo","U2",210);
		playlist.addSong("Bad","Michael Jackson",230);
		playlist.addAdvertisment("AGL",30,320);
		playlist.addAdvertisment("Holden",20,210);
		playlist.addSong("Little Lamb","Mary",210);
		
		System.out.println("The longest song is : " + playlist.longestSong());
		System.out.println("There " + (playlist.tooManyAds()?"was":"was not") + " too many ads in any one hour.");
		
	}

}
