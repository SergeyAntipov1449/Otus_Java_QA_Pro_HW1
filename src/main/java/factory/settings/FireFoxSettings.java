package factory.settings;

import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

public class FireFoxSettings implements BrowserOptions {
  @Override
  public AbstractDriverOptions settings() {
    FirefoxOptions firefoxOptions = new FirefoxOptions();
    firefoxOptions.addArguments("--start-maximized");
    return firefoxOptions;
  }
}
