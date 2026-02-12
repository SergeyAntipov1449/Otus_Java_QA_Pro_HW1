package commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import utilits.Waiters;

public abstract class AbsCommons {
  public WebDriver driver;
  protected Waiters webDriverWait;
  protected Actions actions;

  public AbsCommons(WebDriver driver) {
    this.driver = driver;
    this.webDriverWait = new Waiters(driver);
    this.actions = new Actions(driver);
    PageFactory.initElements(driver, this);
  }
}
