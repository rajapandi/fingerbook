package client;

import java.io.File;

import org.junit.Test;

import com.fingerbook.client.FileHashCalculator;

public class FileHashCalculatorTest extends junit.framework.TestCase {
	@Test
	public void testGetFileHash() {
		FileHashCalculator hash = new FileHashCalculator("SHA1");
		assertEquals("534c4ef52100ab2fd6546bec32f4187a0368a4ca", hash
				.getFileHash(new File("./src/test/resources/example.txt")));
	}
}
