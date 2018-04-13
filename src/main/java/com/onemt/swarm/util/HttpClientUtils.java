package com.onemt.swarm.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 项目名称：crawler-common
 * 类名称：HttpUtil httpClient4.5.3
 * 类描述： HTTP 请求工具类
 * 创建人：liqiao
 * 创建时间：2017年8月8日 下午4:53:39
 * 修改人：liqiao
 * 修改时间：2017年8月8日 下午4:53:39
 * 修改备注：
 * 
 * @version
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 120000;

    static {
        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create().register("https", createSSLConnSocketFactory()) // 支持https
                                                                                                                  // 忽略证书
                        .register("http", PlainConnectionSocketFactory.getSocketFactory()) // 支持http
                        .build();
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 设置连接池大小
        connMgr.setMaxTotal(800);
        connMgr.setValidateAfterInactivity(1000);
        connMgr.setDefaultMaxPerRoute(400);

        RequestConfig.Builder configBuilder = RequestConfig.custom().setConnectTimeout(MAX_TIMEOUT)// 设置连接超时
                .setSocketTimeout(MAX_TIMEOUT)// 设置读取超时
                .setConnectionRequestTimeout(MAX_TIMEOUT); // 设置从连接池获取连接实例的超时

        // 在提交请求之前 测试连接是否可用
        // configBuilder.setStaleConnectionCheckEnabled(true); 该方法过时 ==>setValidateAfterInactivity
        requestConfig = configBuilder.build();
    }

    /**
     * 创建SSL安全连接
     * 信任对方（https）所有证书
     * 
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            // 创建安全套接字对象
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();

            // 获取分层tls/ssl连接
            sslsf = new SSLConnectionSocketFactory(sslContext);

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }

    /**
     * 从连接池获取一个请求
     * 
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connMgr)
                .setDefaultRequestConfig(requestConfig).setConnectionManagerShared(true)
                .setUserAgent(
                        "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36")
                .build();
        /* CloseableHttpClient httpClient = HttpClients.createDefault();//如果不采用连接池就是这种方式获取连接 */
        return httpClient;
    }

    /**
     * 发送 GET 请求（HTTP,HTTPS），不带输入数据
     * 
     * @param url
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, new HashMap<String, Object>());
    }

    /**
     * 发送 GET 请求（HTTP,HTTPS），K-V形式
     * 
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, Object> params) {
        StringBuffer apiUrl = new StringBuffer(url);
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0)
                apiUrl.append("?");
            else
                apiUrl.append("&");
            apiUrl.append(key).append("=").append(params.get(key));
            i++;
        }

        String result = null;
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(apiUrl.toString());
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("====================url:{} 执行状态码 :{}======================", apiUrl.toString(), statusCode);
            if (HttpStatus.SC_OK == statusCode) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                }
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        } finally {
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(response);
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(httpClient);
        }
        return result;
    }

    /**
     * 发送 POST 请求（HTTP），不带输入数据
     * 
     * @param apiUrl
     * @return
     */
    public static String doPost(String apiUrl) {
        return doPost(apiUrl, new HashMap<String, Object>());
    }


    /**
     * 发送 SSL POST 请求（HTTP,HTTPS），K-V形式
     * 
     * @param apiUrl
     *        API接口URL
     * @param params
     *        参数map
     * @return
     */
    public static String doPost(String apiUrl, Map<String, Object> params) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String result = null;

        try {
            // httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("====================doPost url:{} 执行状态码 :{}======================", apiUrl.toString(),
                    statusCode);
            if (HttpStatus.SC_OK == statusCode) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("====================doPost 异常:{}======================", e.getMessage());
        } finally {
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(response);
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(httpClient);
        }
        return result;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），JSON形式
     * 
     * @param apiUrl
     *        API接口URL
     * @param json
     *        JSON对象
     * @return
     */
    public static String doPost(String apiUrl, String json) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String result = null;

        try {
            // httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json, "UTF-8");// 解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            // 构造消息头
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept-Type", "UTF-8");
            httpPost.setHeader("Connection", "Close");

            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("====================doPost url:{} 执行状态码 :{}======================", apiUrl.toString(),
                    statusCode);
            if (HttpStatus.SC_OK == statusCode) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("====================doPost 异常:{}======================", e.getMessage());
        } finally {
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(response);
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(httpClient);
        }
        return result;
    }

}
