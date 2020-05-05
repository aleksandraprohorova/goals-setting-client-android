package activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goalssetting.R;

import entity.User;
import retrofit.RegistrationService;
import retrofit.ServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    EditText loginEditText;
    EditText passwordEditText;
    RegistrationService registrationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        loginEditText = findViewById(R.id.newLoginEditText);
        passwordEditText = findViewById(R.id.newPasswordEditText);
    }

    public void registrateClickListener(View v) {

        final String login = loginEditText.getText().toString();
        final String password = passwordEditText.getText().toString();


        ServiceFactory.setAuthorizeParameters(login, password);
        Call<Object> response = ServiceFactory.getRegistrationService()
                .registrateUser(new User(login, password));


        response.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.i("RegistrationActivity", "response successful");
                    finish();
                } else {
                    //Log.i("RegistrationActivity", String.valueOf(response.code()));
                    Toast.makeText(RegistrationActivity.this, "Couldn't registrate", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("RegistrationActivity", t.getMessage());
                Toast.makeText(RegistrationActivity.this, "Bad request", Toast.LENGTH_SHORT).show();
            }

        });

    }
}
