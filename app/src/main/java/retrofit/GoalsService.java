package retrofit;

import entity.Goal;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GoalsService {
    @GET("users/sprints/{idSprint}/goals/{idGoal}")
    Call<Goal> getGoalById(@Path("idSprint") Long idSprint, @Path("idGoal") Long idGoal);

    @GET("users/sprints/{idSprint}/goals")
    Call<Iterable<Goal>> getAllGoals(@Path("idSprint") Long idSprint);

    @POST("users/sprints/{idSprint}/goals")
    Call<Object> addGoal(@Path("idSprint") Long idSprint, @Body Goal goal);

    @DELETE("users/sprints/{idSprint}/goals/{idGoal}")
    Call<Object> deleteGoal(@Path("idSprint") Long idSprint, @Path("idGoal") Long idGoal);

    @PUT("users/sprints/{idSprint}/goals/{idGoal}")
    Call<Object> replaceGoal(@Path("idSprint") Long idSprint,
                             @Path("idGoal") Long idGoal, @Body Goal newGoal);
}
