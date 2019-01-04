import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeParser {
    String timeInText;
    int secondsRemain;

    private static Logger logger = Logger.getLogger(TimeParser.class.getName());

    public TimeParser(String timeInText) {
        this.timeInText = timeInText;
        this.parseTime(timeInText);
    }

    void parseTime(String timeInText) {
        List<String> timeWordsList = Arrays.asList(timeInText.split(" "));
        timeWordsList.forEach(w -> {
            if (w.contains("d")) {
                secondsRemain += Integer.valueOf(w.substring(0, w.indexOf('d'))) * 86400;
            }
            if (w.contains("h")) {
                secondsRemain += Integer.valueOf(w.substring(0, w.indexOf('h'))) * 3600;
            }
            if (w.contains("m")) {
                secondsRemain += Integer.valueOf(w.substring(0, w.indexOf('m'))) * 60;
            }
            if (w.contains("s")) {
                secondsRemain += Integer.valueOf(w.substring(0, w.indexOf('s')));
            }
        });
        logger.log(Level.INFO, "Parsed time: " + secondsRemain);
    }

    @Override
    public String toString() {
        return "TimeParser{" +
                "timeInText='" + timeInText + '\'' +
                ", secondsRemain=" + secondsRemain +
                '}';
    }
}
