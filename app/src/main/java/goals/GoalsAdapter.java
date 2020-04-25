package goals;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalssetting.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit.GoalsService;
import retrofit.ServiceFactory;
import retrofit2.Call;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder> {
    private List<Goal> goalsList = new ArrayList<>();
    GoalsService goalsService;

    private View addGoalView;
    private String login;

    int count = 0;

    public GoalsAdapter(String login)
    {
        this.login = login;
    }

    class GoalsViewHolder extends RecyclerView.ViewHolder{
        private EditText descriptionTextView;
        private CheckBox checkBox;

        public GoalsViewHolder(View itemView){
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            checkBox = itemView.findViewById(R.id.checkbox);


            addGoalView = itemView;
        }
        @SuppressLint("ResourceType")
        public void bind(final Goal goal)
        {
            descriptionTextView.setText(goal.getDescription());
            checkBox.setChecked(goal.getIsDone());

            final int normalFlags = descriptionTextView.getPaintFlags();

            if (goal.getIsDone())
            {
                descriptionTextView.setPaintFlags(normalFlags| Paint.STRIKE_THRU_TEXT_FLAG);
            }

            descriptionTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus)
                    {
                        goal.setDescription(descriptionTextView.getText().toString());
                        descriptionTextView.setCursorVisible(false);

                        Call<Object> response = ServiceFactory.getGoalsService().replaceGoal(login,
                                goal.getIdSprint(), goal.getId(), goal);
                        response.enqueue(RequestFactory.getPutRequest());
                    }
                    else
                    {
                        descriptionTextView.setCursorVisible(true);
                    }
                }
            });


            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    if (checkBox.isChecked()) {
                        goal.setIsDone(true);
                        descriptionTextView.setPaintFlags(normalFlags| Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        goal.setIsDone(false);
                        descriptionTextView.setPaintFlags(normalFlags);
                        Log.i("GoalsAdapter", "Set isdone");
                    }
                    Log.i("GoalsAdapter", "Before executing a put request");
                    Call<Object> response = ServiceFactory.getGoalsService().replaceGoal(login, goal.getIdSprint(), goal.getId(),
                            goal);
                    response.enqueue(RequestFactory.getPutRequest());
                }
            });
        }
    }


    @Override
    public GoalsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goal_view, parent, false);
        return new GoalsViewHolder(view/*, new DescriptionEditTextListener()*/);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalsViewHolder holder, int position) {
        holder.bind(goalsList.get(position));
        holder.descriptionTextView.setText(goalsList.get(holder.getAdapterPosition()).getDescription());
    }

    @Override
    public int getItemCount() {
        return goalsList.size();
    }

    public void setItems(Collection<Goal> goals) {
        goalsList.addAll(goals);
        notifyDataSetChanged();
    }
    public void clearItems(){
        goalsList.clear();
        notifyDataSetChanged();
    }
    public void addItem(Goal newItem) {
        goalsList.add(newItem);
        notifyDataSetChanged();
    }




}
