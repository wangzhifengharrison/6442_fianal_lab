import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class BasicPlayList implements PlayList {
	private ArrayList<Item> items = new ArrayList<Item>();

	@Override
	public void addSong(String name, String band, int time) {
		// TODO Auto-generated method stub
		Song song = new Song(name, band, time);
		items.add(song);

	}

	@Override
	public void addAdvertisment(String company, int time, int earn) {
		// TODO Auto-generated method stub
		Advertisement ad = new Advertisement(company, time, earn);
		items.add(ad);

	}

	@Override
	public String longestSong() {
		// TODO Auto-generated method stub
		String name = "";
		int time = 0;
		for(int i = 0; i < items.size(); i++){
			Item item = items.get(i);
			if(item.getType() == 0 && item.getTime() > time){
				time = item.getTime();
				Song s = (Song)item;
				name = s.getName();
			}
		}
		return name;
	}

	@Override
	public boolean tooManyAds() {
		// TODO Auto-generated method stub
		TimeQueue queue = new TimeQueue();
		for(int i = 0; i < items.size(); i++){
			Item item = items.get(i);
			for(int j = 0; j < item.getTime();j++){
				if(item.getType() == 0)
					queue.add(0);
				else
					queue.add(1);
				int adtime = queue.adTime();
				if (adtime > 720)
					return true;
			}
		}
		return false;
	}

}
