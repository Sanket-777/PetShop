package com.example.petshop;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @POST("getpet.php")
    Call<GetPetPojo> getPet(@QueryMap Map<String, String> params);

    @POST("getmypet.php")
    Call<GetPetPojo> getMyPet(@QueryMap Map<String, String> params);

    @POST("getdoctor.php")
    Call<GetDoctorPojo> getDoctor(@QueryMap Map<String, String> params);

    @Multipart
    @POST("addpet.php")
    Call<AddPetPojo> addPet(@PartMap() Map<String, RequestBody> partMap,
                            @Part MultipartBody.Part file);

    @Multipart
    @POST("updateprofile.php")
    Call<ProfilePojo> profile(@PartMap() Map<String, RequestBody> partMap,
                              @Part MultipartBody.Part file);
}

