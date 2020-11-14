package org.whu.mya.extension;

import java.lang.annotation.*;

@Documented // 生成javadoc时类会加注解信息
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {
}
