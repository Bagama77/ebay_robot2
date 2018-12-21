import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    static String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    static String appConfigPath = rootPath + "credentials.properties";
    static Properties appProps = new Properties();
    static Logger logger = Logger.getLogger(Main.class.getName());


    public static void main(String[] args) throws Exception {

        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
        appProps.load(new FileInputStream("/C:/Users/Serhii_Zamikhovskyi/IdeaProjects/Robot/" + "src/main/resources/" + "credentials"));
        String url = appProps.getProperty("url");
        logger.log(Level.INFO, "Calling for url: " + url);
        String password1 = appProps.getProperty("password");
        logger.log(Level.INFO, "password = " + password1);

        //WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Java\\jdk-11.0.1\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        //going to ebay.com
        driver.get(url);
        //my eBay
        driver.findElement(By.xpath("//*[text()=' My eBay']"))//cssSelector("div.qui-buttonset-left ng-scope button.qui-popover-button.qui-dropdown.ng-scope.ng-isolate-scope.qui-button"))
                .click();

        Thread.sleep(5000);

        //input login and password
        driver.findElement(By.xpath("//*[@id='userid']")).sendKeys(appProps.getProperty("login"));
        driver.findElement(By.xpath("//*[@id='pass']")).sendKeys(appProps.getProperty("password"));
        WebElement element = ((ChromeDriver) driver).findElementByXPath("//*[@id='sgnBt']");//submit
        element.click();
        Thread.sleep(5000);

        //typing sku of item
        driver.findElement(By.xpath("//*[@id='gh-ac']")).sendKeys(appProps.getProperty("skuEbayItem"));
        driver.findElement(By.xpath("//*[@id='gh-btn']")).click();

        //clicking on the first element in search list results
        Thread.sleep(5000);
//        driver.findElement(By.xpath("//*[@id='srp-river-results-listing1']/div/div[2]/a")).click();
        WebElement elementItemAuction = driver.findElement(By.xpath("//*[@id='srp-river-results-listing1']/div/div[2]/a"));
        logger.log(Level.INFO,"Link to page auction:" + elementItemAuction.getAttribute("href"));
        Thread.sleep(5000);

        //go to auction page
        driver.get(elementItemAuction.getAttribute("href"));

        //get time remaining
        WebElement timeRemaining = driver.findElement(By.xpath("//*[@id='vi-cdown_timeLeft']"));
        logger.log(Level.INFO,"Time remaining: " + timeRemaining.getText());
        //get Price
        WebElement presentPrice = driver.findElement(By.xpath("//*[@id='prcIsum_bidPrice']"));
        logger.log(Level.INFO,"Price: " + presentPrice.getAttribute("content"));









        //
//
//        ConnectionCaller connectionCaller = new ConnectionCaller("https://ebay.com");
//        connectionCaller.sendGet();
//        StringBuffer response = connectionCaller.getResponseSendGet();
//        ResponseParser parser =  new ResponseParser();
//        parser.setResponse(response.toString());
//        System.out.println(parser.parseResponse());
    }
}
