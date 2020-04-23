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

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder> {
    private List<Goal> goalsList = new ArrayList<>();
    RequestFactory requestFactory;

    private View addGoalView;

    int count = 0;

    public GoalsAdapter(RequestFactory requestFactory)
    {
        this.requestFactory = requestFactory;
    }

    class GoalsViewHolder extends RecyclerView.ViewHolder{
        private EditText descriptionTextView;
        private CheckBox checkBox;
        //private DescriptionEditTextListener descriptionEditTextListener;

        public GoalsViewHolder(View itemView/*, DescriptionEditTextListener descriptionEditTextListener*/){
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            checkBox = itemView.findViewById(R.id.checkbox);

            //LayoutInflater inflater = (LayoutInflater) itemView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //addGoalView = inflater.inflate(R.layout.add_goal, null);

            addGoalView = itemView;
            //Button updateGoalButton = itemView.findViewById(R.id.addSprintButton);

            //itemView.addView(addGoalButton);
            //this.descriptionEditTextListener = descriptionEditTextListener;
            //this.descriptionTextView.addTextChangedListener(descriptionEditTextListener);
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
                        requestFactory.getPutRequest().execute(goal);
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
                        //checkBox.setChecked(false);
                        //descriptionTextView.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
                        //descriptionTextView.setCheckMarkDrawable(android.R.attr.listChoiceIndicatorMultiple);
                        goal.setIsDone(true);
                        descriptionTextView.setPaintFlags(normalFlags| Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        //checkBox.setChecked(true);
                        goal.setIsDone(false);
                        descriptionTextView.setPaintFlags(normalFlags);
                        Log.i("GoalsAdapter", "Set isdone");
                        //descriptionTextView.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
                        //descriptionTextView.setCheckMarkDrawable(android.R.attr.listChoiceIndicatorMultiple);
                    }
                    Log.i("GoalsAdapter", "Before executing a put request");
                    requestFactory.getPutRequest().execute(goal);
                }
            });
        }
    }

    /*private class DescriptionEditTextListener implements TextWatcher{

        private int position;

        public void updatePosition(int position){
            this.position = position;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            goalsList.get(position).setDescription(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }*/



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
        //holder.descriptionEditTextListener.updatePosition(holder.getAdapterPosition());
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
