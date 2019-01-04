import java.util.logging.Level;
import java.util.logging.Logger;

public class TradeLogic {
    private double currentPrice;
    private boolean isMeOwnerCurrentPrice = false;
    private int timeRemainingSeconds;
    private double maximumPrice;
    private boolean isMaximumPriceReached;
    private boolean isTradeSuccesfull;//final result
    private boolean isTradeFinished;
    private double priceBid;
    private String scenario;

    private static Logger logger = Logger.getLogger(TradeLogic.class.getName());



//-----------CONSTRUCTORS------------------------------
    public TradeLogic(double currentPrice, int timeRemainingSeconds, double priceBid, boolean isMeOwnerCurrentPrice, double maximumPrice) {
        this.currentPrice = currentPrice;
        this.timeRemainingSeconds = timeRemainingSeconds;
        this.priceBid = priceBid;
        this.isMeOwnerCurrentPrice = isMeOwnerCurrentPrice;
        this.maximumPrice = maximumPrice;
        this.scenario = "OPEN";
    }

    public TradeLogic(double maximumPrice) {
        this.maximumPrice = maximumPrice;
        this.scenario = "OPEN";
        this.isMeOwnerCurrentPrice = false;
    }

    //------------GETTERS SETTERS-------------------------


    public double getPriceBid() {
        return priceBid;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        logger.log(Level.INFO, "Set setScenario" + scenario);
        this.scenario = scenario;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setPriceBid(double priceBid) {
        logger.log(Level.INFO, "Set setPriceBid" + priceBid);
        this.priceBid = priceBid;
    }

    public void setCurrentPrice(double currentPrice) {
        logger.log(Level.INFO, "Set setCurrentPrice" + currentPrice);
        this.currentPrice = currentPrice;
    }

    public boolean isMeOwnerCurrentPrice() {
        return isMeOwnerCurrentPrice;
    }

    public void setMeOwnerCurrentPrice(boolean meOwnerCurrentPrice) {
        logger.log(Level.INFO, "Set setMeOwnerCurrentPrice !!!");
        isMeOwnerCurrentPrice = meOwnerCurrentPrice;
    }

    public int getTimeRemainingSeconds() {
        return timeRemainingSeconds;
    }

    public void setTimeRemainingSeconds(int timeRemainingSeconds) {
        logger.log(Level.INFO, "Set setTimeRemainingSeconds:" + timeRemainingSeconds);
        this.timeRemainingSeconds = timeRemainingSeconds;
    }

    public double getMaximumPrice() {
        return maximumPrice;
    }

    public void setMaximumPrice(double maximumPrice) {
        logger.log(Level.INFO, "Set setMaximumPrice:" + maximumPrice);
        this.maximumPrice = maximumPrice;
    }





//-----------------FUNCTIONS-----------------------------------------
    void calculateScenario(){
        isMaximumPriceReached();
        isTradeFinished();

        if(!isMaximumPriceReached && !isTradeFinished && !isMeOwnerCurrentPrice){
            logger.log(Level.INFO, "calculated Scenario - " + "INCREASE_PRICE");
            this.scenario = "INCREASE_PRICE";
        } else if(!isMaximumPriceReached && !isTradeFinished && isMeOwnerCurrentPrice){
            logger.log(Level.INFO, "calculated Scenario - " + "NOTHING_TO_DO");
            this.scenario = "NOTHING_TO_DO";
        } else {
            logger.log(Level.INFO, "calculated Scenario - " + "SHUTDOWN");
            this.scenario = "SHUTDOWN";
        }
    }

    boolean isTradeFinished(){
        if(timeRemainingSeconds == 0 || currentPrice > maximumPrice){
            this.isTradeFinished = true;
            logger.log(Level.INFO, "Trade is finished!");
            return true;
        } else {
            this.isTradeFinished = false;
            logger.log(Level.INFO, "Trade is not finished!");
            return false;
        }
    }

    private boolean isMaximumPriceReached(){
        if(maximumPrice > currentPrice) {
            this.isMaximumPriceReached = false;
            logger.log(Level.INFO, "Current price is not reached!");
            return false;
        }
        logger.log(Level.INFO, "Current price is reached!");
        return true;
    }

    boolean isTradeSuccessful(){
        if(isMeOwnerCurrentPrice && isTradeFinished()) {
            logger.log(Level.INFO, "Me is owner of current price, trade is finished, trade is succesfull!");
            isTradeSuccesfull = true;
        }
        logger.log(Level.INFO, "Trade is not succesfull!");
        return false;
    }

}
