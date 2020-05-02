package goals;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestFactory {
    /*AsyncTask<Goal, Void, Void> getPutRequest()
    {
        return new UpdateGoalRequest();
    }*/
    public static Callback<Object> getPutRequest() {
        return new retrofit2.Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.i("GoalsAdapter", "goal is updated successfully");
                } else {
                    Log.i("GoalsAdapter", "Couldn't update goal");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("SprintsActivity", t.getMessage());
            }
        };
    }

}
