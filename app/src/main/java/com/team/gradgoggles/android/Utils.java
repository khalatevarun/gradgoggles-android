package com.team.gradgoggles.android;

import com.team.gradgoggles.android.api.model.ApiError;
import com.team.gradgoggles.android.api.service.RetrofitBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class Utils {

    public static ApiError convertErrors(ResponseBody response){
        Converter<ResponseBody,ApiError> converter= RetrofitBuilder.getRetrofit().responseBodyConverter(ApiError.class, new Annotation[0]);

        ApiError apiError=null;

        try {
            apiError=converter.convert(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiError;
    }

}
