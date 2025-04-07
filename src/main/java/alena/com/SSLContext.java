package alena.com;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class SSLContext {
    public static javax.net.ssl.SSLContext newContext() throws KeyManagementException, NoSuchAlgorithmException {
        javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("TLS");
        TrustManager[] trustAllCertificates = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null; // Accepts all issuers
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                // No-op (trust all clients)
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                // No-op (trust all servers)
            }
        }};

        // Initialize the SSLContext with the TrustManager
        sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());
        return sslContext;
    }
}