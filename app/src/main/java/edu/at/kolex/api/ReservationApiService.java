package edu.at.kolex.api;

import edu.at.kolex.model.ReservationRequest;
import edu.at.kolex.model.ReservationResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservationApiService {
    @DELETE("api/reservations/{reservationId}/cancel")
    Call<Void> cancel(@Path("reservationId") Long reservationId,
                      @Query("profileId") Long profileId);


    @POST("api/reservations/hold")
    Call<ReservationResponse> reserveSeat(@Body ReservationRequest reservationRequest);
}
