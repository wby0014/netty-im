package com.wby.netty.server.im.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.reader.UnicodeReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @Description
 * @Date 2020/9/7 18:58
 * @Author wuby31052
 */
@Configuration
public class ConfigUtils implements EnvironmentPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ConfigUtils.class);
    private static Environment ENVIRONMENT;
    private static final String PROPERTIES_SUFFIX = ".properties";
    private static Boolean IS_RPC_MOCK_ENABLE;
    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");

    public static final String APP_NAME = "app.name";

    public static String get(String key) {
        if (null == ENVIRONMENT) {
            logger.warn("The environment variable in ConfigUtils is Null, please verify!");
            return null;
        } else {
            return ENVIRONMENT.getProperty(key);
        }
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (!StringUtils.isEmpty(environment.getProperty(APP_NAME))) {
            try {
                Map<String, Object> map = new HashMap();
                String configLocations = environment.getProperty("config.location");
                if (!StringUtils.isEmpty(configLocations)) {
                    this.getProperty(map, COMMA_SPLIT_PATTERN.split(configLocations));
                }
                ENVIRONMENT = environment;
                environment.getPropertySources().addLast(new MapPropertySource("locationConfig", map));
            } catch (Exception var6) {
                logger.error(var6.getMessage());
            }
        }
    }

    private void getProperty(Map<String, Object> map, String[] locations) throws Exception {
        if (locations != null) {
            String[] var3 = locations;
            int var4 = locations.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String location = var3[var5];
                Resource[] resources;
                Resource[] var8;
                int var9;
                int var10;
                Resource resource;
                if (location.startsWith("classpath:")) {
                    resources = ClassUtils.scanFile(location.replace("classpath:", "").trim());
                    var8 = resources;
                    var9 = resources.length;

                    for (var10 = 0; var10 < var9; ++var10) {
                        resource = var8[var10];
                        this.getPropertyFromResource(map, resource);
                    }
                } else if (location.startsWith("classpath*:")) {
                    resources = ClassUtils.scanFile(location.replace("classpath*:", "").trim());
                    var8 = resources;
                    var9 = resources.length;

                    for (var10 = 0; var10 < var9; ++var10) {
                        resource = var8[var10];
                        this.getPropertyFromResource(map, resource);
                    }
                } else {
                    this.getPropertyFromPath(map, location);
                }
            }
        }
    }

    private void getPropertyFromResource(Map<String, Object> map, Resource resource) throws Exception {
        InputStream is = null;
        UnicodeReader ur = null;

        try {
            is = resource.getInputStream();
            ur = new UnicodeReader(is);
            Properties p = new Properties();
            p.load(ur);
            this.getPropertyFromProperties(map, p);
        } catch (Exception var16) {
            throw var16;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception var15) {
                    logger.error(var15.getMessage());
                }
            }
            if (ur != null) {
                try {
                    ur.close();
                } catch (Exception var14) {
                    logger.error(var14.getMessage());
                }
            }
        }
    }

    private void getPropertyFromProperties(Map<String, Object> map, Properties p) throws Exception {
        Enumeration e = p.propertyNames();
        while (e.hasMoreElements()) {
            String strKey = (String) e.nextElement();
            String strValue = p.getProperty(strKey);
            if (!map.containsKey(strKey)) {
                map.put(strKey, strValue);
            }
        }
    }

    private void getPropertyFromPath(Map<String, Object> map, String path) throws Exception {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                File[] var5 = files;
                int var6 = files.length;
                for (int var7 = 0; var7 < var6; ++var7) {
                    File f = var5[var7];
                    if (f.exists() && f.isFile() && f.getName().endsWith(".properties")) {
                        FileInputStream is = null;

                        try {
                            is = new FileInputStream(f);
                            Properties p = new Properties();
                            p.load(new UnicodeReader(is));
                            this.getPropertyFromProperties(map, p);
                        } catch (Exception var33) {
                            throw var33;
                        } finally {
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (Exception var30) {
                                    logger.error(var30.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        } else {
            FileInputStream is = null;
            try {
                is = new FileInputStream(path);
                Properties p = new Properties();
                p.load(new UnicodeReader(is));
                this.getPropertyFromProperties(map, p);
            } catch (Exception var32) {
                throw var32;
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception var31) {
                        logger.error(var31.getMessage());
                    }
                }
            }
        }
    }

}
