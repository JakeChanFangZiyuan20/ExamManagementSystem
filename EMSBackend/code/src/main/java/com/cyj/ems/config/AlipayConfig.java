package com.cyj.ems.config;

import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

@Component
public class AlipayConfig {

    private static String ngrok = "http://emsjake.gz2vip.idcfengye.com";

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016102400748048";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCpixtqsMkGF7kB0I05oQAPITgZUEizuVrdkmU/3hGE6V+LfIDySraOmia9gjuaRNVy6iOh7by0zA/zthnacxw4O77HUpW7yVQpDBcy0P4b3Ops5SmB4GcxcHO0rKed8cKpr0vXJi5cME5/qrUG0j+8a2qvWyQCHuKekVb6S0BkEC7dOaS3teD90kUinBNAxnZqSzkmlRBI8GsJ1eS+W5FT39v54zZzHNEKCtmvDdkLwvnoJsB2M3a3UfrOP0uPVy2vwBc4fG+Hqm/A+l8xWAeIFWIsgOxkVZ4EB7TYYfvviQ1SG7AFtd0dfd+WI3e/F4iUnyGOn7H0w5dj6jF/iv/PAgMBAAECggEBAI2tJesA8es6dapTiUAoDvGVs9hLY4V0Fb9kL2Gwg3lKv0HDd8mAr474t/jS1FW0wZ12lPIixPdcMtRYTsBiQqmnG2Ra2a8DrWVjqhsX09CayfbXhNj4GaORsc7AHI160k2To8+VI1Z8M4yxrPaXSRV9ubfxdGSlRIB3OXSM37D9Ctsuz7x12WGusMW162VHJroZ2Cs5svAdybDh7SYC2UaNFjTC6jt/IVxqRRltgSFHlC5GctRSZ/6stSxwNzS3NsjUmgMo2+mR3Ktoan4ts/XA0nS9SfqruScXL1yEHk2YK0oPBYD2p7iTTh4XOSGo5igHrv4wmhmcNlEAjY/1AhkCgYEA54e7FESjaET8WVNhUsfF4hrG0CpGdkc16iljhhn53I/IbTvL/HoCEEdUlauA6qTchUaKcdtXwcowAnc5Liua34028XxF5BLqw99dfuN2JVTsVVm0pFFXJWEUkBwmNx0f6ryRyBKe6stdRUtfwDCuu7xRKCgsEHoyoG0Fc63oSksCgYEAu3ZDqmt7kJjS/iVP7BgnLUOsi1Q1EZAnLXKOJ6bAbK7bsYE1kGmOxUAQyj1JjzrSJIUjyvUgct/+d8MPPbptXnQUtec0iMyF8KM1I8YApo9pucrBGygs+DDj0+Gskz2awqSsX5IQiNug0Ru39HOoLs3yVIaecrDGc21Z4dyZbg0CgYEAq3catjTq63KDjEeywNKQGxYHfOyCCGBObl8+hHbCU3TsUvFA7XcSBio9WyaLL7ruUYlfSFjP0gXBrmHLKAVFCiATu510SSc1U+Ac5rbfrWAKMl9f530R0+3kg6FFL/XSyE3kjM41gN8g8QuUGsoy7XfWhq3RjFZjHen/ap81c6cCgYAEzjNN2xN/0WsXOL2jv7rBXkq+B5qUqK1YbOdX/N5R6umW00kcOph+VfYYORn5ofhhF0ijm0UJrNRJHab2XiEPmsotH5ApMZT9ZjGmiRzbKDH9oUTx/UWp1xcAB3j1DU86Th5dFNF3sKmesqc0jQJysLTVld3Z8s4jh1oWgCEuwQKBgFjOdad6kDWyz9yHEYsZrs26XV2M2AdXhfoBxU9onbtljIAP8LNDqSqmPMelKmgkvtaS5Z5QvXAUIOX7z2vnEEO6F0ng48zsTgP4FboIkClWBYildy8LTO7sEtfj1b1irpe0Y1i/50qXEMtd8d039uXua48dDY2gxRhEOzw82pKO";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh/5LuPXpIEkYrz5BJvhaeRHNvWa1jt0qq98sCkLRiCC2ztHolNEG5QMDdwD7roF2q7tKFoTGX5fEzh3Gb1t3IBeOb8FydMKYoN++yC2MH+zJRvdJ425CiDBa9k4BOdh2Mk1GwTp4qpj1rn0Mu291PqJqsRQZRnv3q+rfGD3c0s8Vo72XT9W2g5XyujrrVImq24uVoFyk6f08SZCNX4TiKZV+fJuexghpVeERH90T/MCMSXHKG2pH/N+4DaLn3BpRXCk5jMiWrA9CB3rXWhJHuwfiv2gKfrcC60SguEdMxocKaAwqKxLC5WpMXUWYnf+vyjZ8DuQvf57D3H8J8/ReaQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = ngrok +  "/api/notifyUrl";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//    public static String return_url = "http://localhost:8082/api/returnUrl";
    public static String return_url = "";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "E:\\_Java\\IdeaProject\\EMS\\Alipay\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
