package com.example.retrofittest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("/arboles/dummyTest")

    Call<AddCustomerRes> addCustomer(@Part MultipartBody.Part image,
                                     @Part("name") RequestBody name,
                                     @Part("ref") RequestBody ref
                                     );
}
