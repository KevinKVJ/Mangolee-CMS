package org.mangolee.utils;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

// Jasypt工具类 根据配置文件中的Jasypt密钥进行加解密
public class JasyptUtils {

    public static String encrypt(String jasyptPassword, String rawPassword) {
        if (jasyptPassword == null || rawPassword == null) {
            throw new IllegalArgumentException("参数不能为null");
        }
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig    config    = new SimpleStringPBEConfig();
        config.setPassword(jasyptPassword);
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor.encrypt(rawPassword);
    }

    public static String decrypt(String jasyptPassword, String encryptedPassword) {
        if (jasyptPassword == null || encryptedPassword == null) {
            throw new IllegalArgumentException("参数不能为null");
        }
        PooledPBEStringEncryptor decryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig    config    = new SimpleStringPBEConfig();
        config.setPassword(jasyptPassword);
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        decryptor.setConfig(config);
        return decryptor.decrypt(encryptedPassword);
    }
}
