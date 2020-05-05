package retrofit;

import entity.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RegistrationService {
    @POST("registration/")
    Call<Object> registrateUser(@Body User user);
    @GET("users/authorize")
    Call<User> authorizeUser();
}
