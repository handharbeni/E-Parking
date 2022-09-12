package com.mhandharbeni.e_parking.apis;

import com.mhandharbeni.e_parking.apis.responses.DataResponse;
import com.mhandharbeni.e_parking.apis.responses.data.DataKendaraan;
import com.mhandharbeni.e_parking.apis.responses.data.DataLogin;
import com.mhandharbeni.e_parking.apis.responses.data.DataMe;
import com.mhandharbeni.e_parking.apis.responses.data.DataParkirKeluar;
import com.mhandharbeni.e_parking.apis.responses.data.DataParkirMasuk;
import com.mhandharbeni.e_parking.apis.responses.data.DataPrice;
import com.mhandharbeni.e_parking.apis.responses.data.DataQr;
import com.mhandharbeni.e_parking.apis.responses.data.DataStats;
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

    @FormUrlEncoded
    @POST("parkirMasuk.php")
    Call<DataResponse<DataParkirMasuk>> parkirMasuk(
            @Field(Constant.CI_PLATNUMBER) String platNumber,
            @Field(Constant.CI_TICKETNUMBER) String ticketNumber,
            @Field(Constant.CI_BILLNUMBER) String billNumber,
            @Field(Constant.CI_PRICE) String price,
            @Field(Constant.CI_TYPE) String type,
            @Field(Constant.CI_DATE) String date,
            @Field(Constant.CI_CHECKIN) String checkin,
            @Field(Constant.CI_CHECKOUT) String checkout,
            @Field(Constant.CI_IMAGE) String image,
            @Field(Constant.CI_TOTAL) String total,
            @Field(Constant.CI_PAIDOPTIONS) String paidOptions,
            @Field(Constant.CI_DATAQR) String dataQr,
            @Field(Constant.CI_PAID) String paid,
            @Field(Constant.CI_ISSYNC) String isSync
    );

    @FormUrlEncoded
    @POST("parkirKeluar.php")
    Call<DataResponse<DataParkirKeluar>> parkirKeluar(
            @Field(Constant.CI_BILLNUMBER) String billNumber
    );

    @GET("getKendaraan.php")
    Call<DataResponse<DataKendaraan>> getKendaraan(
            @Query("billNumber") String billNumber
    );

    @GET("getKendaraan.php")
    Call<DataResponse<DataKendaraan>> getKendaraanByPlatNumber(
            @Query("platNumber") String platNumber
    );

    @GET("getStats.php")
    Call<DataResponse<DataStats>> getStats();

}
