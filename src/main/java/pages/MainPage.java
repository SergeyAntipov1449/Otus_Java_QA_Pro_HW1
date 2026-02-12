package pages;

import annotations.Path;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.Collections;
import java.util.List;

@Path("/")
//@PathTemplate("/$1")
public class MainPage extends AbsBasePage<MainPage> {
  public MainPage(WebDriver driver) {
    super(driver);
  }

  @FindBy(css = "nav span[title='Обучение']")
  private WebElement educationMenu;

  public void showEducationMenu() {
    actions.moveToElement(educationMenu).build().perform();
  }

  public List<WebElement> getEducationCategories() {
    List<WebElement> educationCategories = driver.findElements(By.cssSelector("div.sc-ig0m9y-0 a"));
    return educationCategories;
  }

  public WebElement getRandomCategory(List<WebElement> educationCategories) {
    Collections.shuffle(educationCategories);
    WebElement randomCategory = educationCategories.get(0);
    return randomCategory;
  }

  public String getCategoryName(WebElement educationCategory) {
    return educationCategory.getText().split(" \\(")[0];
  }

}
