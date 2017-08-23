package at.karriere.hestia.component;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CookieGenerateComponentTest {

    CookieGenerateComponent cookieGenerateComponent = new CookieGenerateComponent();

    @Test
    public void test() {
        String random = cookieGenerateComponent.generate();
        assertThat(random).as("check random string size").hasSize(32);
    }
}
