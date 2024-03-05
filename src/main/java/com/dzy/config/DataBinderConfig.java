package com.dzy.config;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 空字符串转化为空日期
 * 防止报错"Failed to convert property value of type 'java.lang.String' to required type 'java.util.Date'
 *
 * @Author Dzy
 * @Date 2024/2/27  21:15
 */
@Configuration
public class DataBinderConfig {


}
