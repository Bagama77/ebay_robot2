import java.util.logging.Level;
import java.util.logging.Logger;

public class Sleeper {

    private int timeRemainingSeconds;
    private int timeToSleepMilis;

    private static Logger logger = Logger.getLogger(Sleeper.class.getName());

    public Sleeper(int timeRemainingSeconds) {
        this.timeRemainingSeconds = timeRemainingSeconds;
        this.setTime();
    }

    public int getTimeToSleepMilis() {
        return timeToSleepMilis;
    }

    private void setTime(){

        if(timeRemainingSeconds > 7200){
            timeToSleepMilis =600000;
        }
        if(timeRemainingSeconds > 3600 && timeRemainingSeconds <= 7200){
            timeToSleepMilis =60000;
        }
        if(timeRemainingSeconds > 600 && timeRemainingSeconds <= 3600){
            timeToSleepMilis = 10000;
        }
        if(timeRemainingSeconds <= 60){
            timeToSleepMilis = 5000;
        }
        logger.log(Level.INFO, "Set time to sleep in miliseconds: " + timeToSleepMilis);
    }
}
