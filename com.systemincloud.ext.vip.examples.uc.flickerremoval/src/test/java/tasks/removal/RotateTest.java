package tasks.removal;

import static org.junit.Assert.*;

import org.junit.Test;

public class RotateTest {

	@Test
	public void testRotate1() {
		Rotate r = new Rotate();
		r.cosPhi = Math.cos(Math.toRadians(-45));
		r.sinPhi = Math.sin(Math.toRadians(-45));
		r.domain = 14;

		int[] out = r.rotate(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 });
		assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, out);
	}

	@Test
	public void testRotate2() {
		Rotate r = new Rotate();
		r.cosPhi = Math.cos(Math.toRadians(-45));
		r.sinPhi = Math.sin(Math.toRadians(-45));
		r.domain = 14;

		int[] out = r.rotate(new int[] { 0, 0, 0, 5, 5, 5, 0, 0, 0, 9 });
		assertArrayEquals(new int[] { 0, -1, -2, -3, -4, -5, 0, 0, 0, 0, 0, 0, 0, 0 }, out);
	}
}
