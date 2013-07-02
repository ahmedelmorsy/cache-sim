import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) throws IOException {

		String[] modes = new String[] { "0000", "0001", "0010", "0011", "0100",
				"0101", "0110", "0111" };

		// Cache dataCache = new Cache();
		// Cache instructionCache = new Cache();
		//
		// BufferedReader in = new BufferedReader(new FileReader(new File(
		// "COMP.prg")));
		// String line = "";
		// while ((line = in.readLine()) != null) {
		// String[] splitted = line.split(" ");
		// int op = Integer.parseInt(splitted[0]);
		// int address = Integer.parseInt(splitted[1], 16);
		// switch (op) {
		// case 0:
		// System.out.println("instruction");
		// instructionCache.read(address);
		// break;
		// case 2:
		// System.out.println("read");
		// dataCache.read(address);
		// break;
		// case 3:
		// System.out.println("write");
		// dataCache.write(address, address);
		// break;
		// }
		// }
		// System.out
		// .println("Instruction Hit Rate = "
		// + ((double)instructionCache.getHit() / (instructionCache
		// .getHit() + instructionCache.getMiss())));
		// System.out
		// .println("Data Hit Rate = "
		// + ((double)dataCache.getHit() / (dataCache
		// .getHit() + dataCache.getMiss())));
		// in.close();
		PrintWriter out = new PrintWriter(new FileWriter(new File("result.csv")));
		for (int i = 0; i < modes.length; i++) {
			Cache cache = new Cache();
			cache.setMode(modes[i]);

			BufferedReader in = new BufferedReader(new FileReader(new File(
					"EAR.prg")));
			String line = "";
			while ((line = in.readLine()) != null) {
				String[] splitted = line.split(" ");
				int op = Integer.parseInt(splitted[0]);
				int address = Integer.parseInt(splitted[1], 16);
				switch (op) {
				case 0:
					// System.out.println("instruction");
					cache.read(address);
					break;
				case 2:
					// System.out.println("read");
					cache.read(address);
					break;
				case 3:
					// System.out.println("write");
					cache.write(address, address);
					break;
				}
			}
//			System.out.println("Miss Rate (" + modes[i] + ") = "
//					+ ((double) cache.getMiss() / (cache.getHit() + cache
//							.getMiss())));
			HashMap<Integer, Double> map = cache.getMissRates();
			int count = 500;
			while (map.containsKey(count)) {
				out.write(cache.getMode() + "," + count + "," + map.get(count) + "\n");
				count += 500;
			}
			in.close();
		}
		ReConfCache cache = new ReConfCache();
		BufferedReader in = new BufferedReader(new FileReader(new File(
				"EAR.prg")));
		String line = "";
		while ((line = in.readLine()) != null) {
			String[] splitted = line.split(" ");
			int op = Integer.parseInt(splitted[0]);
			int address = Integer.parseInt(splitted[1], 16);
			switch (op) {
			case 0:
				// System.out.println("instruction");
				cache.read(address);
				break;
			case 2:
				// System.out.println("read");
				cache.read(address);
				break;
			case 3:
				// System.out.println("write");
				cache.write(address, address);
				break;
			}
		}
//		System.out.println("Miss Rate (" + modes[i] + ") = "
//				+ ((double) cache.getMiss() / (cache.getHit() + cache
//						.getMiss())));
		HashMap<Integer, Double> map = cache.getMissRates();
		int count = 500;
		while (map.containsKey(count)) {
			out.write("reconf," + count + "," + map.get(count) + "\n");
			count += 500;
		}
		in.close();
		out.close();
	}
}
