package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.openqa.selenium.WebDriver;
import pages.CatalogPage;
import pages.CoursePage;
import pages.MainPage;

public class GuicePagesModule extends AbstractModule {

  private WebDriver driver;

  public GuicePagesModule(WebDriver deriver) {
    this.driver = deriver;
  }

  @Provides
  @Singleton
  public MainPage getMainPage() {
    return new MainPage(driver);
  }

  @Provides
  @Singleton
  public CatalogPage getCatalogPage() {
    return new CatalogPage(driver);
  }

  @Provides
  @Singleton
  public CoursePage getCoursePage() {
    return new CoursePage(driver);
  }
}
