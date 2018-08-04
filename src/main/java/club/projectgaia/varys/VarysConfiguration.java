package club.projectgaia.varys;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class VarysConfiguration {

    /**
     * 连接池最大连接数
     */
    @Value("${httpclient.config.connMaxTotal}")
    private int connMaxTotal = 20;

    /**
     *
     */
    @Value("${httpclient.config.maxPerRoute}")
    private int maxPerRoute = 20;

    /**
     * 连接存活时间，单位为s
     */
    @Value("${httpclient.config.timeToLive}")
    private int timeToLive = 60;


    @Bean
    public PoolingHttpClientConnectionManager poolingClientConnectionManager() {
        PoolingHttpClientConnectionManager poolHttpcConnManager = new PoolingHttpClientConnectionManager(60, TimeUnit.SECONDS);
        // 最大连接数
        poolHttpcConnManager.setMaxTotal(this.connMaxTotal);
        // 路由基数
        poolHttpcConnManager.setDefaultMaxPerRoute(this.maxPerRoute);
        return poolHttpcConnManager;
    }


    @Value("${httpclient.config.keepAliveTime}")
    private int keepAliveTime = 30;

    @Bean("connectionKeepAliveStrategy")
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
        return new ConnectionKeepAliveStrategy() {

            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                // Honor 'keep-alive' header
                HeaderElementIterator it = new BasicHeaderElementIterator(
                        response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch (NumberFormatException ignore) {
                        }
                    }
                }
                return 30 * 1000;
            }
        };
    }

    // 代理的host地址
    @Value("${httpclient.config.proxyhost}")
    private String proxyHost;

    // 代理的端口号
    @Value("${httpclient.config.proxyPort}")
    private int proxyPort;

    @Bean
    public DefaultProxyRoutePlanner defaultProxyRoutePlanner(){
        HttpHost proxy = new HttpHost(this.proxyHost, this.proxyPort);
        return new DefaultProxyRoutePlanner(proxy);
    }


    @Value("${httpclient.config.connectTimeout}")
    private int connectTimeout = 2000;

    @Value("${httpclient.config.connectRequestTimeout}")
    private int connectRequestTimeout = 2000;

    @Value("${httpclient.config.socketTimeout}")
    private int socketTimeout = 2000;
    @Bean
    public RequestConfig config(){
        return RequestConfig.custom()
                .setConnectionRequestTimeout(this.connectRequestTimeout)
                .setConnectTimeout(this.connectTimeout)
                .setSocketTimeout(this.socketTimeout)
                .build();
    }



    
}
