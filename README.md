# Jerald Edric D. Ang
student from CMPILER S12

# Final Exam Test Case 
This project aims to create my own recognizer for regular expression inputs, using a top-down parser generator. For this, I would need to read a grammar that is right-recursive and left-factored. Moreover, this program requires two processes, namely creating the lexical analyzer and creating the syntax analyzer. The LL (1) parsing is followed, and no semantic analyzer is needed. For error recovery, the program will read error inputs as invalid, making use of the panic mode error recovery.

# Running the program
The program created is based on the Java programming language. The program must be run using a compatible IDE. <br>

The <b>steps</b> to run the program are as follows:
1. Download the repository onto a computing device. 
2. Open the project in a compatible Java IDE (recommended: IntelliJ).
3. Check compatibility of JDK and JRE files. 
4. Run the program in the main class once the IDE fully loads.
5. Check the "output.txt" text file for the results. 
<br>
<b>The entry class file (where the main function is located) is named:</b><br>
"Main" in src folder -> Main.java file <br>

<b>To change the input:</b><br>
The program reads a file called "input.txt" located in the main directory. Change the contents of this file to process a new input. The program also reads a grammar input, named "grammar.txt". To check the output of the file, the "output.txt" in the same directory stores the results computed by the program. In code, these are found in the Main.java file.

line 16 - "grammar.txt" 
line 33 - "input.txt"
line 86 - "output.txt"