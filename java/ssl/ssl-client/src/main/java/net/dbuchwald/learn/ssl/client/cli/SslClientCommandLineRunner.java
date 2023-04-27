package net.dbuchwald.learn.ssl.client.cli;

import net.dbuchwald.learn.ssl.client.SslClientApplication;
import net.dbuchwald.learn.ssl.client.service.GreetingClientService;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Component
public class SslClientCommandLineRunner implements CommandLineRunner {

  @Value("${http.client.ssl.key-store}")
  private Resource keyStoreResource;
  @Value("${http.client.ssl.key-store-password}")
  private String keyStorePassword;
  @Value("${http.client.ssl.trust-store}")
  private Resource trustStore;
  @Value("${http.client.ssl.trust-store-password}")
  private String trustStorePassword;

  private final Logger logger = LoggerFactory.getLogger(SslClientApplication.class);

  private final GreetingClientService greetingClientService;

  public SslClientCommandLineRunner(GreetingClientService greetingClientService) {
    this.greetingClientService = greetingClientService;
  }

  @Override
  public void run(String... args) throws UnrecoverableKeyException, CertificateException, IOException,
      NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    setUpDefaultSSLContext();
    logger.info("Retrieving greeting from service...");
    String greeting = greetingClientService.getGreetingMessage();
    logger.info("...done. Got the following message: [{}]", greeting);
  }

  private void setUpDefaultSSLContext() throws IOException, CertificateException, NoSuchAlgorithmException,
      KeyStoreException, KeyManagementException, UnrecoverableKeyException {

    KeyStore keyStore = KeyStore.getInstance("JKS");
    keyStore.load(new BufferedInputStream(keyStoreResource.getInputStream()), keyStorePassword.toCharArray());

    SSLContext sslContext = new SSLContextBuilder()
        .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
        .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
        .build();

    SSLContext.setDefault(sslContext);
  }
}
