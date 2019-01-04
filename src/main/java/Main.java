import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.util.Set;
import java.util.logging.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.*; //interactions.Actions;
//import org.openqa.selenium.internal.selenesedriver.*;//support.ui.Select;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
//import org.openqa.selenium.HasInputDevices;

public class Main {

    private static String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    static String appConfigPath = rootPath + "credentials.properties";
    private static Properties appProps = new Properties();
    private static Logger logger = Logger.getLogger(Main.class.getName());
    private static WebDriver driver;


    static{
        try{
            appProps.load(new FileInputStream("C:\\Users\\Bagama\\IdeaProjects\\Robot ebay\\ebay_robot2\\src\\main\\resources\\" + "credentials"));
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Java\\jdk1.8.0_181\\chromedriver.exe");
            driver = new ChromeDriver();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        System.out.println(Thread.currentThread().getContextClassLoader().getResource("credentials").getPath());
        String url = appProps.getProperty("url");

        loginAndFindItemPage(url);
        TradeLogic trader = new TradeLogic(Double.valueOf(appProps.getProperty("maxAmount")));
        parsePage(trader);

        while (!trader.getScenario().equalsIgnoreCase("SHUTDOWN")){

            if(trader.getScenario().equalsIgnoreCase("INCREASE_PRICE")){
                increaseTheCurrentPrice(trader);
            }
            //Thread.sleep(new Sleeper(timeParser.secondsRemain).getTimeToSleepMilis());
            Thread.sleep(5000);

            driver.navigate().refresh();
            parsePage(trader);
        }


        if(trader.isTradeFinished()){
            //mailer.send mail
        }


    }

    public static void loginAndFindItemPage(String url) throws InterruptedException{

        //going to ebay.com
        driver.get(url);//my eBay
        driver.findElement(By.xpath("//*[text()=' My eBay']"))//cssSelector("div.qui-buttonset-left ng-scope button.qui-popover-button.qui-dropdown.ng-scope.ng-isolate-scope.qui-button"))
                .click();

        //input login and password
        driver.findElement(By.xpath("//*[@id='userid']")).sendKeys(appProps.getProperty("login"));
        driver.findElement(By.xpath("//*[@id='pass']")).sendKeys(appProps.getProperty("password"));
        WebElement element = ((ChromeDriver) driver).findElementByXPath("//*[@id='sgnBt']");//submit
        element.click();
        Thread.sleep(5000);

        //in case when there is page with secret questions
        try {
            WebElement secretQuestionPage = ((ChromeDriver) driver).findElementByXPath("//*[@id='secretQuesForm']/h1");
            if (secretQuestionPage != null) {
                driver.findElement(By.xpath("//*[@id='rmdLtr']")).click();
                Thread.sleep(5000);
            }
        }catch (Exception e){
//            e.printStackTrace();
            System.out.println("continue work without secret questions page...");
        }

        //typing sku of item
        driver.findElement(By.xpath("//*[@id='gh-ac']")).sendKeys(appProps.getProperty("skuEbayItem"));
        driver.findElement(By.xpath("//*[@id='gh-btn']")).click();

        //clicking on the first element in search list results
        Thread.sleep(5000);
        WebElement elementItemAuction = driver.findElement(By.xpath("//*[@id='srp-river-results-listing1']/div/div[2]/a"));
        logger.log(Level.INFO,"Link to page auction:" + elementItemAuction.getAttribute("href"));
        Thread.sleep(5000);

        //go to auction page
        driver.get(elementItemAuction.getAttribute("href"));
    }

    public static void parsePage(TradeLogic trader) throws InterruptedException{
        //get time remaining
        WebElement timeRemaining = driver.findElement(By.xpath("//*[@id='vi-cdown_timeLeft']"));
        TimeParser timeParser = new TimeParser(timeRemaining.getText());
        //logger.log(Level.INFO,"Parsed time remaining: " + timeParser);

        //get Price
        WebElement presentPriceElement = driver.findElement(By.xpath("//*[@id='prcIsum_bidPrice']"));
        double presentPriceDouble = Double.valueOf(presentPriceElement.getAttribute("content"));
        logger.log(Level.INFO,"Price parsed: " + presentPriceDouble);

        //get priceBid
        Thread.sleep(5000);
        String priceStepText = ((ChromeDriver) driver).findElementByXPath("//*[@id='w1-17-_mtb']").getText();
        double priceBid = Double.valueOf(priceStepText.substring(priceStepText.indexOf('$')+1, priceStepText.indexOf('.')+2));
        logger.log(Level.INFO,"next price is:" + priceBid);

        //set new values to trader logic
        trader.setCurrentPrice(presentPriceDouble);
        trader.setTimeRemainingSeconds(timeParser.secondsRemain);
        trader.setPriceBid(priceBid);
        trader.calculateScenario();
    }

    public static void increaseTheCurrentPrice(TradeLogic trader){
        logger.log(Level.INFO,"Inside method increaseTheCurrentPrice..");
        driver.findElement(By.xpath("//*[@id='MaxBidId']")).sendKeys("" + trader.getPriceBid());
        driver.findElement(By.xpath("//*[@id='bidBtn_btn']")).click();
        trader.setMeOwnerCurrentPrice(true);
    }
}
