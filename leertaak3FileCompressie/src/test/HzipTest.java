package test;

import huffman.Hzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import junit.framework.TestCase;

public class HzipTest extends TestCase {
	
	//fuck eclipse and files, sorry iedereen met een tere ziel, maar fuck. 
	private final static String FILE = "C:/Users/Jan-Bert/Documents/GitHub/2.3-software-engineering/leertaak3FileCompressie/src/test/TestA.dat";
	
	public void testDecompressedIsOriginal() throws IOException{
		String fileText;
		Scanner str = new Scanner(new File(FILE));
		fileText = str.nextLine();
		str.close();
		
		Hzip.compress(FILE);
		Hzip.uncompress(FILE+".huf");
		
		String uncompressedFileText;
		Scanner str1 = new Scanner(new File(FILE+".uc"));
		uncompressedFileText = str1.nextLine();
		str1.close();
		
		assertEquals(fileText, uncompressedFileText);
	}
}
