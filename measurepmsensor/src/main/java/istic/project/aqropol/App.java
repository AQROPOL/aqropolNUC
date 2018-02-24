package istic.project.aqropol;

import jssc.*;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Options options = new Options();

        /*
            Help option group
         */
        {
            OptionGroup optionGroup = new OptionGroup();

            {
                Option option = new Option("h", "help", false, "print this message.");
                optionGroup.addOption(option);
            }

            options.addOptionGroup(optionGroup);
        }

        /*
            Info Group Options
         */
        {
            OptionGroup optionGroup = new OptionGroup();

            {
                Option option = new Option("l", "list", false, "Liste les interfaces USB disponibles.");
                option.setRequired(true);
                optionGroup.addOption(option);
            }

            {
                Option option = new Option("c", "path", true, "Spécifie le path des interfaces USB.");
                optionGroup.addOption(option);
            }

            {
                Option option = new Option("r", "regex", true, "Spécifie un pattern pour filtrer la liste des port.");
                optionGroup.addOption(option);
            }

            options.addOptionGroup(optionGroup);
        }



        /*
            Port option groupe
         */
        {
            OptionGroup optionGroup = new OptionGroup();

            {
                Option option = new Option("i", "interface", true, "Ouvre l'interface USB spécifié.");
                option.setRequired(true);
                optionGroup.addOption(option);
            }

            {
                Option option = new Option("b", "baud", true, "Spécifie le baud rate.");
                option.setType(Integer.class);
                option.setArgName("baud_rate");
                option.setArgs(1);
                options.addOption(option);
            }

            {
                Option option = new Option("d", "databit", true, "Spécifie le data bit.");
                options.addOption(option);
            }

            {
                Option option = new Option("s", "stop", true, "Spécifie le stop bit.");
                options.addOption(option);
            }

            {
                Option option = new Option("p", "parity", true, "Spécifie le parity bit.");
                options.addOption(option);
            }

            options.addOptionGroup(optionGroup);
        }

        /*
            Parse command
         */
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            if(cmd.hasOption("help")) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("[-h] | -l [-c <path>] [-r <regex>] | -i <name> [-b <baud_rate>] [-d <data_bit>] [-s <stop_bit>] [-p <parity_bit>]", options);
            } else if(cmd.hasOption("list")) {
                String path = cmd.getOptionValue("path");
                String regex = cmd.getOptionValue("regex");

                App.printPortList(path, regex);
            } else if(cmd.hasOption("interface")) {
                String name = cmd.getOptionValue("interface");
                int baud_rate = (int) (cmd.hasOption("baud") ? cmd.getParsedOptionValue("baud") : SerialPort.BAUDRATE_9600);
                int data_bit = (int) (cmd.hasOption("databit") ? cmd.getParsedOptionValue("databit") : SerialPort.DATABITS_8);
                int stop_bit = (int) (cmd.hasOption("stop") ? cmd.getParsedOptionValue("stop") : SerialPort.STOPBITS_1);
                int parity_bit = (int) (cmd.hasOption("parity") ? cmd.getParsedOptionValue("parity") : SerialPort.PARITY_NONE);
                openPort(name, baud_rate, data_bit, stop_bit, parity_bit);
            }

        } catch (ParseException e) {
            System.err.println( "Parsing failed.  Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void openPort(String name, int baud_rate, int data_bit, int stop_bit, int parity_bit) {
        SerialPort serialPort = new SerialPort(name);
        try {
            serialPort.openPort();
            boolean res = serialPort.setParams(baud_rate, data_bit, stop_bit, parity_bit);
            if(res) {
                System.out.println("serial port parameters set to :\n" +
                        "\tBaud rate : " + baud_rate + "\n" +
                        "\tData bit : " + data_bit + "\n" +
                        "\tStop bit : " + stop_bit + "\n" +
                        "\tParity bit : " + parity_bit + "\n");
            } else {
                System.err.println("[ECHEC] with parameters : \n" +
                        "\tBaud rate : " + baud_rate + "\n" +
                        "\tData bit : " + data_bit + "\n" +
                        "\tStop bit : " + stop_bit + "\n" +
                        "\tParity bit : " + parity_bit + "\n");
            }

            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
            serialPort.setEventsMask(mask);
            serialPort.addEventListener(event -> {
                if(event.isRXCHAR()){//If data is available
                        //Read data, if 10 bytes available

                    try {
                            byte buffer[] = serialPort.readBytes();
                            System.out.print(new String(buffer));
                        }
                        catch (SerialPortException ex) {
                            ex.printStackTrace();
                        }
                }
                else if(event.isCTS()){//If CTS line has changed state
                    if(event.getEventValue() == 1){//If line is ON
                        System.out.println("CTS - ON");
                    }
                    else {
                        System.out.println("CTS - OFF");
                    }
                }
                else if(event.isDSR()){///If DSR line has changed state
                    if(event.getEventValue() == 1){//If line is ON
                        System.out.println("DSR - ON");
                    }
                    else {
                        System.out.println("DSR - OFF");
                    }
                }
            });

            boolean loop = true;
            while(loop) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            serialPort.closePort();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    private static void printPortList(String path, String regex) {
        String[] portNames;

        if(path == null) {
            portNames = SerialPortList.getPortNames();
        } else if (regex == null) {
            portNames = SerialPortList.getPortNames();
        } else {
            portNames = SerialPortList.getPortNames(path, Pattern.compile(regex));
        }

        System.out.println("Serial Port List [" + portNames.length +"] :");

        Arrays.stream(portNames).map(portName -> "\t- " + portName).forEach(System.out::println);
    }
}
