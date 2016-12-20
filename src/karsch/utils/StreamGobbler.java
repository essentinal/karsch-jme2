package karsch.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;

public class StreamGobbler extends Thread {
	public static final int TYPE_OUTPUT = 0;
	public static final int TYPE_ERROR = 1;
	private InputStream is;
	private int type;
	private boolean log;
	private static final String logfile_out = "karsch_out.log";
	private static final String logfile_err = "karsch_err.log";

	public StreamGobbler(InputStream is, int type, boolean log) {
		this.is = is;
		this.type = type;
		this.log = log;
	}

	public void run() {
		InputStreamReader isr =null;
		BufferedReader br = null;
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line = null;
			
			if (log){
				try {
					if (type == StreamGobbler.TYPE_OUTPUT){
						fos = new FileOutputStream(new File(logfile_out));
					} else {
						fos = new FileOutputStream(new File(logfile_err));
					}
					bw = new BufferedWriter(new PrintWriter(fos));
					bw.write("Karsch log file \ncreated on " + new Date().toString() + "\n\n");
					bw.close();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
					log = false;
				}
			}
			
			while ((line = br.readLine()) != null) {
				if (type == StreamGobbler.TYPE_OUTPUT) {
					System.out.println(line);
				} else if (type == StreamGobbler.TYPE_ERROR) {
					System.err.println(line);
				}
				if (log){
					if (type == StreamGobbler.TYPE_OUTPUT){
						fos = new FileOutputStream(new File(logfile_out),true);
					} else {
						fos = new FileOutputStream(new File(logfile_err),true);
					}
					bw = new BufferedWriter(new PrintWriter(fos));
					// write the line into logfile
					bw.write(line + "\n");
					bw.close();
					fos.close();
				}
			}
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fos != null)
					fos.close();
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
				if (is != null)
					is.close();
			} catch (Exception e) {
				// doesnt matter
			}
		}
	}
}