package test;

import huffman.Hzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import junit.framework.TestCase;
import junit.*;

public class HzipTest extends TestCase {
	
	private final static String FILE = "TestA.dat";
	
	@Test
	public void decompressedIsOriginal() throws IOException{
		String fileText;
		Scanner str = new Scanner(new File(FILE));
		fileText = str.nextLine();
		str.close();
		
		Hzip.compress(FILE);
		Hzip.uncompress(FILE+".huf");
		
		String uncompressedFileText;
		str = new Scanner(new File(FILE+".uc"));
		uncompressedFileText = str.nextLine();
		str.close();
		
		this.assertEquals(fileText, uncompressedFileText);
	}
}
