package sprints;

import java.util.Calendar;

public class Sprint {
    private long id;
    private long idUser;
    private Calendar startDate;
    private Calendar endDate;

    public Sprint(){
        this.startDate = Calendar.getInstance();
        this.endDate = Calendar.getInstance();
    }
    public Sprint(long id)
    {
        this(id, id, Calendar.getInstance(), Calendar.getInstance());
    }

    public Sprint(long id, long idUser, Calendar startDate, Calendar endDate)
    {
        this.id = id;
        this.idUser = idUser;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setId(long id)
    {
        this.id = id;
    }
    public long getId() { return id; }

    public long getIdUser() { return idUser; }

    public void setIdUser(long idUser)
    {
        this.idUser = idUser;
    }


    public void setStartDate(Calendar startDate){
        this.startDate = startDate;
    }
    public Calendar getStartDate()
    {
        return startDate;
    }

    public void setEndDate(Calendar endDate){
        this.endDate = endDate;
    }
    public Calendar getEndDate()
    {
        return endDate;
    }

}
