# This folder contains all the source code required for example mTLS communication between REST client and REST server

The following steps are required to make this run:
 * Generate self-signed Root Certificate Authority
 * Generate server certificate for HTTPS connectivity
 * Sign the server certificate with Root CA certificate
 * Import signed certificate into server keystore
 * Export Root CA certificate into client truststore
 * (OPTIONAL) Export PEM file to be used for cURL testing
 * (OPTIONAL) Use cURL to test the server with HTTPS enabled
 * Generate client certificate for mTLS connection
 * Sign the client certificate with Root CA certificate
 * Export Root CA certificate to server truststore
 * (OPTIONAL) Export client public key to be used for cURL testing
 * (OPTIONAL) Use cURL to test mTLS communication
 * Run server/client to establish mTLS connection

## Initial setup

Run `setup.sh` script to generate all the required certificates and put them into client/server folders

## Running server and testing it with pure HTTP

Run the following command:

```shell
mvn -f ssl-server/pom.xml clean spring-boot:run
```

Test it with simple cURL call:

```shell
curl --verbose http://localhost:8080/hello
```

You expect the following output:

```
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /hello HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.85.0
> Accept: */*
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 12
< Date: Fri, 14 Apr 2023 13:28:56 GMT
< 
* Connection #0 to host localhost left intact
Hello World! 
```

Test it now with the client application:

```shell
mvn -f ssl-client/pom.xml clean spring-boot:run
```

You expect output to be similar to the following:

```
[INFO] Attaching agents: []

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.0.6)

2023-04-27T20:29:25.355+02:00  INFO 36296 --- [           main] n.d.l.ssl.client.SslClientApplication    : Starting SslClientApplication using Java 17.0.6 with PID 36296 (/home/dawid/Development/learn/java/ssl/ssl-client/target/classes started by dawid in /home/dawid/Development/learn/java/ssl/ssl-client)
2023-04-27T20:29:25.360+02:00  INFO 36296 --- [           main] n.d.l.ssl.client.SslClientApplication    : No active profile set, falling back to 1 default profile: "default"
2023-04-27T20:29:25.833+02:00  INFO 36296 --- [           main] n.d.l.ssl.client.SslClientApplication    : Started SslClientApplication in 0.782 seconds (process running for 1.008)
2023-04-27T20:29:26.030+02:00  INFO 36296 --- [           main] n.d.l.ssl.client.SslClientApplication    : Retrieving greeting from service...
2023-04-27T20:29:26.132+02:00  INFO 36296 --- [           main] n.d.l.ssl.client.SslClientApplication    : ...done. Got the following message: [Hello World!]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.350 s
[INFO] Finished at: 2023-04-27T20:29:26+02:00
[INFO] ------------------------------------------------------------------------
```

Stop the server (kill `mvn` process).

## Running server with HTTPS enabled

Run the server with `https` profile enabled:

```shell
mvn -f ssl-server/pom.xml -Dspring-boot.run.profiles=https clean spring-boot:run
```

Try without Root CA certificate:

```shell
curl --verbose https://localhost:8443/hello
```

It fails due to server certificate not being in trusted certificate chain:

```
*   Trying 127.0.0.1:8443...
* Connected to localhost (127.0.0.1) port 8443 (#0)
* ALPN: offers h2
* ALPN: offers http/1.1
*  CAfile: /etc/pki/tls/certs/ca-bundle.crt
*  CApath: none
* TLSv1.0 (OUT), TLS header, Certificate Status (22):
* TLSv1.3 (OUT), TLS handshake, Client hello (1):
* TLSv1.2 (IN), TLS header, Certificate Status (22):
* TLSv1.3 (IN), TLS handshake, Server hello (2):
* TLSv1.2 (IN), TLS header, Finished (20):
* TLSv1.2 (IN), TLS header, Supplemental data (23):
* TLSv1.3 (IN), TLS handshake, Encrypted Extensions (8):
* TLSv1.3 (IN), TLS handshake, Certificate (11):
* TLSv1.2 (OUT), TLS header, Unknown (21):
* TLSv1.3 (OUT), TLS alert, unknown CA (560):
* SSL certificate problem: self-signed certificate in certificate chain
* Closing connection 0
curl: (60) SSL certificate problem: self-signed certificate in certificate chain
More details here: https://curl.se/docs/sslcerts.html

curl failed to verify the legitimacy of the server and therefore could not
establish a secure connection to it. To learn more about this situation and
how to fix it, please visit the web page mentioned above.
```

Try again, but with Root CA certificate:

```shell
curl --verbose --cacert certs/root_ca.pem https://localhost:8443/hello
```

It works now as expected:

```
curl --verbose --cacert certs/root_ca.pem https://localhost:8443/hello
*   Trying 127.0.0.1:8443...
* Connected to localhost (127.0.0.1) port 8443 (#0)
* ALPN: offers h2
* ALPN: offers http/1.1
*  CAfile: certs/root_ca.pem
*  CApath: none
* TLSv1.0 (OUT), TLS header, Certificate Status (22):
* TLSv1.3 (OUT), TLS handshake, Client hello (1):
* TLSv1.2 (IN), TLS header, Certificate Status (22):
* TLSv1.3 (IN), TLS handshake, Server hello (2):
* TLSv1.2 (IN), TLS header, Finished (20):
* TLSv1.2 (IN), TLS header, Supplemental data (23):
* TLSv1.3 (IN), TLS handshake, Encrypted Extensions (8):
* TLSv1.3 (IN), TLS handshake, Certificate (11):
* TLSv1.3 (IN), TLS handshake, CERT verify (15):
* TLSv1.3 (IN), TLS handshake, Finished (20):
* TLSv1.2 (OUT), TLS header, Finished (20):
* TLSv1.3 (OUT), TLS change cipher, Change cipher spec (1):
* TLSv1.2 (OUT), TLS header, Supplemental data (23):
* TLSv1.3 (OUT), TLS handshake, Finished (20):
* SSL connection using TLSv1.3 / TLS_AES_256_GCM_SHA384
* ALPN: server did not agree on a protocol. Uses default.
* Server certificate:
*  subject: C=PL; O=dbuchwald.net; OU=SSL; CN=localhost
*  start date: Apr 14 14:06:02 2023 GMT
*  expire date: Jul 13 14:06:02 2023 GMT
*  common name: localhost (matched)
*  issuer: C=PL; O=dbuchwald.net; OU=CA; CN=Root Certificate Authority
*  SSL certificate verify ok.
* TLSv1.2 (OUT), TLS header, Supplemental data (23):
> GET /hello HTTP/1.1
> Host: localhost:8443
> User-Agent: curl/7.85.0
> Accept: */*
> 
* TLSv1.2 (IN), TLS header, Supplemental data (23):
* TLSv1.3 (IN), TLS handshake, Newsession Ticket (4):
* TLSv1.2 (IN), TLS header, Supplemental data (23):
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 12
< Date: Sat, 15 Apr 2023 10:54:32 GMT
< 
* Connection #0 to host localhost left intact
Hello World!
```

Use the client now with support for HTTPS enabled:

```shell
mvn -f ssl-client/pom.xml -Dspring-boot.run.profiles=https clean spring-boot:run
```

You expect it to work correctly:

```
[INFO] Attaching agents: []

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.0.6)

2023-04-27T20:31:26.188+02:00  INFO 36770 --- [           main] n.d.l.ssl.client.SslClientApplication    : Starting SslClientApplication using Java 17.0.6 with PID 36770 (/home/dawid/Development/learn/java/ssl/ssl-client/target/classes started by dawid in /home/dawid/Development/learn/java/ssl/ssl-client)
2023-04-27T20:31:26.191+02:00  INFO 36770 --- [           main] n.d.l.ssl.client.SslClientApplication    : The following 1 profile is active: "https"
2023-04-27T20:31:26.705+02:00  INFO 36770 --- [           main] n.d.l.ssl.client.SslClientApplication    : Started SslClientApplication in 0.814 seconds (process running for 1.066)
2023-04-27T20:31:26.909+02:00  INFO 36770 --- [           main] n.d.l.ssl.client.SslClientApplication    : Retrieving greeting from service...
2023-04-27T20:31:27.273+02:00  INFO 36770 --- [           main] n.d.l.ssl.client.SslClientApplication    : ...done. Got the following message: [Hello World!]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.835 s
[INFO] Finished at: 2023-04-27T20:31:27+02:00
[INFO] ------------------------------------------------------------------------
```

Stop the application.

## Running server with mTLS enabled

Start the application again, but with two profiles - `https` and `mtls`:

```shell
mvn -f ssl-server/pom.xml -Dspring-boot.run.profiles=https,mtls clean spring-boot:run
```

Test it first without client certificate:

```shell
curl --verbose --cacert certs/root_ca.pem https://localhost:8443/hello
```

It will not work:

```
curl --verbose --cacert certs/root_ca.pem https://localhost:8443/hello
*   Trying 127.0.0.1:8443...
* Connected to localhost (127.0.0.1) port 8443 (#0)
* ALPN: offers h2
* ALPN: offers http/1.1
*  CAfile: certs/root_ca.pem
*  CApath: none
* TLSv1.0 (OUT), TLS header, Certificate Status (22):
* TLSv1.3 (OUT), TLS handshake, Client hello (1):
* TLSv1.2 (IN), TLS header, Certificate Status (22):
* TLSv1.3 (IN), TLS handshake, Server hello (2):
* TLSv1.2 (IN), TLS header, Finished (20):
* TLSv1.2 (IN), TLS header, Supplemental data (23):
* TLSv1.3 (IN), TLS handshake, Encrypted Extensions (8):
* TLSv1.3 (IN), TLS handshake, Request CERT (13):
* TLSv1.3 (IN), TLS handshake, Certificate (11):
* TLSv1.3 (IN), TLS handshake, CERT verify (15):
* TLSv1.3 (IN), TLS handshake, Finished (20):
* TLSv1.2 (OUT), TLS header, Finished (20):
* TLSv1.3 (OUT), TLS change cipher, Change cipher spec (1):
* TLSv1.2 (OUT), TLS header, Supplemental data (23):
* TLSv1.3 (OUT), TLS handshake, Certificate (11):
* TLSv1.2 (OUT), TLS header, Supplemental data (23):
* TLSv1.3 (OUT), TLS handshake, Finished (20):
* SSL connection using TLSv1.3 / TLS_AES_256_GCM_SHA384
* ALPN: server did not agree on a protocol. Uses default.
* Server certificate:
*  subject: C=PL; O=dbuchwald.net; OU=SSL; CN=localhost
*  start date: Apr 14 14:06:02 2023 GMT
*  expire date: Jul 13 14:06:02 2023 GMT
*  common name: localhost (matched)
*  issuer: C=PL; O=dbuchwald.net; OU=CA; CN=Root Certificate Authority
*  SSL certificate verify ok.
* TLSv1.2 (OUT), TLS header, Supplemental data (23):
> GET /hello HTTP/1.1
> Host: localhost:8443
> User-Agent: curl/7.85.0
> Accept: */*
> 
* TLSv1.2 (IN), TLS header, Supplemental data (23):
* TLSv1.3 (IN), TLS alert, bad certificate (554):
* OpenSSL SSL_read: error:0A000412:SSL routines::sslv3 alert bad certificate, errno 0
* Closing connection 0
curl: (56) OpenSSL SSL_read: error:0A000412:SSL routines::sslv3 alert bad certificate, errno 0
```

Try again, but with client certificate provided:

```shell
curl --verbose --cacert certs/root_ca.pem --cert-type P12 --cert ./certs/client-identity.p12:changeit https://localhost:8443/hello
```

Works as a charm:

```
curl --verbose --cacert certs/root_ca.pem --cert-type P12 --cert ./certs/client-identity.p12:changeit https://localhost:8443/hello
*   Trying 127.0.0.1:8443...
* Connected to localhost (127.0.0.1) port 8443 (#0)
* ALPN: offers h2
* ALPN: offers http/1.1
*  CAfile: certs/root_ca.pem
*  CApath: none
* TLSv1.0 (OUT), TLS header, Certificate Status (22):
* TLSv1.3 (OUT), TLS handshake, Client hello (1):
* TLSv1.2 (IN), TLS header, Certificate Status (22):
* TLSv1.3 (IN), TLS handshake, Server hello (2):
* TLSv1.2 (IN), TLS header, Finished (20):
* TLSv1.2 (IN), TLS header, Supplemental data (23):
* TLSv1.3 (IN), TLS handshake, Encrypted Extensions (8):
* TLSv1.3 (IN), TLS handshake, Request CERT (13):
* TLSv1.3 (IN), TLS handshake, Certificate (11):
* TLSv1.3 (IN), TLS handshake, CERT verify (15):
* TLSv1.3 (IN), TLS handshake, Finished (20):
* TLSv1.2 (OUT), TLS header, Finished (20):
* TLSv1.3 (OUT), TLS change cipher, Change cipher spec (1):
* TLSv1.2 (OUT), TLS header, Supplemental data (23):
* TLSv1.3 (OUT), TLS handshake, Certificate (11):
* TLSv1.2 (OUT), TLS header, Supplemental data (23):
* TLSv1.3 (OUT), TLS handshake, CERT verify (15):
* TLSv1.2 (OUT), TLS header, Supplemental data (23):
* TLSv1.3 (OUT), TLS handshake, Finished (20):
* SSL connection using TLSv1.3 / TLS_AES_256_GCM_SHA384
* ALPN: server did not agree on a protocol. Uses default.
* Server certificate:
*  subject: C=PL; O=dbuchwald.net; OU=SSL; CN=localhost
*  start date: Apr 14 14:06:02 2023 GMT
*  expire date: Jul 13 14:06:02 2023 GMT
*  common name: localhost (matched)
*  issuer: C=PL; O=dbuchwald.net; OU=CA; CN=Root Certificate Authority
*  SSL certificate verify ok.
* TLSv1.2 (OUT), TLS header, Supplemental data (23):
> GET /hello HTTP/1.1
> Host: localhost:8443
> User-Agent: curl/7.85.0
> Accept: */*
> 
* TLSv1.2 (IN), TLS header, Supplemental data (23):
* TLSv1.3 (IN), TLS handshake, Newsession Ticket (4):
* TLSv1.2 (IN), TLS header, Supplemental data (23):
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 12
< Date: Sat, 15 Apr 2023 10:58:33 GMT
< 
* Connection #0 to host localhost left intact
Hello World!
```

Finally, test with the client application (note the same profile as previously):

```shell
mvn -f ssl-client/pom.xml -Dspring-boot.run.profiles=https clean spring-boot:run
```

You expect it to run just fine:

```
[INFO] Attaching agents: []

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.0.6)

2023-04-27T20:32:43.360+02:00  INFO 37089 --- [           main] n.d.l.ssl.client.SslClientApplication    : Starting SslClientApplication using Java 17.0.6 with PID 37089 (/home/dawid/Development/learn/java/ssl/ssl-client/target/classes started by dawid in /home/dawid/Development/learn/java/ssl/ssl-client)
2023-04-27T20:32:43.364+02:00  INFO 37089 --- [           main] n.d.l.ssl.client.SslClientApplication    : The following 1 profile is active: "https"
2023-04-27T20:32:43.901+02:00  INFO 37089 --- [           main] n.d.l.ssl.client.SslClientApplication    : Started SslClientApplication in 0.85 seconds (process running for 1.123)
2023-04-27T20:32:44.113+02:00  INFO 37089 --- [           main] n.d.l.ssl.client.SslClientApplication    : Retrieving greeting from service...
2023-04-27T20:32:44.587+02:00  INFO 37089 --- [           main] n.d.l.ssl.client.SslClientApplication    : ...done. Got the following message: [Hello World!]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  4.026 s
[INFO] Finished at: 2023-04-27T20:32:44+02:00
[INFO] ------------------------------------------------------------------------
```

## Additional notes/gotchas

This exercise was executed in both Windows 10 and Linux (Fedora 37 Workstation) environments, and there is a number of
details that are easy to miss.

### Client certificate handling difference between cURL and JSSE (Windows only)

When using client certificate in cURL on Windows, it will pick it up from `client-identity.p12` file only if it was
imported into the keystore as a separate entry, hence the double import of signed certificate response (as a `client` and
`client-public`).

Interestingly enough, cURL on Linux seems to work just fine even if the signed certificate is just a part of private key
pair (`client`).

*To be investigated further!*

### X.509 Certificate extension required for Linux cURL

When generating initial self-signed certificate for Root Certificate Authority it must be specified with extension BC:C
for it to be correctly recognized as a CA by cURL on Linux. Most probably this has something to do with Linux version of 
underlying `openssl` library that requires this extension to be able to build chain of trust to root CA.

Interestingly enough, in Windows environment this extension was not required.

Troubleshooting this particular issue was really tricky. When invoking cURL the following message was received:

```
curl --verbose --cacert certs/root_ca.pem https://localhost:8443/hello
*   Trying 127.0.0.1:8443...
* Connected to localhost (127.0.0.1) port 8443 (#0)
* ALPN: offers h2
* ALPN: offers http/1.1
*  CAfile: certs/root_ca.pem
*  CApath: none
* TLSv1.0 (OUT), TLS header, Certificate Status (22):
* TLSv1.3 (OUT), TLS handshake, Client hello (1):
* TLSv1.2 (IN), TLS header, Certificate Status (22):
* TLSv1.3 (IN), TLS handshake, Server hello (2):
* TLSv1.2 (IN), TLS header, Finished (20):
* TLSv1.2 (IN), TLS header, Supplemental data (23):
* TLSv1.3 (IN), TLS handshake, Encrypted Extensions (8):
* TLSv1.3 (IN), TLS handshake, Certificate (11):
* TLSv1.2 (OUT), TLS header, Unknown (21):
* TLSv1.3 (OUT), TLS alert, unknown CA (560):
* SSL certificate problem: invalid CA certificate
* Closing connection 0
curl: (60) SSL certificate problem: invalid CA certificate
More details here: https://curl.se/docs/sslcerts.html

curl failed to verify the legitimacy of the server and therefore could not
establish a secure connection to it. To learn more about this situation and
how to fix it, please visit the web page mentioned above.
```

This doesn't provide sufficient information to resolve the issue, but using `openssl` utility provides more details:

```
openssl s_client -CAfile ./certs/root_ca.pem -connect localhost:8443
CONNECTED(00000003)
Can't use SSL_get_servername
depth=1 C = PL, O = dbuchwald.net, OU = CA, CN = Root Certificate Authority
verify error:num=79:invalid CA certificate
verify return:1
depth=1 C = PL, O = dbuchwald.net, OU = CA, CN = Root Certificate Authority
verify error:num=26:unsuitable certificate purpose
verify return:1
depth=1 C = PL, O = dbuchwald.net, OU = CA, CN = Root Certificate Authority
verify return:1
depth=0 C = PL, O = dbuchwald.net, OU = SSL, CN = localhost
verify return:1
---
Certificate chain
 0 s:C = PL, O = dbuchwald.net, OU = SSL, CN = localhost
   i:C = PL, O = dbuchwald.net, OU = CA, CN = Root Certificate Authority
   a:PKEY: rsaEncryption, 4096 (bit); sigalg: RSA-SHA384
   v:NotBefore: Apr 15 11:18:40 2023 GMT; NotAfter: Jul 14 11:18:40 2023 GMT
 1 s:C = PL, O = dbuchwald.net, OU = CA, CN = Root Certificate Authority
   i:C = PL, O = dbuchwald.net, OU = CA, CN = Root Certificate Authority
   a:PKEY: rsaEncryption, 4096 (bit); sigalg: RSA-SHA384
   v:NotBefore: Apr 15 11:18:38 2023 GMT; NotAfter: Jul 14 11:18:38 2023 GMT
---
Server certificate
-----BEGIN CERTIFICATE-----
MIIFYjCCA0qgAwIBAgIIDKOsx+sN1MIwDQYJKoZIhvcNAQEMBQAwVzELMAkGA1UE
BhMCUEwxFjAUBgNVBAoTDWRidWNod2FsZC5uZXQxCzAJBgNVBAsTAkNBMSMwIQYD
VQQDExpSb290IENlcnRpZmljYXRlIEF1dGhvcml0eTAeFw0yMzA0MTUxMTE4NDBa
Fw0yMzA3MTQxMTE4NDBaMEcxCzAJBgNVBAYTAlBMMRYwFAYDVQQKEw1kYnVjaHdh
bGQubmV0MQwwCgYDVQQLEwNTU0wxEjAQBgNVBAMTCWxvY2FsaG9zdDCCAiIwDQYJ
KoZIhvcNAQEBBQADggIPADCCAgoCggIBANQgsCzP7Y9FrcT1bLPLA4GRDPgz8lGH
Dnxg6mFUJPGTUNtRVwgowltZ1awUv10r84QuwLttjtuMQrZSKB4pqpxOqMjRaQ9S
IbAIDQj6R3VsjcZ0MWJs+5mnuJDuJWXBOR+G2wOIi+WWbnuoe5ddVk0D8V9r9Xs6
GYT2wTYX6JPemUpomZ7J5+hOsyEDFvpzBU9WIjF18fxks97sdDykpzdUja2+3DQN
Fnxu4XB3b7sK5+ml58dMp/gLxwwdKixzBZh0aDE2x2waWUsE7kV1hKk206q2gIRo
eo/YokMCLFzUeMDD94XEB4MFdBjQ5x7hZEKAYTPFP7Lc+VsZdeCtO9uM+rc/Rw7L
+T24fkUMHvQapqhQ+IeeHOlH2S+LAnU6dDXLvhyluXOxBcQPAMPElSh/FjNPhh5g
2Ji+h8YO5uPiJ9msFIb6fVkv49PrY/rxLMSa2oAQlBo9noGdzZ9X1Og0yn9v3Goo
ikyMxCM1AfHj05oBlxcaWql0sYP6+3HHKnTcOjIaIDyXjvOO+aucpzrHiVsZnxDa
ZG+nsce0cxL67hUyh3FN5Q5RuiC6T17Fdaj0RjiSs9yDBdMX2cORYuikjo3r5nNd
Jx/YIMZCnDmDmGnGzJptTstsERW64ERp4i3oljYyke0lrhXLFJ2Q024TP1HMaBEM
Zt974kdUNibJAgMBAAGjQjBAMB0GA1UdDgQWBBS1AlJEZ1t0QbZGjLep6dIpfxr3
jjAfBgNVHSMEGDAWgBSZUl9ou8V9OPl2ybvncyJBuw+x1DANBgkqhkiG9w0BAQwF
AAOCAgEAUU0QHEaRyJIS5yKV0yk41zrGUpf+1z8JT3bObWpIH8AqsaXTGHzzOUIz
XN4RAedcYIKIsNVr/UtQEHB0kwD7RrdUx02mKIs+a0ZTfFnsYriqvElgXfTk2Jm5
krtrwVIUf/QWZzMjreOnARL4CejBRsvbwod11R0grLpJRBzEMafyeO3Q3tlPFkBb
Uzbt69N/8gakW3oP9GEx/M5vdQ+ot1poplWKCWwtPY+0s5zakpqHd8lN2J1dQ0hI
LIcLaSd4oYaFzt3ame+vmKV/z/UQWdG1MKOrkvb9emi4RbmjvapCo8S62OTB4S0h
DWwGkAW0N1N6GfH2qAftlyiFGQiWyJD2euyRowTyy0zxXlliYqOwk3T48achnI4+
Ghbt5PHRr61LW6ElihGVmSp9jJirmT9IgXaxnSqYGFo+6/Vh+yEVgKoo5J9ICAZL
gUabeamvnwtif5hEb735++bCwmoePKd8zaHtfQ9t4cyv9XritLF24k+eBExTIu7b
b9uK2ls9f6ha0JwFYxr147R23WyckiJeqqK9tgNSAlN84EKKmxkUGwR8ctIqvJZB
JQAjF2ok8K3AVaGHSw5TA34mwjWJ4PyDk1v5zwHJZxlzYPxsw5Q+teV6W+IxsAzx
tpA3++PGP5wfqPtvuTnN4kOTj2sCV98mfxWfb5R4Nmk/OEPFJAA=
-----END CERTIFICATE-----
subject=C = PL, O = dbuchwald.net, OU = SSL, CN = localhost
issuer=C = PL, O = dbuchwald.net, OU = CA, CN = Root Certificate Authority
---
No client certificate CA names sent
Peer signing digest: SHA256
Peer signature type: RSA-PSS
Server Temp Key: X25519, 253 bits
---
SSL handshake has read 3540 bytes and written 375 bytes
Verification error: unsuitable certificate purpose
---
New, TLSv1.3, Cipher is TLS_AES_256_GCM_SHA384
Server public key is 4096 bit
Secure Renegotiation IS NOT supported
Compression: NONE
Expansion: NONE
No ALPN negotiated
Early data was not sent
Verify return code: 26 (unsuitable certificate purpose)
---
```

When generated all certificates again, but with Root CA containing `BC:C` extension, it is possible to pass `openssl` 
verification:

```
openssl s_client -CAfile ./certs/root_ca.pem -connect localhost:8443
CONNECTED(00000003)
Can't use SSL_get_servername
depth=1 C = PL, O = dbuchwald.net, OU = CA, CN = Root Certificate Authority
verify return:1
depth=0 C = PL, O = dbuchwald.net, OU = SSL, CN = localhost
verify return:1
---
Certificate chain
 0 s:C = PL, O = dbuchwald.net, OU = SSL, CN = localhost
   i:C = PL, O = dbuchwald.net, OU = CA, CN = Root Certificate Authority
   a:PKEY: rsaEncryption, 4096 (bit); sigalg: RSA-SHA384
   v:NotBefore: Apr 15 11:26:18 2023 GMT; NotAfter: Jul 14 11:26:18 2023 GMT
 1 s:C = PL, O = dbuchwald.net, OU = CA, CN = Root Certificate Authority
   i:C = PL, O = dbuchwald.net, OU = CA, CN = Root Certificate Authority
   a:PKEY: rsaEncryption, 4096 (bit); sigalg: RSA-SHA384
   v:NotBefore: Apr 15 11:26:14 2023 GMT; NotAfter: Jul 14 11:26:14 2023 GMT
---
Server certificate
-----BEGIN CERTIFICATE-----
MIIFYzCCA0ugAwIBAgIJAO6bjns61JMLMA0GCSqGSIb3DQEBDAUAMFcxCzAJBgNV
BAYTAlBMMRYwFAYDVQQKEw1kYnVjaHdhbGQubmV0MQswCQYDVQQLEwJDQTEjMCEG
A1UEAxMaUm9vdCBDZXJ0aWZpY2F0ZSBBdXRob3JpdHkwHhcNMjMwNDE1MTEyNjE4
WhcNMjMwNzE0MTEyNjE4WjBHMQswCQYDVQQGEwJQTDEWMBQGA1UEChMNZGJ1Y2h3
YWxkLm5ldDEMMAoGA1UECxMDU1NMMRIwEAYDVQQDEwlsb2NhbGhvc3QwggIiMA0G
CSqGSIb3DQEBAQUAA4ICDwAwggIKAoICAQCQY8sg3gaaBBEq4V2uPyp+4YP0tQRW
La6+ggXgFlkyAuUT4PZu2tWVU94BlJorEq17E2DvrQV/aIuOLCDVzesL8/22CbLa
SoS2N+d3qpxWbOQQQhga8ixtaHBih89LP6oI7CuxFZTKY+6gYRBqO/Wc8wSDfoEk
gRshcKX3sfcFdAwcraH8XCL+2m5i4RoaPVbZrzZT7nV7c29yeHDsHdddYztqlCcm
kU7tZ8d0KSvDAQxTzGVj0SYj3ykEQKqp+mn5Nq/kl0+FRBLNkL39cj7kG27KTY+v
+eeRiUjahBlR6qbbvRTpQwJO3IJuM9Dn5D/PZ0cuMwcPB35nUVYCSe+CG2A7pW9v
zgCMixGQfva3umzHVqQiu3CCzWwWwWN5Z8nkWRrqd6dhwrnquOi6mzm2yYxldbxN
thYgi7F1LEuEvEzP78CRVtJ9iyr7lxeld/xUUzL0G521u2B/RMoJOMLOawj+iwvI
/XlIe5L7yUqj+F/Q5wScuIsCClrnbl6HWwK/r8xqclNGmhivyrT+pB1t0BL/Ar7L
amp9LUl24IWaqKQK1iMSnmlGUwibYxx1nzSk17csdQyL8Byg6fxI8VyUX2XIFVuH
nJBigFzxwX0EtgVS0uGcdw/8PpRfxBkDf1+RkUoihhylbsF50xNJB7z0R4aOBKi+
/yFOk3XY4lvzkwIDAQABo0IwQDAdBgNVHQ4EFgQUwXH0ttnFjxF2yt/DDsKy7Ps3
6hYwHwYDVR0jBBgwFoAUjLAL6qeQZkkpsOHyYyKd7B6aigAwDQYJKoZIhvcNAQEM
BQADggIBAN9vIPAhIFcr8uZfjHFpceVywFQQ5P9t2M3YPzL65EDUwzgELvybF8Li
xjEghV+sGNlWdGTJzANqs7z8RtAucz6gEfzMbn745/Ce8ra/cwrRNZqYhRmUeFwT
7yGrDDSXVnVCvqLe4pDxFRZ2CyDEyRCnwiKnx9GjdzF4KQLaJqM59AEN8tqYWUkt
dTiRPr5E4Xm58HPqhrt3O1l1zbJZWeHjsHo6jpArSuokvv/B/7U5JZRR3G2r8F5i
oHrscuJFjn8frMtIuIy/hldGvNn2bYFOVEu5dOo9YB1MZ9/sVa0ue9ZiTIP7CLQE
kLnMJhWvyV/10s43tNrpacNBjwujCz00PdJX1Md8xbntdyg5JfJ1ogFFfHLFA+LR
PsN25QWeHCWnqn3HdEXGyI0ZRZAsbp1QP1/N5oSCptr4/GyPZoDheJgkMvFS9g5B
XPwvLNyMxN4y3AFShpRm3HCo41MrAvT/wlCP1ZUGEeoPkokXkTKILfTQpiyGY9KP
PkTpN+gNPPtGKfjFfbaRklePdEG8TUU5pWjF3ahoKebsvPozXnQHcgmqsjLwvnq4
ftr+sXVo+M03oaQK0ZZl76++D13JoIb6PvnD8Zs3B4LT+9T4nZv7Hnu5zKUP9/7Y
mmzBIx0pEROMqbFuv1can0FK+LUKlZhvcJBeiBwz2nQG8F7h+Yky
-----END CERTIFICATE-----
subject=C = PL, O = dbuchwald.net, OU = SSL, CN = localhost
issuer=C = PL, O = dbuchwald.net, OU = CA, CN = Root Certificate Authority
---
No client certificate CA names sent
Peer signing digest: SHA256
Peer signature type: RSA-PSS
Server Temp Key: X25519, 253 bits
---
SSL handshake has read 3558 bytes and written 375 bytes
Verification: OK
---
New, TLSv1.3, Cipher is TLS_AES_256_GCM_SHA384
Server public key is 4096 bit
Secure Renegotiation IS NOT supported
Compression: NONE
Expansion: NONE
No ALPN negotiated
Early data was not sent
Verify return code: 0 (ok)
---
```

And, obviously, cURL runs fine.

###

Getting detailed logs of SSL handshake with Maven Spring Boot plugin:

```shell
mvn -f ssl-client/pom.xml -Dspring-boot.run.jvmArguments="-Djavax.net.debug=all" -Dspring-boot.run.profiles=https clean spring-boot:run
```