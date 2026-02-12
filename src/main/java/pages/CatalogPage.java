package pages;

import annotations.Path;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.io.IOException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Path("/catalog/courses/")
public class CatalogPage extends AbsBasePage<CatalogPage> {
  public CatalogPage(WebDriver driver) {
    super(driver);
  }

  @FindBy(xpath = "//p[text()='Направление']/following::div[contains(@class,'isrHWT')][1]")
  private WebElement sideBarEducationMenu;


  public WebElement getCourseByName(String courseName) {
    List<WebElement> courseNamesList = driver.findElements(By.cssSelector("section .sc-zzdkm7-0"));
    Optional<WebElement> searchResult = courseNamesList.stream()
        .filter(coursePlate -> coursePlate.findElement(By.cssSelector("h6 div")).getText()
            .equalsIgnoreCase(courseName))
        .findFirst();
    if (!searchResult.isEmpty()) {
      return searchResult.get();
    }
    throw new RuntimeException(String.format("Курс %s не найден", courseName));
  }

  public List<WebElement> getDisplayedCourses() {
    List<WebElement> courses;
    return courses = driver.findElements(By.cssSelector("section .sc-zzdkm7-0"));
  }

  public LocalDate getCourseStartDate(WebElement coursePlate) {
    String startDate = coursePlate.findElement(By.cssSelector("div.sc-1x9oq14-0 div.sc-hrqzy3-1"))
        .getText().split("·")[0]
        .trim();
    String pattern = "dd MMMM, yyyy";
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withLocale(new Locale("ru"));

      return LocalDate.parse(startDate, formatter);
    } catch (DateTimeParseException e) {
      throw new RuntimeException(e);
    }
  }

  public String getCourseName(WebElement coursePlate) {
    return coursePlate.findElement(By.cssSelector("h6 div")).getText();
  }

  public List<WebElement> getStratDateCursesExtremum(List<WebElement> coursePlates, boolean getEarliest) {
    List<WebElement> extremumCourses;
    LocalDate extremumStartDate = coursePlates.stream()
        .map(this::getCourseStartDate)
        .reduce((date1, date2) ->
            getEarliest
                ? (date1.isBefore(date2) ? date1 : date2) :
                (date1.isAfter(date2) ? date1 : date2))
        .get();

    return extremumCourses = coursePlates.stream()
        .filter(element -> extremumStartDate.equals(this.getCourseStartDate(element)))
        .collect(Collectors.toList());
  }

  public HashMap<String, MonthDay> getCoursePageData(WebElement coursePlate) {
    HashMap<String, MonthDay> result = new HashMap<>();
    String coursePageURL = coursePlate.getAttribute("href");
    try {
      Document document = Jsoup.connect(coursePageURL).get();

      String dateString = document.selectFirst(".sc-3cb1l3-1 p").text();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM", new Locale("ru"));

      MonthDay startDate = MonthDay.parse(dateString, formatter);
      String name = document.select(".sc-s2pydo-6 h1").text();

      result.put(name, startDate);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  public boolean checkCourseDataOnCoursePage(WebElement coursePlate) {
    String courseNameOnCatalogPage = this.getCourseName(coursePlate);
    MonthDay courseStartDateOnCatalogPage = MonthDay.from(this.getCourseStartDate(coursePlate));

    HashMap<String, MonthDay> coursePageData = this.getCoursePageData(coursePlate);
    String courseNameOnCoursePage = coursePageData.keySet().iterator().next();
    MonthDay courseStartDateOnCoursePage = coursePageData.values().iterator().next();

    if (courseNameOnCatalogPage.equals(courseNameOnCoursePage) && courseStartDateOnCatalogPage.equals(courseStartDateOnCoursePage)) {
      return true;
    } else {
      new RuntimeException(String.format("Данные курса %s не совпадают", courseNameOnCatalogPage));
      return false;
    }
  }

  public List<String> getActiveEducationCategoryNames() {
    List<WebElement> activeEducationCategories;
    activeEducationCategories = sideBarEducationMenu.findElements(By.cssSelector("div.sc-1fry39v-0[value='true']"));
    List<String> categoryNames = activeEducationCategories.stream()
        .map(category -> category.findElement(By.cssSelector("label")).getText()).toList();
    return categoryNames;
  }
}
