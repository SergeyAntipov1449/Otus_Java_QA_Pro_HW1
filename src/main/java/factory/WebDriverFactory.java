package factory;

import exceptions.BrowserNotFoundException;
import factory.settings.ChromeSettings;
import factory.settings.FireFoxSettings;
import io.github.bonigarcia.wdm.WebDriverManager;
import listeners.StyleUpdateListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.decorators.Decorated;
import org.openqa.selenium.support.events.EventFiringDecorator;

public class WebDriverFactory {

  private String browser = System.getProperty("browser");
  private StyleUpdateListener styleUpdateListener = new StyleUpdateListener();

  public WebDriver create() {
    switch (browser) {
      case "chrome" -> {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver((ChromeOptions) new ChromeSettings().settings());
        return new EventFiringDecorator<>(styleUpdateListener).decorate(driver);
      }
      case "firefox" -> {
        WebDriverManager.firefoxdriver().setup();
        WebDriver driver = new FirefoxDriver((FirefoxOptions) new FireFoxSettings().settings());
        return new EventFiringDecorator<>(styleUpdateListener).decorate(driver);
      }
    }
    throw new BrowserNotFoundException(browser);
  }

  @SuppressWarnings("unchecked")
  public void quit(WebDriver driver) {
    WebDriver original = ((Decorated<WebDriver>) driver).getOriginal();
    WebDriverManager.getInstance().quit(original);
  }
}
