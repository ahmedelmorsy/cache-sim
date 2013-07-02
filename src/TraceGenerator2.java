import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TraceGenerator2 {

	static final int DIM = 10; // Vector length
	static final int RD = 2; // It indicates Read
	static final int WR = 3; // It indicates Write

	// This function writes a memory access with the simulator format.
	// It needs the trace file, the memory access type, and the address
	public static void trace(PrintWriter file, int type, int addr) {
		file.write(type + " " + Integer.toHexString(addr) + "\n");
	}

	public static void main(String[] args) throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(new File("2048cons.prg")));
		for (int i = 0; i < 2048; i++) {
			trace(out, RD, i);
		}
	}

}
