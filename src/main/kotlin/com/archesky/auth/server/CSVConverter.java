package com.archesky.auth.server;

import com.archesky.auth.server.types.CSV;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;

@Component
public class CSVConverter<T, L extends CSV<T>> extends AbstractHttpMessageConverter<L> {
    public CSVConverter () {
        super(new MediaType("text", "csv"));
    }

    @Override
    protected boolean supports (Class<?> clazz) {
        return clazz.isAssignableFrom(CSV.class);
    }

    @NotNull
    @Override
    protected L readInternal (Class<? extends L> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        System.out.println(inputMessage.getBody().read());
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void writeInternal (L l, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        final OutputStreamWriter outputStream = new OutputStreamWriter(outputMessage.getBody());
        outputStream.write("Hello World");
        outputStream.close();
    }
}
