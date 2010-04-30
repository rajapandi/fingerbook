package tests;

import java.io.File;

import org.junit.Test;

import com.fingerbook.client.FileHashCalculator;

public class FileHashCalculatorTest extends junit.framework.TestCase {
	@Test
	public void testGetFileHash() {
		FileHashCalculator hash = new FileHashCalculator("SHA1");
		assertEquals("4a7f374ddf17eda1ba104ff0f1937fa67ffeab7e", hash
				.getFileHash(new File("./pom.xml")));
	}
}
