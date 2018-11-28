package club.projectgaia.varys.service;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//@Service("httpClientManagerFactoryBean")
//public class HttpClientManagerFactoryBean implements FactoryBean<PoolingHttpClientConnectionManager>, InitializingBean, DisposableBean {
@Component
public class HttpClientManagerFactoryBean implements InitializingBean, DisposableBean {

    /**
     * FactoryBean生成的目标对象
     */
    private PoolingHttpClientConnectionManager connManager;

    @Autowired
    private ConnectionKeepAliveStrategy connectionKeepAliveStrategy;

    @Autowired
    private HttpRequestRetryHandler httpRequestRetryHandler;
    /*
    @Autowired
    private DefaultProxyRoutePlanner proxyRoutePlanner;
    */

    @Autowired
    private RequestConfig config;

    // 销毁上下文时，销毁HttpClient实例
    @Override
    public void destroy() throws Exception {
        /*
         * 调用httpClient.close()会先shut down connection manager，然后再释放该HttpClient所占用的所有资源，
         * 关闭所有在使用或者空闲的connection包括底层socket。由于这里把它所使用的connection manager关闭了，
         * 所以在下次还要进行http请求的时候，要重新new一个connection manager来build一个HttpClient,
         * 也就是在需要关闭和新建Client的情况下，connection manager不能是单例的.
         */
        if (null != this.connManager) {
            this.connManager.close();
            this.connManager = null;
        }
    }

    @Override// 初始化实例
    public void afterPropertiesSet() throws Exception {
        /*
         * 建议此处使用HttpClients.custom的方式来创建HttpClientBuilder，而不要使用HttpClientBuilder.create()方法来创建HttpClientBuilder
         * 从官方文档可以得出，HttpClientBuilder是非线程安全的，但是HttpClients确实Immutable的，immutable 对象不仅能够保证对象的状态不被改变，
         * 而且还可以不使用锁机制就能被其他线程共享
         */
        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();
        //设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connManager.setMaxTotal(200);
        connManager.setDefaultMaxPerRoute(20);

    }

    public CloseableHttpClient getHttpClient(){
        /*CloseableHttpClient httpClient = HttpClients.createDefault();//如果不采用连接池就是这种方式获取连接*/
        return HttpClients.custom().setConnectionManager(connManager)
                .setRetryHandler(httpRequestRetryHandler)
                .setKeepAliveStrategy(connectionKeepAliveStrategy)
                //.setRoutePlanner(proxyRoutePlanner)
                .setDefaultRequestConfig(config)
                .build();
    }


    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }
}
