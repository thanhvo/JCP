package jcip.log;

import java.io.*;
import java.math.*;
import java.security.*;

public class LogServiceTest {
	public static void main(String[] args) {		
		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter printer = null;
		try {
			fw = new FileWriter("log.txt", true); 
			bw = new BufferedWriter(fw);
			printer = new PrintWriter(bw);
			LogService logger = new LogService(printer);
			SecureRandom random = new SecureRandom();
			logger.start();
			while(true) {
				logger.log(new BigInteger(130, random).toString(32));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (Exception ex) {}
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (Exception ex) {}
			try {
				if (printer != null) {
					printer.close();
				}
			} catch (Exception ex) {}
		}
	}
}
