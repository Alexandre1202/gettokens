package br.com.aab.tokens.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONObject;

public class TokenFactory {

  public static void main(String[] args) {
    TokenFactory login = new TokenFactory();
    IDReader cpfReader = new IDReader();
    cpfReader.getCPFs().forEach(cpf -> System.out.println(login.postLogin(cpf)));
  }

  public String postLogin(String cpf) {
    String encode = "UTF-8";
    String loginEP = "https://192.168.246.242:8443/v1/login/system";
    Map<String, Object> mapJson = null;
    StringBuffer responseSB = new StringBuffer();
    try {
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
          @Override
          public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates,
              String s) throws CertificateException {          }

          @Override
          public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates,
              String s) throws CertificateException {          }

          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
          }
        }
      };

      JSONObject redeem = new JSONObject();
      redeem.put("cpf", cpf);
      redeem.put("system", "genesys");
      byte[] bodyArrayByte = redeem.toString().getBytes(encode);

      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
          return false;
        }
      };

      javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
          new javax.net.ssl.HostnameVerifier(){

            public boolean verify(String hostname,
                javax.net.ssl.SSLSession sslSession) {
              return hostname.equals("192.168.246.242");
            }
          });

      HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(loginEP).openConnection();
      httpsURLConnection.setRequestProperty("Content-Type", "application/json; charset="+encode);
      httpsURLConnection.setRequestProperty("Content-Length", String.valueOf(bodyArrayByte));
      httpsURLConnection.setRequestProperty("Accept", "application/json");
      httpsURLConnection.setRequestMethod("POST");
      httpsURLConnection.setDoOutput(true);
      httpsURLConnection.setDoInput(true);

      httpsURLConnection.getOutputStream().write(bodyArrayByte);
      Reader reader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream(), encode));
      for (int c; (c = reader.read()) >= 0;)
        responseSB.append((char)c);
      System.out.println(responseSB.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (KeyManagementException e) {
      e.printStackTrace();
    }
    return responseSB.toString();
  }
}
