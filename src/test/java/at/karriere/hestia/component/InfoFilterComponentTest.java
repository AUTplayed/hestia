package at.karriere.hestia.component;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InfoFilterComponentTest {

    InfoFilterComponent infoFilterComponent = new InfoFilterComponent();
    String propinfo = "redis_version: 1\nasd: 1\nuptime_in_seconds:52866\n";
    String propfiltered = "redis_version: 1\nuptime(dd-HH-mm-ss):00-14-41-05\n";
    String catinfo = "# Memory\nprop\nprop\n\n# No\nno\n\n# No2\nno\n";
    String catfiltered = "# Memory\nprop\nprop\n\n";
    String info = "# Info\nredis_version: 1\nno\n\n# No\nno\n\n";
    String filtered = "# Info\nredis_version: 1\n\n";

    @Test
    public void testProperty() {
        String result = infoFilterComponent.filter(propinfo);
        assertThat(result).as("check if properties get filtered").isEqualTo(propfiltered);
    }

    @Test
    public void testCategory() {
        String result = infoFilterComponent.filter(catinfo);
        assertThat(result).as("check if categories get filtered").isEqualTo(catfiltered);
    }

    @Test
    public void testPropertyAndCategory() {
        String result = infoFilterComponent.filter(info);
        assertThat(result).as("check if properties and categories get filtered").isEqualTo(filtered);
    }
}
