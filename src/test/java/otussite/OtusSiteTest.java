package otussite;

import com.google.inject.Inject;
import extensions.UIExtensions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;
import pages.CatalogPage;
import pages.CoursePage;
import pages.MainPage;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(UIExtensions.class)
public class OtusSiteTest {
  @Inject
  private MainPage mainPage;
  @Inject
  private CatalogPage catalogPage;
  @Inject
  private CoursePage coursePage;

  @Test
  @DisplayName("Проверить название курса на станице курса")
  public void courseNameOnCoursePage_Test() {
    String requiredCourseName = "Python Developer";
    // Открываем главную страницу
    catalogPage.open();
    // Закрываем всплывающее окно
    catalogPage.closeNotification();
    // Находим курс по имени и кликаем по нему
    catalogPage.getCourseByName(requiredCourseName).click();
    // Проверяем, что название курса на странице курса совпадает с требуемым
    Assertions.assertEquals(requiredCourseName, coursePage.getCourseName());
  }

  @Test
  @DisplayName("Проверить дату начала и название курса для самых ранних и поздних курсов")
  public void extremumCourseNameAndDate_Test() {
    // Открываем главную страницу
    catalogPage.open();
    // Закрываем всплывающее окно
    catalogPage.closeNotification();
    // Получаем список отображаемых
    List<WebElement> displayedCourses = catalogPage.getDisplayedCourses();
    // Получаем список курсов с самой ранней и поздней датой начала
    List<WebElement> stratDateCursesExtremum = new ArrayList<>();
    stratDateCursesExtremum.addAll(catalogPage.getStratDateCursesExtremum(displayedCourses, true));
    stratDateCursesExtremum.addAll(catalogPage.getStratDateCursesExtremum(displayedCourses, false));
    // Проверяем, что дата начала и название курса в каталоге и на странице курса совпадают
    for (WebElement course : stratDateCursesExtremum) {
      Assertions.assertTrue(catalogPage.checkCourseDataOnCoursePage(course));
    }
  }

  @Test
  @DisplayName("Проверить открытие старницы каталога с выбранной категорий курсов")
  public void categoryCatalogPage_Test() {
    // Открываем главную страницу
    mainPage.open();
    // Закрываем всплывающее окно
    mainPage.closeNotification();
    // Фокус на выпадающее меню
    mainPage.showEducationMenu();
    // Получаем список категорий обучения
    List<WebElement> educationCategories = mainPage.getEducationCategories();
    // Выбираем случайную категорию
    WebElement randomCategory = mainPage.getRandomCategory(educationCategories);
    // Получаем название выбранной категории
    String categoryName = mainPage.getCategoryName(randomCategory);
    // Кликаем на выбранной категории и переходим на страницу каталога
    randomCategory.click();
    // Получаем список категорий с активными чекбоксами на странице каталога
    List<String> activeCategory = catalogPage.getActiveEducationCategoryNames();
    // Проверяем, что название выбранной категории есть в списке активных категорий
    Assertions.assertTrue(activeCategory.contains(categoryName));
  }
}
