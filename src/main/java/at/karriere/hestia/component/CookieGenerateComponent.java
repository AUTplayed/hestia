package at.karriere.hestia.component;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
public class CookieGenerateComponent {

    public String generate() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

}