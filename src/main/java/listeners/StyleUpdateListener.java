package listeners;

import com.google.inject.Singleton;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.WebDriverListener;
import java.lang.reflect.Method;
import java.util.List;

@Singleton
public class StyleUpdateListener implements WebDriverListener {
  @Override
  public void afterFindElements(WebDriver driver, By locator, List<WebElement> result) {
    for (WebElement webElement : result) {
      ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute(\"style\", \"border:5px solid green\")", webElement);
    }
  }

  @Override
  public void afterFindElement(WebDriver driver, By locator, WebElement result) {
    ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute(\"style\", \"border:5px solid green\")", result);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void beforeClick(WebElement element) {
    WebDriver driver = ((WrapsDriver) element).getWrappedDriver();
    ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute(\"style\", \"border:5px solid red\")", element);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void afterAnyWebElementCall(WebElement element, Method method, Object[] args, Object result) {
    WebDriver driver = ((WrapsDriver) element).getWrappedDriver();
    String methodName = method.getName();
    if (methodName.equals("findElement") || methodName.equals("findElements")) {
      ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute(\"style\", \"border:5px solid green\")", element);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void beforeAnyWebElementCall(WebElement element, Method method, Object[] args) {
    WebDriver driver = ((WrapsDriver) element).getWrappedDriver();
    String methodName = method.getName();
    if (methodName.equals("click")) {
      ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute(\"style\", \"border:5px solid red\")", element);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}

