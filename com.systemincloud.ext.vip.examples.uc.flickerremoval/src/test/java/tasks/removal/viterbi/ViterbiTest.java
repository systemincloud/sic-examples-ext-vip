package tasks.removal.viterbi;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.javatuples.Pair;
import org.junit.Test;

import tasks.removal.Viterbi;

public class ViterbiTest {

	private Pair<int[], Integer> loadSquareTableInt(String fileName) {
		List<Integer> outValues = new LinkedList<>();
		Integer size = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(ViterbiTest.class.getResourceAsStream(fileName)));
			String line;
			while((line = in.readLine()) != null) {
				String[] values = line.split("\\s+");
	        	for(String sv : values) 
	        		if(!"".equals(sv))
	        			outValues.add(Integer.parseInt(sv));
	        	if(size == null) size = outValues.size();
			}
        	in.close();
        } catch(IOException e) {}

		int[] out = new int[outValues.size()];
		for(int i = 0; i < outValues.size(); i++) out[i] = outValues.get(i);
		
		return new Pair<int[], Integer>(out, size);
	}
	
	private Pair<long[], Integer> loadSquareTableLong(String fileName) {
		List<Long> outValues = new LinkedList<>();
		Integer size = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(ViterbiTest.class.getResourceAsStream(fileName)));
			String line;
			while((line = in.readLine()) != null) {
				String[] values = line.split("\\s+");
	        	for(String sv : values)
	        		if(!"".equals(sv))
	        			outValues.add(Long.parseLong(sv));
	        	if(size == null) size = outValues.size();
			}
        	in.close();
        } catch(IOException e) {}

		long[] out = new long[outValues.size()];
		for(int i = 0; i < outValues.size(); i++) out[i] = outValues.get(i);
		
		return new Pair<long[], Integer>(out, size);
	}
	
	private boolean compareTables(long[] left, long[] right) {
		if(left.length != right.length) return false;
		for(int i = 0; i < left.length; i++)
			if(left[i] != right[i]) return false;
		return true;
		
	}
	
	public static void print(int[] values) {
		for(int i = 0; i < values.length; i++)
			System.out.print(values[i] + " ");
	}
	
	public static void print(long[] values, Integer size) {
		for(int i = 0; i < values.length; i++) {
			if(i%size == 0) System.out.print("\n");
			System.out.print(values[i] + " ");
		}
			
	}
	
	private boolean compareTables(int[] left, int[] right) {
		if(left.length != right.length) return false;
		for(int i = 0; i < left.length; i++)
			if(left[i] != right[i]) return false;
		return true;
	}
	
	@Test
	public void testCreateTempTest1() {
		Viterbi v = new Viterbi();
		
		Pair<int[],  Integer> in  = loadSquareTableInt("simpleIn");
		Pair<long[], Integer> tbl = loadSquareTableLong("simpleTbl");
		long[] result = v.createTempTable(in.getValue0(), in.getValue1());
//		print(result, in.getValue1());
		assertTrue(compareTables(result, tbl.getValue0()));
	}
	
	@Test
	public void testCreateTempTest2() {
		Viterbi v = new Viterbi();
		
		Pair<int[],  Integer> in  = loadSquareTableInt("spaceIn");
		Pair<long[], Integer> tbl = loadSquareTableLong("spaceTbl");
		long[] result = v.createTempTable(in.getValue0(), in.getValue1());
//		print(result, in.getValue1());
		assertTrue(compareTables(result, tbl.getValue0()));
	}
	
	@Test
	public void testCreateTempTest3() {
		Viterbi v = new Viterbi();
		
		Pair<int[],  Integer> in  = loadSquareTableInt("twoPathsIn");
		Pair<long[], Integer> tbl = loadSquareTableLong("twoPathsTbl");
		long[] result = v.createTempTable(in.getValue0(), in.getValue1());
//		print(result, in.getValue1());
		assertTrue(compareTables(result, tbl.getValue0()));
	}
	
	@Test
	public void testCreateTempTest4() {
		Viterbi v = new Viterbi();
		
		Pair<int[],  Integer> in  = loadSquareTableInt("borderIn");
		Pair<long[], Integer> tbl = loadSquareTableLong("borderTbl");
		long[] result = v.createTempTable(in.getValue0(), in.getValue1());
//		print(result, in.getValue1());
		assertTrue(compareTables(result, tbl.getValue0()));
	}
	
	@Test
	public void testCreateTempTest5() {
		Viterbi v = new Viterbi();
		
		Pair<int[],  Integer> in  = loadSquareTableInt("border2In");
		Pair<long[], Integer> tbl = loadSquareTableLong("border2Tbl");
		long[] result = v.createTempTable(in.getValue0(), in.getValue1());
//		print(result, in.getValue1());
		assertTrue(compareTables(result, tbl.getValue0()));
	}
	
	@Test
	public void testCreateTempTest6() {
		Viterbi v = new Viterbi();
		
		Pair<int[],  Integer> in  = loadSquareTableInt("verticalIn");
		Pair<long[], Integer> tbl = loadSquareTableLong("verticalTbl");
		long[] result = v.createTempTable(in.getValue0(), in.getValue1());
		print(result, in.getValue1());
		assertTrue(compareTables(result, tbl.getValue0()));
	}

	@Test
	public void testCreateOutTest1() {
		Viterbi v = new Viterbi();
		
		Pair<long[], Integer> tbl = loadSquareTableLong("simpleTbl");
		Pair<int[],  Integer> out = loadSquareTableInt ("simpleOut");
		int[] result = v.generateF(tbl.getValue0(), tbl.getValue1());
//		print(result);
		assertTrue(compareTables(result, out.getValue0()));
	}
	
	@Test
	public void testCreateOutTest2() {
		Viterbi v = new Viterbi();
		
		Pair<long[], Integer> tbl = loadSquareTableLong("spaceTbl");
		Pair<int[],  Integer> out = loadSquareTableInt ("spaceOut");
		int[] result = v.generateF(tbl.getValue0(), tbl.getValue1());
//		print(result);
		assertTrue(compareTables(result, out.getValue0()));
	}
	
	@Test
	public void testCreateOutTest3() {
		Viterbi v = new Viterbi();
		
		Pair<long[], Integer> tbl = loadSquareTableLong("twoPathsTbl");
		Pair<int[],  Integer> out = loadSquareTableInt ("twoPathsOut");
		int[] result = v.generateF(tbl.getValue0(), tbl.getValue1());
//		print(result);
		assertTrue(compareTables(result, out.getValue0()));
	}
	
	@Test
	public void testCreateOutTest4() {
		Viterbi v = new Viterbi();
		
		Pair<long[], Integer> tbl = loadSquareTableLong("borderTbl");
		Pair<int[],  Integer> out = loadSquareTableInt ("borderOut");
		int[] result = v.generateF(tbl.getValue0(), tbl.getValue1());
//		print(result);
		assertTrue(compareTables(result, out.getValue0()));
	}
	
	@Test
	public void testCreateOutTest5() {
		Viterbi v = new Viterbi();
		
		Pair<long[], Integer> tbl = loadSquareTableLong("border2Tbl");
		Pair<int[],  Integer> out = loadSquareTableInt ("border2Out");
		int[] result = v.generateF(tbl.getValue0(), tbl.getValue1());
//		print(result);
		assertTrue(compareTables(result, out.getValue0()));
	}
	
	@Test
	public void testCreateOutTest6() {
		Viterbi v = new Viterbi();
		
		Pair<long[], Integer> tbl = loadSquareTableLong("verticalTbl");
		Pair<int[],  Integer> out = loadSquareTableInt ("verticalOut");
		int[] result = v.generateF(tbl.getValue0(), tbl.getValue1());
		print(result);
		assertTrue(compareTables(result, out.getValue0()));
	}
}
