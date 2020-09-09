package com.wby.netty.server.im.config;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @Date 2020/9/7 19:03
 * @Author wuby31052
 */
public class ClassUtils {
    private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private static MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(new PathMatchingResourcePatternResolver());

    public ClassUtils() {
    }

    public static List<String> scanInterface(String[] basePackages, Class<? extends Annotation> annotation) {
        ArrayList classes = new ArrayList();

        try {
            String[] var3 = basePackages;
            int var4 = basePackages.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String basePackage = var3[var5];
                if (!StringUtils.isEmpty(basePackage)) {
                    String packageSearchPath = "classpath*:" + org.springframework.util.ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage)) + "/**/*.class";
                    Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

                    for (int i = 0; i < resources.length; ++i) {
                        Resource resource = resources[i];
                        if (resource.isReadable()) {
                            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                            ClassMetadata classMetadata = metadataReader.getClassMetadata();
                            if (classMetadata.isInterface() && (new AnnotationTypeFilter(annotation)).match(metadataReader, metadataReaderFactory)) {
                                String className = classMetadata.getClassName();
                                if (!classes.contains(className)) {
                                    classes.add(className);
                                }
                            }
                        }
                    }
                }
            }

            return classes;
        } catch (IOException var14) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", var14);
        }
    }

    public static Resource[] scanFile(String name) {
        try {
            return resourcePatternResolver.getResources("classpath*:" + name);
        } catch (IOException var2) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", var2);
        }
    }

    public static boolean isWrapClass(Class<?> clazz) {
        try {
            return ((Class)clazz.getField("TYPE").get((Object)null)).isPrimitive();
        } catch (Exception var2) {
            return false;
        }
    }

    public static Field[] getAllFields(Class clazz) {
        Class c = clazz;

        ArrayList fieldList;
        for (fieldList = new ArrayList(); c != null && !Object.class.equals(c); c = c.getSuperclass()) {
            fieldList.addAll(new ArrayList(Arrays.asList(c.getDeclaredFields())));
        }

        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }
}
