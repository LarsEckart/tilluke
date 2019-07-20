package tilluke.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.discovery.event.ServiceStartedEvent;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import javax.inject.Inject;
import javax.inject.Singleton;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.title;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Testcontainers
@MicronautTest
class Ui_should {

    @Inject
    EmbeddedServer server;

    //@Container
    private BrowserWebDriverContainer chrome =
            new BrowserWebDriverContainer()
                    .withCapabilities(new ChromeOptions())
                    .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("./build"));

    //@BeforeEach
    void setUp() {
        Configuration.baseUrl = "http://host.testcontainers.internal:" + server.getPort();
        RemoteWebDriver driver = chrome.getWebDriver();
        WebDriverRunner.setWebDriver(driver);
        Configuration.fastSetValue = true;
    }

    @Test
    void testExposedHost() throws Exception {
        GenericContainer container = new GenericContainer().withCommand("top");
        try {
            container.start();
            String response =
                    container.execInContainer("wget", "-O", "-", "http://host.testcontainers.internal:" + server.getPort()).getStdout();
            assertEquals("Hello World!", response);
        } finally {
            container.stop();
        }
    }
    @Disabled
    @Test
    void have_title_on_index_page() {
        open("/views");
        assertThat(title()).isEqualTo("tilluke");
    }

    @Singleton
    static class DoOnStartup implements ApplicationEventListener<ServerStartupEvent> {

        @Override
        public void onApplicationEvent(ServerStartupEvent event) {
            org.testcontainers.Testcontainers.exposeHostPorts(event.getSource().getPort());
            System.out.println("birx" + event.getSource().getPort());
        }
    }
}
