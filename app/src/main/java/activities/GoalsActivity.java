package activities;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalssetting.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import entity.Goal;
import adapters.GoalsAdapter;
import itemtouch.SimpleItemTouchHelperCallback;
import retrofit.ServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import entity.Sprint;

public class GoalsActivity extends AppCompatActivity {

    LinearLayout mainLayout;

    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private Button viewStartDate;
    private Button viewEndDate;
    private EditText sprintTime;

    private RecyclerView viewOfGoals;
    private GoalsAdapter goalsAdapter;

    private Collection<Goal> goals;

    Sprint sprint;

    private Button addGoalButton;
    private View addGoalView;

    private String login;
    private long idSprint;


    @Override
    protected void onStart() {
        super.onStart();

        goalsAdapter.clearItems();
        getCurrentSprint();
        getAllGoals();

        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.removeView(addGoalButton);
                mainLayout.addView(addGoalView);

                Button saveGoalButton = findViewById(R.id.saveGoalButton);
                Button cancelButton = findViewById(R.id.cancelButton);
                saveGoalButton.setEms(10);
                cancelButton.setEms(10);

                final EditText newGoalEditText = findViewById(R.id.newGoalEditText);
                newGoalEditText.setText("");
                saveGoalButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Goal tmp = new Goal(idSprint, newGoalEditText.getText().toString());
                        addGoal(tmp);
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainLayout.removeView(addGoalView);
                        mainLayout.addView(addGoalButton);
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        mainLayout = findViewById(R.id.mainLayout);

        viewStartDate = findViewById(R.id.startTime);
        viewEndDate = findViewById(R.id.endTime);

        sprintTime = findViewById(R.id.sprintTime);
        sprintTime.setEnabled(false);

        viewOfGoals = findViewById(R.id.viewOfGoals);
        viewOfGoals.setLayoutManager(new LinearLayoutManager(this));


        Intent intent = getIntent();
        idSprint = Long.valueOf(intent.getStringExtra("idSprint"));
        login = intent.getStringExtra("login");

        goalsAdapter = new GoalsAdapter(login);
        viewOfGoals.setAdapter(goalsAdapter);


        addGoalButton = findViewById(R.id.addGoalButton);
        addGoalButton.setText("Add goal");

        addGoalButton.setEms(10);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addGoalView = inflater.inflate(R.layout.add_goal, null);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(goalsAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(viewOfGoals);

    }

    public void setStartDate(View v) {
        new DatePickerDialog(GoalsActivity.this, startDateSetListener,
                sprint.getStartDate().get(Calendar.YEAR),
                sprint.getStartDate().get(Calendar.MONTH),
                sprint.getStartDate().get(Calendar.DAY_OF_MONTH))
                .show();
    }
    public void setEndDate(View v) {
        new DatePickerDialog(GoalsActivity.this, endDateSetListener,
                endDate.get(Calendar.YEAR),
                endDate.get(Calendar.MONTH),
                endDate.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener startDateSetListener=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar tmp = Calendar.getInstance();
            tmp.set(Calendar.YEAR, year);
            tmp.set(Calendar.MONTH, monthOfYear);
            tmp.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            sprint.setStartDate(tmp);

            updateSprint();
        }
    };
    DatePickerDialog.OnDateSetListener endDateSetListener=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Calendar tmp = Calendar.getInstance();
            Log.i("GoalsActivity", "Before setting date is " + tmp.toString());
            tmp.set(year, monthOfYear, dayOfMonth);

            sprint.setEndDate(tmp);
            Log.i("GoalsActivity", "Before request date is " + tmp.toString());
            updateSprint();
        }
    };
    private void setInitialDateTime() {

        viewStartDate.setText(DateUtils.formatDateTime(this,
                sprint.getStartDate().getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

        viewEndDate.setText(DateUtils.formatDateTime(this,
                sprint.getEndDate().getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

        long diff = sprint.getEndDate().getTimeInMillis() - sprint.getStartDate().getTimeInMillis();


        sprintTime.setText(String.format("%s days", String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))));
    }

    private void getCurrentSprint() {
        Call<Sprint> response = ServiceFactory.getSprintsService().getSprintById(idSprint);
        response.enqueue(new Callback<Sprint>() {
            @Override
            public void onResponse(Call<Sprint> call, Response<Sprint> response) {
                if (response.isSuccessful()) {
                    Log.i("GoalsActivity", "response successfull");
                    sprint = (Sprint) response.body();
                    Log.i("GoalsActivity", "At the beginning date is " + sprint.getEndDate().toString());
                    setInitialDateTime();
                } else {
                    Toast.makeText(GoalsActivity.this, "Couldn't get sprint", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Sprint> call, Throwable t) {
                Log.i("GoalsActivity", t.getMessage());
                Toast.makeText(GoalsActivity.this, "Bad request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getAllGoals() {
        Call<Iterable<Goal>> response = ServiceFactory.getGoalsService().getAllGoals(idSprint);
        response.enqueue(new Callback<Iterable<Goal>>() {
            @Override
            public void onResponse(Call<Iterable<Goal>> call, Response<Iterable<Goal>> response) {
                if (response.isSuccessful()) {
                    Log.i("GoalsActivity", "response successfull");
                    goals = (ArrayList<Goal>) response.body();
                    goalsAdapter.setItems(goals);
                } else {
                    Toast.makeText(GoalsActivity.this, "Couldn't get goals", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Iterable<Goal>> call, Throwable t) {
                Log.i("SprintsActivity", t.getMessage());
                Toast.makeText(GoalsActivity.this, "Bad request", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addGoal(final Goal goal) {
        Call<Object> response = ServiceFactory.getGoalsService().addGoal(idSprint, goal);
        response.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.i("SprintsActivity", "response successfull " + sprint.getEndDate());
                    goalsAdapter.addItem(goal);
                    mainLayout.removeView(addGoalView);
                    mainLayout.addView(addGoalButton);
                } else {
                    Toast.makeText(GoalsActivity.this, "Couldn't create goal", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("SprintsActivity", t.getMessage());
                Toast.makeText(GoalsActivity.this, "Bad request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSprint() {
        Call<Object> response = ServiceFactory.getSprintsService().replaceSprint(idSprint, sprint);
        Calendar tmp = sprint.getEndDate();
        Log.i("GoalsActivity", "Date is " + tmp.toString());
        response.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.i("GoalsActivity", "Sprint is updated successfully");
                    Log.i("GoalsActivity", "" + sprint.getEndDate().toString());
                    setInitialDateTime();
                } else {
                    Toast.makeText(GoalsActivity.this, "Couldn't update sprint", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("GoalsActivity", t.getMessage());
                Toast.makeText(GoalsActivity.this, "Bad request", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
