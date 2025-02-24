package control.invest.IC.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BrapiConfig {
    @Value("${brapi.token}")
    private String brapiToken;

    public String getBrapiToken() {
        return brapiToken;
    }
}
