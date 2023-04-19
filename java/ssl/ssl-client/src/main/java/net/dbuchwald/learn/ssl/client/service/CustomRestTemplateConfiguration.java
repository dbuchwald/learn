package net.dbuchwald.learn.ssl.client.service;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Configuration
public class CustomRestTemplateConfiguration {

  @Value("${http.client.ssl.key-store}")
  private Resource keyStoreResource;
  @Value("${http.client.ssl.key-store-password}")
  private String keyStorePassword;
  @Value("${http.client.ssl.trust-store}")
  private Resource trustStore;
  @Value("${http.client.ssl.trust-store-password}")
  private String trustStorePassword;

  @Bean
  public RestTemplate restTemplate() throws IOException,
                                            CertificateException,
                                            NoSuchAlgorithmException,
                                            KeyStoreException,
                                            KeyManagementException,
                                            UnrecoverableKeyException {

    KeyStore keyStore = KeyStore.getInstance("JKS");
    keyStore.load(new BufferedInputStream(keyStoreResource.getInputStream()), keyStorePassword.toCharArray());

    SSLContext sslContext = new SSLContextBuilder()
        .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
        .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
        .build();
    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
    return new RestTemplate(factory);
  }
}