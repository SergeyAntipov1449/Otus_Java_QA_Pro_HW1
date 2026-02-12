package pages;

import annotations.Path;
import commons.AbsCommons;
import exceptions.AnnotationNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public abstract class AbsBasePage<T> extends AbsCommons {
  public AbsBasePage(WebDriver driver) {
    super(driver);
  }

  private String baseUrl = System.getProperty("base.url");

  @FindBy(xpath = "//*[contains(text(), 'Посещая наш сайт')]/following-sibling::div//button")
  protected WebElement notificationButton;

  public String getPath() {
    Class clazz = getClass();
    if (clazz.isAnnotationPresent(Path.class)) {
      Path path = (Path) clazz.getDeclaredAnnotation(Path.class);
      return path.value();
    }
    throw new AnnotationNotFoundException();
  }

  public String getPathFromTemplate(String... data) {
    Class clazz = getClass();
    if (clazz.isAnnotationPresent(Path.class)) {
      Path path = (Path) clazz.getDeclaredAnnotation(Path.class);
      String value = path.value();
      for (int i = 1; i < data.length; i++) {
        value.replace("$" + i, data[i]);
      }
      return value;
    }
    throw new AnnotationNotFoundException();
  }

  public T open() {
    driver.get(baseUrl + getPath());
    return (T) this;
  }

  public T open(String... data) {
    driver.get(baseUrl + getPath() + getPathFromTemplate());
    return (T) this;
  }

  public String getPageURL() {
    return driver.findElement(By.cssSelector("head [rel='canonical']")).getAttribute("href");
  }

  public void closeNotification() {
    webDriverWait.waitForCondition(ExpectedConditions.stalenessOf(notificationButton), 4);
    notificationButton.click();
  }
}
