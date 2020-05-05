package activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalssetting.R;

import java.util.ArrayList;

import adapters.SprintAdapter;
import entity.Sprint;
import itemtouch.SimpleItemTouchHelperCallback;
import retrofit.ServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SprintsActivity extends AppCompatActivity {

    private RecyclerView viewOfSprints;
    private SprintAdapter sprintAdapter;

    private ArrayList<Sprint> sprints;

    private String login;
    private SprintAdapter.OnSprintClickListener onSprintClickListener = new SprintAdapter.OnSprintClickListener() {
        @Override
        public void onSprintClick(Sprint sprint) {
            Toast.makeText(SprintsActivity.this, "Sprint clicked", Toast.LENGTH_SHORT).show();
            launchGoalsActivity(sprint.getId());
        }
    };


    private void launchGoalsActivity(long id)
    {
        Intent intent = new Intent(this, GoalsActivity.class);
        intent.putExtra("idSprint", Long.toString(id));
        intent.putExtra("login", login);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprints);

        viewOfSprints = findViewById(R.id.viewOfSprints);
        viewOfSprints.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        login = intent.getStringExtra("login");

        Log.i("SprintsActivity", login);

        sprintAdapter = new SprintAdapter(onSprintClickListener, login);
        viewOfSprints.setAdapter(sprintAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(sprintAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(viewOfSprints);
    }

    @Override
    protected void onStart() {
        super.onStart();

        sprintAdapter.clearItems();
        getAllSprints();
    }



    private void getAllSprints() {
        Call<Iterable<Sprint>> response = ServiceFactory.getSprintsService().getAllSprints();
        response.enqueue(new Callback<Iterable<Sprint>>() {
            @Override
            public void onResponse(Call<Iterable<Sprint>> call, Response<Iterable<Sprint>> response) {
                if (response.isSuccessful()) {
                    Log.i("SprintsActivity", "response successfull");
                    sprints = (ArrayList<Sprint>) response.body();
                    sprintAdapter.setItems(sprints);
                } else {
                    Toast.makeText(SprintsActivity.this, "Couldn't get sprints", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Iterable<Sprint>> call, Throwable t) {
                Log.i("SprintsActivity", t.getMessage());
                Toast.makeText(SprintsActivity.this, "Bad request", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void addSprint(View v) {
        final Sprint tmp = new Sprint();
        sprintAdapter.addItem(tmp);
        //new AddSprintsRequest().execute(new Sprint());

        Call<Object> response = ServiceFactory.getSprintsService().addSprint(tmp);
        response.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.i("SprintsActivity", "response successfull");
                    //sprintAdapter.addItem(tmp);
                    launchGoalsActivity((Integer)response.body());
                } else {
                    Toast.makeText(SprintsActivity.this, "Couldn't create sprint", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("SprintsActivity", t.getMessage());
                Toast.makeText(SprintsActivity.this, "Bad request", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
