package istic.project.aqropol;

import com.fazecast.jSerialComm.SerialPort;
import com.google.gson.Gson;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.firmata4j.IODevice;
import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;
import org.firmata4j.firmata.FirmataDevice;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class App {

    private static final Gson gson = new Gson();

    private final static Logger logger = Logger.getLogger(RoutingProducer.class);

    public static void main(String[] args) {
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

            {
                Option option = new Option("l", "list", false, "Liste les interfaces USB disponibles.");
                option.setRequired(true);
                optionGroup.addOption(option);
            }

            {
                Option option = new Option("i", "interface", true, "Ouvre l'interface USB spécifié. Par exemple : /dev/tty.usbmodem1421");
                option.setType(String.class);
                option.setArgName("name");
                option.setArgs(1);
                option.setRequired(true);
                optionGroup.addOption(option);
            }

            options.addOptionGroup(optionGroup);
        }


        /*
            Parse command
         */
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("help")) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("[-h] | -l | -i <name>", options);
            } else if (cmd.hasOption("list")) {
                App.printPortList();
            } else if (cmd.hasOption("interface")) {
                String portName = cmd.getOptionValue("interface");
                openPort(portName);
            }

        } catch (ParseException e) {
            logger.error("Fail to parse", e);
        }
    }

    private static void openPort(String portName) {
        /*
            Attention Firmata prend le chemin absolu ou relatif, pas seulement le nom de l'interface
         */
        IODevice device = new FirmataDevice(portName);

        try {

            RoutingProducer.RoutingProducerFactory fact = new RoutingProducer.RoutingProducerFactory();
            RoutingProducer producer = fact.build();

            device.addEventListener(new IODeviceEventListener() {
                @Override
                public void onStart(IOEvent ioEvent) {
                    System.out.println(ioEvent);
                }

                @Override
                public void onStop(IOEvent ioEvent) {
                    System.out.println(ioEvent);
                }

                @Override
                public void onPinChange(IOEvent ioEvent) {
                    System.out.println(ioEvent);
                }

                @Override
                public void onMessageReceive(IOEvent ioEvent, String s) {
                    try {
                        producer.send(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            device.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("shutting down, closing the serial comm..");
                try {
                    device.stop();
                    System.out.print("Serial comm closed");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }


        //JsonReader reader = new JsonReader(new InputStreamReader(serialPort.getInputStream()));
        //System.out.println(gson.fromJson(line, Measure.class));
    }

    private static void printPortList() {
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Serial Port List [" + ports.length + "] :");

        for (int i = 0; i < ports.length; i++) {
            System.out.println("\t- [" + i + "] " + ports[i].getDescriptivePortName() + " <" + ports[i].getSystemPortName() + ">");
        }
    }

    public static boolean isJSONValid(String jsonInString) {
        try {
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    public class Measure {
        public String data;
        public Sensor sensor;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Sensor getSensor() {
            return sensor;
        }

        public void setSensor(Sensor sensor) {
            this.sensor = sensor;
        }

        @Override
        public String toString() {
            return "istic.project.aqropol.App.measure{" +
                    "data='" + data + '\'' +
                    ", sensor=" + sensor +
                    '}';
        }

        public class Sensor {
            public String name;
            public String unity;
            public String type;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUnity() {
                return unity;
            }

            public void setUnity(String unity) {
                this.unity = unity;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return "istic.project.aqropol.App.measure.Sensor{" +
                        "name='" + name + '\'' +
                        ", unity='" + unity + '\'' +
                        ", type='" + type + '\'' +
                        '}';
            }
        }
    }
}
