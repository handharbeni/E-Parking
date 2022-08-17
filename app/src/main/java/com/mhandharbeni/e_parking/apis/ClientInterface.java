package com.mhandharbeni.e_parking.apis;

import com.mhandharbeni.e_parking.apis.responses.DataResponse;
import com.mhandharbeni.e_parking.apis.responses.data.DataLogin;
import com.mhandharbeni.e_parking.apis.responses.data.DataMe;
import com.mhandharbeni.e_parking.apis.responses.data.DataPrice;
import com.mhandharbeni.e_parking.apis.responses.data.DataQr;
import com.mhandharbeni.e_parking.apis.responses.data.DataStatus;
import com.mhandharbeni.e_parking.utils.Constant;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ClientInterface {
    @FormUrlEncoded
    @POST("loginJWT.php")
    Call<DataResponse<DataLogin>> login(
            @Field(Constant.CI_USERNAME) String username,
            @Field(Constant.CI_PASSWORD) String password
    );

    @GET("getMe.php")
    Call<DataResponse<DataMe>> getMe();

    @GET("getPrice.php")
    Call<DataResponse<DataPrice>> getPrice();

    @GET("getQr.php")
    Call<DataResponse<DataQr>> getQr(
            @Query("amount") String amount,
            @Query("billNumber") String billNumber
    );

    @GET("checkStatusPayment.php")
    Call<DataResponse<DataStatus>> getStatusPayment(
            @Query("billNumber") String billNumber
    );
}
