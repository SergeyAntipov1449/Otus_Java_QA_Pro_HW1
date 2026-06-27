package factory.settings;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

public class ChromeSettings implements BrowserOptions {
  @Override
  public AbstractDriverOptions settings() {
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.addArguments("--start-maximized");
    chromeOptions.addArguments("--no-sandbox");
    chromeOptions.addArguments("--disable-dev-shm-usage");

    String headless = System.getProperty("headless");
    if ("true".equalsIgnoreCase(headless)) {
      chromeOptions.addArguments("--headless=new");
    }

    return chromeOptions;
  }
}
