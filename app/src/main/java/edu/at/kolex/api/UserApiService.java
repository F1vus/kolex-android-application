package edu.at.kolex.api;

import java.util.List;
import edu.at.kolex.model.ProfileDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApiService {
    @GET("api/profiles")
    Call<List<ProfileDTO>> getUserProfiles();

    @PUT("api/profiles/{id}")
    Call<ProfileDTO> updateProfile(@Path("id") Long id, @Body ProfileDTO profileDTO);
}
