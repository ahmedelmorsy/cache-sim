import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
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
		Cache cache = new Cache();
		cache.setMode("0100");

		BufferedReader in = new BufferedReader(new FileReader(new File(
				"COMP.prg")));
		String line = "";
		while ((line = in.readLine()) != null) {
			String[] splitted = line.split(" ");
			int op = Integer.parseInt(splitted[0]);
			int address = Integer.parseInt(splitted[1], 16);
			switch (op) {
			case 0:
//				System.out.println("instruction");
				cache.read(address);
				break;
			case 2:
//				System.out.println("read");
				cache.read(address);
				break;
			case 3:
//				System.out.println("write");
				cache.write(address, address);
				break;
			}
		}
		System.out
				.println("Miss Rate = "
						+ ((double) cache.getMiss() / (cache.getHit() + cache
								.getMiss())));
		in.close();
		cache = new Cache();
		cache.setMode("0000");

		in = new BufferedReader(new FileReader(new File(
				"COMP.prg")));
		line = "";
		while ((line = in.readLine()) != null) {
			String[] splitted = line.split(" ");
			int op = Integer.parseInt(splitted[0]);
			int address = Integer.parseInt(splitted[1], 16);
			switch (op) {
			case 0:
//				System.out.println("instruction");
				cache.read(address);
				break;
			case 2:
//				System.out.println("read");
				cache.read(address);
				break;
			case 3:
//				System.out.println("write");
				cache.write(address, address);
				break;
			}
		}
		System.out
				.println("Miss Rate = "
						+ ((double) cache.getMiss() / (cache.getHit() + cache
								.getMiss())));
		in.close();
	}
}
