package com.archesky.auth.server.configuration

import com.archesky.auth.server.CSVConverter
import com.archesky.auth.server.types.CSV
import com.archesky.auth.server.types.TokenTwo
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
@Configuration
class Config : WebMvcConfigurer {
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        super.extendMessageConverters(converters)
        converters.add(CSVConverter<TokenTwo, CSV<TokenTwo>>())
    }
}
