import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class Parser {
    static enum coms {clear,cd,ls,cp,mv,rm,mkdir,rmdir,cat,more,pwd,date,help,kimo}
    private static Vector <String> args;
    private static String cmd;

    private static int Parse(String com) throws IOException {
        //to parse first to split using "|" then split using spaces and we will not accept the spaces
        com = com.strip();
        com = com.replace('\'', '"');
        com = com.replace('|', '#');
        if (com.length() == 0) {
            return -1;
        }
        if ((com.charAt(0) == '"') && (com.charAt(com.length() - 1) != '"'))
            System.out.println("Error Parsing the command");
        else if ((com.charAt(0) == '"') && (com.charAt(com.length() - 1) == '"')) {
            com = com.substring(1, com.length() - 1); //delete the quotes begin and end
            com = com.strip();
        }
        if (com.indexOf("more") > 0) {
            Terminal.flag = true;
        } //will be checked in the other coms
        int comNum = -1; //comNum is set to -1 to be returned if we didn't find the com
        String[] Instructions = com.split("#");//replaced with # so we can split
        for (int i = 0; i < Instructions.length; i++) {
            args = new Vector <>();
            Instructions[i] = Instructions[i].strip();
            if (com.indexOf(">>") > 0) {
                Terminal.toFile = 1;
            } else if (com.indexOf(">") > 0) {
                {
                    Terminal.toFile = 2;
                }
            }
            //to remove white spaces of entire commands
            String Values[] = Instructions[i].split(" ");
            for (int j = 0; j < Values.length; j++) {
                if (j == 0) {
                    cmd = Values[0]; //first word will be the command
                }
                else {
                    args.add(Values[j]);
                }
                try {
                    comNum = coms.valueOf(cmd).ordinal();//get the number of command to be executed from the enum
                }
                catch (IllegalArgumentException e) {//Catch if the command isn't in our list
                    run(-1);
                    return -1;
                }
                
            }
            if (Terminal.toFile > 0) Terminal.File = args.lastElement();
            run(comNum);
        }
        return comNum;
    }
    
    public static void run(int i) throws IOException {
        switch (i) {
            case -1:
                
                System.out.println( "There is no such command" );
                break;
            case 0:
                Terminal.clear();
                break;
            case 1:
                Terminal.cd(args.size() > 0 ? args.elementAt(0) : "");
                break;
            case 2:
                Terminal.ls();
                break;
            case 3:
                Terminal.cp(args.size() > 0 ? args.elementAt(0) : "", args.size() > 1 ? args.elementAt(1) : "");
                break;
            case 4:
                Terminal.mv(args.size() > 0 ? args.elementAt(0) : "", args.size() > 1 ? args.elementAt(1) : "");
                break;
            case 5:
                Terminal.rm(args.size() > 0 ? args.elementAt(0) : "");
                break;
            case 6:
                Terminal.mkdir(args.size() > 0 ? args.elementAt(0) : "");
                break;
            case 7:
                Terminal.rmdir(args.size() > 0 ? args.elementAt(0) : "");
                break;
            case 8:
                Terminal.cat(args);
                break;
            case 9:
                Terminal.flag = true;
                break;
            case 10:
                Terminal.pwd();
                break;
            
            case 11:
                Terminal.date();
                break;
            case 12:
                Terminal.help();
                break;
            case 13:
                new Kimo().executeCommand();
                break;
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print(Terminal.currentDir + " ");
            var a = input.nextLine();
            Parse(a);
        }
        
    }
}