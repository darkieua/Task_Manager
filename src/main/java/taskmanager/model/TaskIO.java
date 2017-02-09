package taskmanager.model;

import java.io.*;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static taskmanager.controller.MainController.dateFormat;

/**
 * Created by DarkST on 18.12.2016.
 */

public class TaskIO {

    public static void write (TaskList tasks, Writer out) {
        try {
            int count = 1;
            for (Task temp : tasks) {
                out.write(temp.getTitle().replace("\"", "\"\""));
                if (temp.isRepeated()) {
                    out.write(" from [" + dateFormat.format(temp.getStartTime()) + "] to [" + dateFormat.format(temp.getEndTime()) + "]" + " every [" + intervalFormat((int)temp.getRepeatInterval()) + "]");
                }
                else {
                    out.write(" at [" + dateFormat.format(temp.getTime()) + "]");
                }
                if (!temp.isActive()) out.write(" inactive");
                out.write((count++ < tasks.size() ? ";" : ".") + "\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write (TaskList tasks, OutputStream out) {
        try {
            DataOutputStream stream = new DataOutputStream(out);
            stream.writeInt(tasks.size());
            for (Task temp : tasks) {
                stream.writeInt(temp.getTitle().length());
                stream.writeUTF(temp.getTitle());
                stream.writeBoolean(temp.isActive());
                stream.writeBoolean(temp.isRepeated());
                if (temp.isRepeated()) {
                    stream.writeLong(temp.getStartTime().getTime());
                    stream.writeLong(temp.getEndTime().getTime());
                    stream.writeLong(temp.getRepeatInterval());
                }
                else {
                    stream.writeLong(temp.getTime().getTime());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read (TaskList tasks, Reader in) {
        try {
            int readed = in.read();
            String dataString = new String();
            while (readed != -1) {
                char dataChar = (char)readed;
                dataString += dataChar;
                if (dataChar == '\n') {
                    tasks.add(parseLine(dataString));
                    dataString = "";
                }
                readed = in.read();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read (TaskList tasks, InputStream in) {
        DataInputStream stream = new DataInputStream(in);
        try {
            int size = stream.readInt();
            for (int i = 0; i < size; i ++) {
                int titleSize = stream.readInt();
                String title = stream.readUTF();
                Boolean isActive = stream.readBoolean();
                boolean isRepeated = stream.readBoolean();

                if (isRepeated) {
                    long startTime = stream.readLong();
                    long endTime = stream.readLong();
                    long interval = stream.readLong();
                    Task temp = new Task(title, new Date(startTime), new Date(endTime), (int)interval/1000);
                    if (isActive) temp.setActive(true);
                    tasks.add(temp);
                }
                else {
                    long time = stream.readLong();
                    Task temp = new Task(title, new Date(time));
                    if (isActive) temp.setActive(true);
                    tasks.add(temp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeBinary (TaskList tasks, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        write(tasks, out);
    }

    public static void readBinary (TaskList tasks, File file) throws FileNotFoundException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
        read(tasks, in);
    }

    public static void writeText (TaskList tasks, File file) {
        try {
            Writer writer = new FileWriter(file);
            write(tasks, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readText (TaskList tasks, File file) {
        try {
            Reader reader = new FileReader(file);
            read(tasks, reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String intervalFormat (int input) {
        input = input / 1000;
        int numberOfDays = input / 86400;
        int numberOfHours = (input % 86400 ) / 3600;
        int numberOfMinutes = ((input % 86400 ) % 3600 ) / 60;
        int numberOfSeconds = ((input % 86400 ) % 3600 ) % 60 ;
        String output = new String();
        if (numberOfDays > 0) {
            output += numberOfDays + " ";
            if (numberOfDays > 1) output += "days ";
            else output += "day ";
        }
        if (numberOfHours > 0) {
            output += numberOfHours + " ";
            if (numberOfHours > 1) output += "hours ";
            else output += "hour ";
        }

        if (numberOfMinutes > 0) {
            output += numberOfMinutes + " ";
            if (numberOfMinutes > 1) output += "minutes ";
            else output += "minute ";
        }

        if (numberOfSeconds > 0) {
            output += numberOfSeconds + " ";
            if (numberOfSeconds > 1) output += "seconds";
            else output += "second";
        }
        return output;
    }

    private static Date parseDate (String dateStr) {
        if (!dateStr.isEmpty()) {
            dateStr = dateStr.replace("[", "");
            dateStr = dateStr.replace("]", "");
            Date date = null;
            try {
                date = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }
        return null;
    }

    private static int parseInterval (String interval) {
        if (!interval.isEmpty()) {
            int days = 0;
            int hours = 0;
            int minutes = 0;
            int seconds = 0;

            interval = interval.replace("[", "");
            interval = interval.replace("]", "");
            String tokens[] = interval.split(" ");

            for (int tokenCount = 0; tokenCount < tokens.length; tokenCount ++) {
                if (tokens[tokenCount].compareTo("days") == 0 || tokens[tokenCount].compareTo("day") == 0) {
                    days = Integer.parseInt(tokens[tokenCount - 1]);
                }
                if (tokens[tokenCount].compareTo("hours") == 0 || tokens[tokenCount].compareTo("hour") == 0) {
                    hours = Integer.parseInt(tokens[tokenCount - 1]);
                }
                if (tokens[tokenCount].compareTo("minutes") == 0 || tokens[tokenCount].compareTo("minute") == 0) {
                    minutes = Integer.parseInt(tokens[tokenCount - 1]);
                }

                if (tokens[tokenCount].compareTo("seconds") == 0 || tokens[tokenCount].compareTo("second") == 0) {
                    seconds = Integer.parseInt(tokens[tokenCount - 1]);
                }
            }
            return (86400 * days) + (3600 * hours) + (60 * minutes) + seconds;
        }
        return -1;
    }

    private static Task parseLine (String line) {
        String tokens[] = line.split(" ");
        boolean isRepeated = false;
        boolean isActive = true;
        String title = new String();
        String time = new String();
        String from = new String();
        String to = new String();
        String interval = new String();
        for (int tokenCount = 0; tokenCount < tokens.length; tokenCount ++) {
            if (tokens[tokenCount].compareTo("from") == 0) {
                isRepeated = true;
                for (int i = 0; i < tokenCount; i ++) {
                    title += tokens[i] + " ";
                }
                title = title.substring(0, title.length() - 1);
                for (int i = tokenCount + 1; tokens[i].compareTo("to") != 0; i ++) {
                    from += tokens[i] + " ";
                }
            }

            if (isRepeated) {
                if (tokens[tokenCount].compareTo("to") == 0){
                    for (int i = tokenCount + 1; tokens[i].compareTo("every") != 0; i++) {
                        to += tokens[i] + " ";
                    }
                }

                if (tokens[tokenCount].compareTo("every") == 0) {
                    for (int i = tokenCount + 1; !tokens[i - 1].contains("]"); i++) {
                        interval += tokens[i] + " ";
                    }
                }
            }
            else {
                if (tokens[tokenCount].compareTo("at") == 0) {
                    isRepeated = false;
                    for (int i = 0; i < tokenCount; i ++) {
                        title += tokens[i] + " ";
                    }
                    title = title.substring(0, title.length() - 1);
                    for (int i = tokenCount + 1; !tokens[i - 1].contains("]"); i ++) {
                        time += tokens[i] + " ";
                    }
                }
            }

            if (tokens[tokenCount].contains("inactive")) {
                isActive = false;
            }
        }
        Task result = null;
        if (isRepeated) {
             result = new Task(title, parseDate(from), parseDate(to), parseInterval(interval));
        } else {
            result = new Task(title, parseDate(time));
        }
        if (isActive) result.setActive(true);
        return result;
    }

    private static int intFromByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }
    private static long longFromByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }
}
