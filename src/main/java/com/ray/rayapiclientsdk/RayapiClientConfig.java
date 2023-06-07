package com.ray.rayapiclientsdk;

import com.ray.rayapiclientsdk.client.RayapiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ray 2023/5/19
 */
@Data
@Configuration
@ConfigurationProperties("rayapimain.client")
@ComponentScan
public class RayapiClientConfig {
    private String publicKey;
    private String privateKey;

    @Bean
    public RayapiClient rayapiClient() {
        return new RayapiClient(publicKey, privateKey);
    }
}
