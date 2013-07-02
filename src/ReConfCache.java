import java.util.HashMap;


public class ReConfCache {

	private Cache cache;
	private int[] history;
	
	
	public ReConfCache() {
		this.cache = new Cache();
		//DM with 1 Word per Block
		this.cache.setMode("0000");
		this.history = new int[48];
		for (int i = 0; i < this.history.length; i++) {
			this.history[i] = -1;
		}
	}
	
	public int read(int address) {
		if (this.cache.contains(address)) {
			return this.cache.read(address);
		} else {
			for (int i = 0; i < this.history.length; i++) {
				if (this.history[i] == address) {
					// conflict miss
					int mode = Integer.valueOf(this.cache.getMode(), 2);
					if (mode < 8)
						mode += 4;
					this.cache.setMode(Integer.toBinaryString(mode));
					System.out.println("Changing mode to " + Integer.toBinaryString(mode));
					return this.cache.read(address);
				}
			}
			//not found so It is first hit miss or capacity miss
			int mode = Integer.valueOf(this.cache.getMode(), 2);
			if (mode > 3)
				mode -= 3;
			else if (mode < 3)
				mode++;
			String newMode = Integer.toBinaryString(mode);
			while (newMode.length() < 4)
				newMode = '0' + newMode;
			this.cache.setMode(newMode);
			System.out.println("Changing mode to " + newMode);
			return this.cache.read(address);
		}
	}
	
	public void write(int address, int data) {
		if (this.cache.contains(address)) {
			this.cache.write(address, data);
			return;
		} else {
			for (int i = 0; i < this.history.length; i++) {
				if (this.history[i] == address) {
					// conflict miss
					int mode = Integer.valueOf(this.cache.getMode(), 2);
					if (mode < 8)
						mode += 4;
					this.cache.setMode(Integer.toBinaryString(mode));
					System.out.println("Changing mode to " + Integer.toBinaryString(mode));
					this.cache.write(address, data);
					return;
				}
			}
			//not found so It is first hit miss or capacity miss
			int mode = Integer.valueOf(this.cache.getMode(), 2);
			if (mode > 3)
				mode -= 3;
			else if (mode < 3)
				mode++;
			String newMode = Integer.toBinaryString(mode);
			while (newMode.length() < 4)
				newMode = '0' + newMode;
			this.cache.setMode(newMode);
			System.out.println("Changing mode to " + newMode);
			this.cache.write(address,data);
			return;
		}
	}
	
	public int getHit() {
		return this.cache.getHit();
	}
	
	public int getMiss() {
		return this.cache.getMiss();
	}
	
	public String getMode() {
		return this.cache.getMode();
	}
	
	public HashMap<Integer, Double> getMissRates() {
		return this.cache.getMissRates();
	}
}
