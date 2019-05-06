# Nginx 与 Https 底层深入透析

## 什么是信息安全

    信息安全主要包括以下三方面的内容：
        保密性
        真实性
        完整性
        
    互联网使用最多的加密方式是https证书
    
    保证信息安全的唯一途径：数据信息加密
    
## 对称加密

    密码或密钥其实就是字符串或字节流
    
    使用 AES-256算法进行加密
    DES算法、3DES算法、TDEA算法、Blowfish算法、RC5算法、IDEA算法
    
## 非对称加密

    非对称加密算法：RSA
    计算机使用RSA算法生成一对公私钥
    
## 非对称密钥与证书的关系

    非对称密钥：公钥和对应私钥组成，公钥加密，私钥解密
    keytool -genkey -v -alias tomcat -keyalg RSA -validity 36500 -keystore e:\tomcat.keystore -dname "CN=localhost,OU=cn,O=cn,L=cn,ST=cn,c=cn" -storepass password -keypass password