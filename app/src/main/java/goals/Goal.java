package goals;

public class Goal {
    private String description;
    private long id;
    private long idSprint;
    private Boolean isDone;

    public Goal(){
        isDone = false;
    }
    public Goal(long idSprint, String description) {
        this.idSprint = idSprint;
        this.description = description;
        this.isDone = false;
    }
    public Goal(long id, long idSprint, String description, Boolean isDone)
    {
        this.id = id;
        this.idSprint = idSprint;
        this.description = description;
        this.isDone = isDone;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public long getIdSprint() {
        return idSprint;
    }
    public void setIdSprint(long idSprint) {
        this.idSprint = idSprint;
    }

    public Boolean getIsDone() {
        return isDone;
    }
    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }
}
