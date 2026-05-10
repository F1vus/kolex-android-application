package edu.at.kolex.api;

import edu.at.kolex.model.BuyRandomTicketRequest;
import edu.at.kolex.model.BuyTicketRequest;
import edu.at.kolex.model.BuyTicketResponse;
import edu.at.kolex.model.TopUpRequest;
import edu.at.kolex.model.TopUpResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PaymentApiService {
    @POST("api/payment/buy-ticket")
    Call<BuyTicketResponse> buyTicket(@Body BuyTicketRequest buyTicketRequest);

    @POST("api/payment/purchase-random")
    Call<BuyTicketResponse> purchaseRandom(@Body BuyRandomTicketRequest request);

    @POST("api/payment/top-up")
    Call<TopUpResponse> topUp(@Body TopUpRequest request);
}

