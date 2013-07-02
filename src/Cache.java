import java.util.ArrayList;
import java.util.HashMap;


public class Cache {

	public static final int MAX_SIZE = 2048;
	
	private static int clock = 0;
	
	private int[] data;
	private byte[] tags;
	private boolean[] validBits;
	private int[] lruBits;
	private int hit;
	private int miss;
	private ArrayList<Long> hitAT;
	private ArrayList<Long> missAT;
	private HashMap<Integer, Double> missRates;
	
	//Way Prediction
	private int[] mru;
	
	/*
	 * 0000 - Direct Mapped with 1 Word x Block
	 * 0001 - Direct Mapped with 2 Word x Block
	 * 0010 - Direct Mapped with 4 Word x Block
	 * 0011 - Direct Mapped with 8 Word x Block
	 * 0100 - 2 Way Set Associative with 1 Word x Block
	 * 0101 - 2 Way Set Associative with 2 Word x Block
	 * 0110 - 2 Way Set Associative with 4 Word x Block
	 * 0111 - 2 Way Set Associative with 8 Word x Block
	 * 1000 - 4 Way Set Associative with 1 Word x Block
	 * 1001 - 4 Way Set Associative with 2 Word x Block
	 * 1010 - 4 Way Set Associative with 4 Word x Block
	 * 1011 - 4 Way Set Associative with 8 Word x Block
	 */
	private String mode ;
	private int assoc;
	private int blockSize;
	
	public Cache() {
		init();
		this.mode = "0000";//Direct Mapped
		this.assoc = 1;
		this.hit = 0;
		this.miss = 0;
		this.mru = null;
		this.blockSize = 1;
		reset();
	}
	
	private void reset() {
		this.mru = new int[Cache.MAX_SIZE / this.assoc / this.blockSize];
		for (int j = 0; j < mru.length; j++) {
			this.mru[j] = -1;
		}
		this.tags = new byte[Cache.MAX_SIZE / this.blockSize];
		for (int j = 0; j < tags.length; j++) {
			this.tags[j] = -1;
		}
		this.lruBits = new int[Cache.MAX_SIZE / this.blockSize];
		for (int j = 0; j < lruBits.length; j++) {
			this.lruBits[j] = -1;
		}
		for (int i = 0; i < MAX_SIZE; i++) {
			this.data[i] = -1;
		}
	}

	private void init() {
		this.data = new int[Cache.MAX_SIZE];
		this.validBits = new boolean[Cache.MAX_SIZE];
		this.hitAT = new ArrayList<Long>();
		this.missAT = new ArrayList<Long>();
		this.missRates = new HashMap<Integer, Double>();
	}

	public void setMode(String mode) {
		this.mode = mode;
		if ("0000".equals(mode)) {
			//DM with 1 Word x Block
			if (this.assoc != 1 || this.blockSize != 1) {
				this.assoc = 1;
				this.blockSize = 1;
				this.reset();
			}
		} else if ("0001".equals(mode)) {
			//DM with 2 Words x Block
			if (this.assoc != 1 || this.blockSize != 2) {
				this.assoc = 1;
				this.blockSize = 2;
				this.reset();
			}
		} else if ("0010".equals(mode)) {
			//DM with 4 Words x Block
			if (this.assoc != 1 || this.blockSize != 4) {
				this.assoc = 1;
				this.blockSize = 4;
				this.reset();
			}
		} else if ("0011".equals(mode)) {
			//DM with 8 Words x Block
			if (this.assoc != 1 || this.blockSize != 8) {
				this.assoc = 1;
				this.blockSize = 8;
				this.reset();
			}
		} else if ("0100".equals(mode)) {
			//2 Way Set Associative with 1 Word x Block
			if (this.assoc != 2 || this.blockSize != 1) {
				this.assoc = 2;
				this.blockSize = 1;
				this.reset();
			}
		} else if ("0101".equals(mode)) {
			//2 Way Set Associative with 2 Words x Block
			if (this.assoc != 2 || this.blockSize != 2) {
				this.assoc = 2;
				this.blockSize = 2;
				this.reset();
			}
		} else if ("0110".equals(mode)) {
			//2 Way Set Associative with 4 Words x Block
			if (this.assoc != 2 || this.blockSize != 4) {
				this.assoc = 2;
				this.blockSize = 4;
				this.reset();
			}
		} else if ("0111".equals(mode)) {
			//2 Way Set Associative with 8 Words x Block
			if (this.assoc != 2 || this.blockSize != 8) {
				this.assoc = 2;
				this.blockSize = 8;
				this.reset();
			}
		} else if ("1000".equals(mode)) {
			//4 Way Set Associative with 1 Words x Block
			if (this.assoc != 4 || this.blockSize != 1) {
				this.assoc = 4;
				this.blockSize = 1;
				this.reset();
			}
		} else if ("1001".equals(mode)) {
			//4 Way Set Associative with 2 Words x Block
			if (this.assoc != 4 || this.blockSize != 2) {
				this.assoc = 4;
				this.blockSize = 2;
				this.reset();
			}
		} else if ("1010".equals(mode)) {
			//4 Way Set Associative with 4 Words x Block
			if (this.assoc != 4 || this.blockSize != 4) {
				this.assoc = 4;
				this.blockSize = 4;
				this.reset();
			}
		} else if ("1011".equals(mode)) {
			//4 Way Set Associative with 8 Words x Block
			if (this.assoc != 4 || this.blockSize != 8) {
				this.assoc = 4;
				this.blockSize = 8;
				this.reset();
			}
		} 
	}
	
	public String getMode() {
		return this.mode;
	}
	
	public int getHit() {
		return this.hit;
	}
	
	public int getMiss() {
		return this.miss;
	}
	
	public boolean contains(int address) {
		int sets = Cache.MAX_SIZE / this.assoc / this.blockSize;
		int blockNo = address / this.blockSize;
		int pos = blockNo % sets;
		byte tag = (byte) (blockNo / sets);
		//MRU Way Prediction
		if (this.mru[pos] >= 0 && tag == this.tags[pos+this.mru[pos]]) {
			return true; 
		}
		//
		for (int i = 0; i < this.assoc; i++) {
			if (tag == this.tags[pos+i]) {
				return true; 
			}
		}
		return false;
	}
	
	public int read(int address) {
		long before = System.nanoTime();
		int sets = Cache.MAX_SIZE / this.assoc / this.blockSize;
		int blockNo = address / this.blockSize;
		int pos = blockNo % sets;
		byte tag = (byte) (blockNo / sets);
		//MRU Way Prediction
		if (this.mru[pos] >= 0 && tag == this.tags[pos+this.mru[pos]]) {
			this.hit++;
			this.lruBits[pos+this.mru[pos]] = clock++;
			if ((this.hit + this.miss) % 500 == 0) {
				this.missRates.put(this.hit + this.miss, (double) this.miss / (this.hit + this.miss));
			}
			hitAT.add(System.nanoTime() - before);
			return this.data[this.blockSize * (pos + this.mru[pos]) + (address % this.blockSize)]; 
		}
		//
		for (int i = 0; i < this.assoc; i++) {
			if (tag == this.tags[pos+i]) {
				this.hit++;
				this.lruBits[pos+i] = clock++;
				this.mru[pos] = i;
				if ((this.hit + this.miss) % 500 == 0) {
					this.missRates.put(this.hit + this.miss, (double) this.miss / (this.hit + this.miss));
				}
				hitAT.add(System.nanoTime() - before);
				return this.data[this.blockSize * (pos + i) + (address % this.blockSize)]; 
			}
		}
		this.miss++;
		getFromMemory(address);
		missAT.add(System.nanoTime() - before);
		if ((this.hit + this.miss) % 500 == 0) {
			this.missRates.put(this.hit + this.miss, (double) this.miss / (this.hit + this.miss));
		}
		return address;
	}
	
	public HashMap<Integer, Double> getMissRates() {
		return this.missRates;
	}
	
	private void getFromMemory(int address) {
		int sets = Cache.MAX_SIZE / this.assoc / this.blockSize;
		int blockNo = address / this.blockSize;
		int pos = blockNo % sets;
		byte tag = (byte) (blockNo / sets);
		int min = Integer.MAX_VALUE;
		int minIndex = -1;
		for (int i = 0; i < this.assoc; i++) {
			if (data[this.blockSize * (pos + i)] == -1) {
				for (int j = 0; j < this.blockSize; j++) {
					this.data[this.blockSize * (pos+ i) + j] = blockNo * this.blockSize + j;
				}
				this.tags[pos+i] = tag;
				this.validBits[pos+i] = true;
				this.lruBits[pos+i] = clock++;
				this.mru[pos] = i;
				return;
			} else {
				if (this.lruBits[pos+i] < min) {
					min = this.lruBits[pos+i];
					minIndex = i;
				}
			}
		}
		for (int j = 0; j < this.blockSize; j++) {
			this.data[this.blockSize * (pos+ minIndex) + j] = address;//check this
		}
		this.tags[pos+minIndex] = tag;
		this.validBits[pos+minIndex] = true;
		this.lruBits[pos+minIndex] = clock++;
		this.mru[pos] = minIndex;
		return;
	}

	public void write(int address, int data) {
		int sets = Cache.MAX_SIZE / this.assoc / this.blockSize;
		int blockNo = address / this.blockSize;
		int pos = blockNo % sets;
		byte tag = (byte) (address / sets);
		//MRU Way Prediction
		if (this.mru[pos] >= 0 && tag == this.tags[pos+this.mru[pos]]) {
			this.hit++;
			this.lruBits[pos+this.mru[pos]] = clock++;
			this.data[this.blockSize * (pos + this.mru[pos]) + (address % this.blockSize)] = data;
			if ((this.hit + this.miss) % 500 == 0) {
				this.missRates.put(this.hit + this.miss, (double) this.miss / (this.hit + this.miss));
			}
			return; 
		}
		//
		for (int i = 0; i < this.assoc; i++) {
			if (tag == this.tags[pos+i]) {
				this.hit++;
				this.lruBits[pos+i] = clock++;
				this.data[this.blockSize * (pos + i) + (address % this.blockSize)] = data;
				this.validBits[pos+i] = false;
				if ((this.hit + this.miss) % 500 == 0) {
					this.missRates.put(this.hit + this.miss, (double) this.miss / (this.hit + this.miss));
				}
				return;
			}
		}
		this.miss++;
		getFromMemory(address);
		for (int i = 0; i < this.assoc; i++) {
			if (tag == this.tags[pos+i]) {
				this.lruBits[pos+i] = clock++;
				this.data[this.blockSize * (pos + i) + (address % this.blockSize)] = data;
				this.validBits[pos+i] = false;
			}
		}
		if ((this.hit + this.miss) % 500 == 0) {
			this.missRates.put(this.hit + this.miss, (double) this.miss / (this.hit + this.miss));
		}
		return;
	}
}
