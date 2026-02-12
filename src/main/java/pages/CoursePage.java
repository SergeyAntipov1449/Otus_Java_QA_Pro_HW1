package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CoursePage extends AbsBasePage<CoursePage> {
  public CoursePage(WebDriver driver) {
    super(driver);
  }

  public String getCourseName() {

    return driver.findElement(By.cssSelector(".sc-s2pydo-6 h1")).getText();
  }


}
