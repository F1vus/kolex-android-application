package edu.at.kolex.api;

import java.util.List;
import edu.at.kolex.model.Profile;
import edu.at.kolex.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApiService {
    @GET("api/profiles")
    Call<List<Profile>> getUserProfiles();

    @POST("api/profiles")
    Call<Profile> createProfile(@Body Profile profile);

    @PUT("api/profiles/{id}")
    Call<Profile> updateProfile(@Path("id") Long id, @Body Profile profile);

    @DELETE("api/profiles/{id}")
    Call<Void> deleteProfile(@Path("id") Long id);

    @GET("api/users")
    Call<User> getUser();
}
