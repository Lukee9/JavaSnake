package Snake;

import java.io.*;
import java.util.*;
import java.time.*;

public class Leaderboard {
    String filename; //The filename for the leaderboard text file

    //Creates the file if it doesn't already exist, since we read the file before writing to it (writing to it normally automatically creates it anyway)
    public Leaderboard(String Filename) {
        filename = Filename;
        File file = new File(Filename);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Adds new user's score to leaderboard
    public void addScore(int Score, String Name) {
        BufferedWriter write = null;
        FileWriter file = null;
        try {
            file = new FileWriter(filename, true);
            write = new BufferedWriter(file);
            //Gets the current date and time in seconds so we can see if it was within the last 24 hours later
            LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            //Writes in the form "Point - Name;TimeInSeconds" then newline
            write.write(String.format("%s - %s;%s%n", Score, Name, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));
            write.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (write != null) {
                    write.close();
                }
                if (file != null) {
                    file.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    //Gets the best of the user's scores within the last 24 hours
    public String[] getRecentBest() {
        ArrayList<String> lines = new ArrayList<String>(); //Stores all lines from the leaderboard
        BufferedReader read = null;
        FileReader file = null;
        String nextLine = "";
        String dateTimeString;
        try {
            file = new FileReader(filename);
            read = new BufferedReader(file);
            nextLine = read.readLine(); //The next line to check in the file
            while (nextLine != null) {
                dateTimeString = nextLine.split(";")[1]; //Gets only the datetime part of the line
                //Checks if the datetime part of the line is within 24*60*60 seconds of the current datetime (now). 24*60*60 is 24 hours in seconds.
                if (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - Long.parseLong(dateTimeString) < 24 * 60 * 60) {
                    lines.add(nextLine.split(";")[0]); //Adds the score to the list if it was within the last 24 hours, only adds the name and score part, not the time part
                }
                nextLine = read.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (read != null) {
                    read.close();
                }
                if (file != null) {
                    file.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        //Gets the ten best scores from our list and returns them
        String[] best = getTenBest(lines);
        return best;
    }
    //Gets the best of the user's scores
    public String[] getAllBest() {
        ArrayList<String> lines = new ArrayList<String>(); //Stores all lines from the leaderboard
        BufferedReader read = null;
        FileReader file = null;
        String nextLine = "";
        try {
            file = new FileReader(filename);
            read = new BufferedReader(file);
            nextLine = read.readLine();
            while (nextLine != null) {
                lines.add(nextLine.split(";")[0]); //Adds the score to the list if it was within the last 24 hours, only adds the name and score part, not the time part
                nextLine = read.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (read != null) {
                    read.close();
                }
                if (file != null) {
                    file.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        //Gets the ten best scores from our list and returns them
        String[] best = getTenBest(lines);
        return best;
    }

    //Gets the ten best values from the given list
    private String[] getTenBest(ArrayList<String> List) {
        //Sorts the list in order of the 'point' section of the string, which was in format 'point - name'
        List.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.split(" - ")[0].compareTo(o1.split(" - ")[0]);
            }
        });
        //Returns the top 10 values, however if less than 10 exist, returns the full set of values
        if (List.size() > 10) {
            return List.subList(0, 10).toArray(new String[10]);
        } else {
            return List.toArray(new String[List.size()]);
        }
    }

}
