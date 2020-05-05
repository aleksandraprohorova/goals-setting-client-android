package activities;

import android.content.Intent;
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

public class AuthorizationActivity  extends AppCompatActivity {
    EditText loginEditText;
    EditText passwordEditText;
    RegistrationService registrationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
    }
    private void launchSprintsActivity(String login, String password)
    {
        Intent intent = new Intent(this, SprintsActivity.class);
        intent.putExtra("password", password);
        intent.putExtra("login", login);
        startActivity(intent);
    }
    public void launchRegistrationActivity(View v)
    {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void confirmClickListener(View v) {

       final String login = loginEditText.getText().toString();
       final String password = passwordEditText.getText().toString();

        ServiceFactory.setAuthorizeParameters(login, password);
        Call<User> response = ServiceFactory.getRegistrationService()
                .authorizeUser();

        response.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.i("AuthorizationActivity", "response successful");
                    launchSprintsActivity(login, password);
                } else {
                    Log.i("AuthorizationActivity", String.valueOf(response.code()));
                    Toast.makeText(AuthorizationActivity.this, "Couldn't authorize", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("AuthorizeActivity", t.getMessage());
                Toast.makeText(AuthorizationActivity.this, "Bad request", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
