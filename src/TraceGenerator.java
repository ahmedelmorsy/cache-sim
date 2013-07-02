import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TraceGenerator {

	static final int DIM = 10; // Vector length
	static final int RD = 2; // It indicates Read
	static final int WR = 3; // It indicates Write

	// This function writes a memory access with the simulator format.
	// It needs the trace file, the memory access type, and the address
	public static void trace(PrintWriter file, int type, int addr) {
		file.write(type + " " + Integer.toHexString(addr) + "\n");
	}

	public static void main(String[] args) throws IOException {
		int[][] a = new int[DIM][DIM];
		int[][] b = new int[DIM][DIM];
//		int[][] c = new int[DIM][DIM];
		for (int i = 0; i < DIM; i++) {
			for (int j = 0; j < DIM; j++) {
				a[i][j] = i * DIM + j;
			}
		}
		for (int i = 0; i < DIM; i++) {
			for (int j = 0; j < DIM; j++) {
				b[i][j] = 2048 + i * DIM + j;
			}
		}
//		printMat(a);
//		printMat(b);
		PrintWriter out = new PrintWriter(new FileWriter(new File("mat" + DIM
				+ ".prg")));
		for (int i = 0; i < DIM; i++) {
			for (int j = 0; j < DIM; j++) {
				for (int k = 0; k < DIM; k++) {
					int addr = 2048 * 2048 + i * DIM + j;
//					c[i][j] += a[i][k] * b[k][j];
					trace(out, RD, a[i][k]);
					trace(out, RD, b[k][j]);
					trace(out, RD, addr);
					trace(out, WR, addr);
				}
			}
		}
//		printMat(c);
	}

//	private static void printMat(int[][] c) {
//		System.out.println("---------------------");
//		for (int i = 0; i < DIM; i++) {
//			for (int j = 0; j < DIM; j++) {
//				System.out.print(c[i][j] + " ");
//			}
//			System.out.println();
//		}
//	}

}
