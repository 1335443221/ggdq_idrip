package com.sl.common.swagger.response;

public @interface ApiResponseProperty {

    String name();

    String description() default "";

    String type();

}

