import java.io.*;
import java.util.*;

public class chkpro {
	
	public static final int _MAXDIFF_ = 500;
	public static final int _MAXLINES_ = 5000;

	public static void main(String args[]) {
		String UTORid = null, project = null, inputFile = null;
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		
		// print instructions
		printInstructions();
		
		// get specs from user
		try {
			System.out.print("Enter your UTORid: ");
			UTORid = "kwanale1";
			System.out.print("Enter project number: ");
			project = "5";
			System.out.print("Enter input file: ");
			inputFile = cin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// verify input file exists
		verifyFile(inputFile);
		
		// run student program
		String studentExec = getStudentExec(project, UTORid);
		String studentOutputFile = getOutputFile(studentExec);
		runProgram(studentExec, inputFile, studentOutputFile);
		
		// run solution program
		String solnExec = getSolnExec(project);
		String solnOutputFile = getOutputFile(solnExec);
		runProgram(solnExec, inputFile, solnOutputFile);
		
		// make comparison
		compareOutput(studentOutputFile, solnOutputFile);
		
	} // end main
	
	
	// compare two files and print lines with differences
	public static void compareOutput(String studentOutputFile, String solnOutputFile) {
		int nDiff = 0, nLines = 0;
		boolean maxReached = false;
		String studLine = null, solnLine = null;
		
		System.out.println("\n");
		
		try {

			BufferedReader brStud = new BufferedReader(new FileReader(studentOutputFile));
			BufferedReader brSoln = new BufferedReader(new FileReader(solnOutputFile));
			
			// examine up to _MAXLINES_ lines in soln code and print up to _MAXDIFF_ differences
			while ((solnLine = brSoln.readLine()) != null && !maxReached) {
				studLine = brStud.readLine();
				
				if (!solnLine.equals(studLine)) {
					nDiff++;
					System.out.println("-------------------");
					System.out.println("DIFFERENCE " + nDiff);
					System.out.println("-------------------");
					System.out.println(solnLine + "\n" + studLine);
					System.out.println("-------------------\n");
				}
				
				nLines++;
				maxReached = (nDiff >= _MAXDIFF_) || (nLines >= _MAXLINES_);
			}
			
			brSoln.close();
			brStud.close();
			
			if (nDiff == 0) {
				System.out.println("No differences found.\n");
			}
			else {
				System.out.println(nDiff + " differences found.\n");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	} // end compareOutput
		
	
	// EQUIVALENT OF "java myprog < input.txt > output.txt"
	public static void runProgram(String executable, String inputFile, String outputFile) {
		
		// verify the file exists
		verifyFile(executable);
		
		try {
			
			// ProcessBuilder to run external program
			ProcessBuilder pb;
			if (executable.contains("jar")) {
				pb = new ProcessBuilder("java", "-jar", executable);
			}
			else {
				executable = executable.replaceAll(".class", "");
				pb = new ProcessBuilder("java", executable);
			}

			pb.redirectErrorStream(true); 
			Process p = pb.start();

			// writer to feed input into progam
			final PrintWriter pw = new PrintWriter(p.getOutputStream());
			
			// reader to get content from input file
			final BufferedReader fileBr = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));

			// reader to get output from running program
			final BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			// writer to write program output to file
			final BufferedWriter fOut = new BufferedWriter(new FileWriter(outputFile));
			
			// thread to feed input file contents into program
			Thread tIn = new Thread(new Runnable() {
				public void run() {
					try {
						String lineRead;
						while ((lineRead = fileBr.readLine()) != null) {
							pw.println(lineRead);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (pw != null) {
							pw.close();
						}
						if (fileBr != null) {
							try {
								fileBr.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} // end read input file
				} // end thread action
			});
			tIn.start();
			

			// thread to read program output and write it to a file
			Thread tOut = new Thread(new Runnable() {
				public void run() {
					try {
						String lineRead;
						while ((lineRead = br.readLine()) != null) {
							fOut.write(lineRead);
							fOut.newLine();
						}
						fOut.flush();

					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (br != null) {
							try {
								br.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (fOut != null) {
							try {
								fOut.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} // end read program output
				} // end thread action
			});
			tOut.start();
			
			
			// wait for threads to finish, otherwise output files can't be opened later
			try {
				tIn.join();
				tOut.join();
			} catch (InterruptedException e) {
				System.out.println("Thread interrupted!");
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	} // end runProgram
	
	
	// bail if a file doesn't exist
	public static void verifyFile(String file) {
		File f = new File(file);
		if (!f.exists()) {
			System.out.println("\nERROR: Cannot find file " + file + "!\n");
			System.exit(1);
		}
	}
	
	
	// basic interpretation instructions
	public static void printInstructions() {
		System.out.println("\nchk_pro examines up to " + _MAXLINES_ + " lines of code in the " +
						   "solution output,\nand then displays up to " + _MAXDIFF_ +
						   " differences with your program output.\nIn the differences shown, " +
						   "the solution output is on the top, and\nyour output is on the bottom.\n\n" +
						   "chk_pro expects that your .class file, the solution .jar file, and\nthe input file " +
						   "are all in the same directory as chkpro.class.\n\n" +
						   "Remember to verify that your program works with the online chk_pro, \nas this " +
						   "offline chk_pro is more robust to BufferedReaderimplementations\nthan the " +
						   "online chk_pro, which is used in actual project grading.\n\n");
	}
	
	
	// get name of student executable
	public static String getStudentExec(String project, String UTORid) {
		return "Pro" + project + "_" + UTORid + ".class";
	}
	
	
	// get name of solution executable
	public static String getSolnExec(String project) {
		return "Pro" + project + "s.jar";
	}
	
	
	// get name of output file based on executable name
	public static String getOutputFile(String exec) {
		return exec.substring(0, exec.indexOf(".")) + "_output.txt";
	}

}