package istic.project.aqropol;

import jssc.SerialPortList;
import org.apache.commons.cli.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Options options = new Options();
        options.addOption("l", "list", false, "liste les interfaces USB disponibles");
        options.addOption("h", "help", false, "print this message");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if(cmd.hasOption("help")) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("pmsensor", options);
            } else if(cmd.hasOption("list")) {
                App.printPortList();
            }

        } catch (ParseException e) {
            System.err.println( "Parsing failed.  Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printPortList() {
        String[] portNames = SerialPortList.getPortNames();

        System.out.println("Serial Port List [" + portNames.length +"] :");

        for(int i = 0; i < portNames.length; i++){
            System.out.println("\t- " + portNames[i]);
        }
    }
}
