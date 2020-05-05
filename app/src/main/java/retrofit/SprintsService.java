package retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import entity.Sprint;


public interface SprintsService {
    @GET("users/sprints/{idSprint}")
    Call<Sprint> getSprintById(@Path("idSprint") Long idSprint);

    @GET("users/sprints")
    Call<Iterable<Sprint>> getAllSprints();

    @POST("users/sprints")
    Call<Object> addSprint(@Body Sprint sprint);

    @DELETE("users/sprints/{idSprint}")
    Call<Object> deleteSprint(@Path("idSprint") Long idSprint);

    @PUT("users/sprints/{idSprint}")
    Call<Object> replaceSprint(@Path("idSprint") Long idSprint, @Body Sprint newSprint);
}
