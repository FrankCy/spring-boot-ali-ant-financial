package com.ali.ant.financial.util;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @description：认证服务
 * @author: Yang.Chang
 * @project: spring-boot-ali-ant-financial
 * @package: com.ali.ant.financial.util
 * @email: cy880708@163.com
 * @date: 2018/9/19 下午7:13
 * @mofified By:
 */
public class CertificationUtil {

    private static final Logger logger = LoggerFactory.getLogger(CertificationUtil.class);

    /**
     * @description：post请求公用方法 ( params为JSONObject.toString()获得的字符串）
     * @version 1.0
     * @author: Yang.Chang
     * @email: cy880708@163.com
     * @date: 2018/9/19 下午7:39
     * @mofified By:
     */
    public static String post(String strURL, String params) {
        System.out.println(strURL);
        System.out.println(params);
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post=new HttpPost(strURL);
            StringEntity entity=new StringEntity(params,"UTF-8");
            post.setEntity(entity);
            CloseableHttpResponse response = client.execute(post);
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity);
            logger.debug(result);
            return result;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("error",e);
        }
        // 自定义错误信息
        return "error";
    }

    /**
     * @description：GET 请求公用方法 ( param为字符串拼接的以&间隔的字符串)
     * @version 1.0
     * @author: Yang.Chang
     * @email: cy880708@163.com
     * @date: 2018/9/19 下午7:38
     * @mofified By:
     */
    public static String get(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {

            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            //打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            //设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
            //建立实际的连接
            connection.connect();
            //获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            logger.error("error",e);
        } finally {
            // 使用finally块来关闭输入流
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("error",e2);
            }
        }
        return result;
    }

    /**
     * @description：事例
     * @version 1.0
     * @author: Yang.Chang
     * @email: cy880708@163.com
     * @date: 2018/9/19 下午7:40
     * @mofified By:
     */
    public static void main(String[] args) {
        //企业三要素匹配查询接口
        String url = "http://i.yjapi.com/ECIMatch/CompanyVerify";
        StringBuffer stringBuffer = new StringBuffer();
        //接口调用者Key
        stringBuffer.append("key=");
        //统一机构代码
        stringBuffer.append("&regNo=110108012660422");
        //公司名称
        stringBuffer.append("&companyName=小米科技");
        //法人
        stringBuffer.append("&frName=雷军");
        String paramsString = stringBuffer.toString();
        get(url, paramsString);
    }

}
