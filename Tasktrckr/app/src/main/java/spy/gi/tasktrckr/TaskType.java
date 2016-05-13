package spy.gi.tasktrckr;

public enum TaskType {
    ENG(R.color.engTask), MGMT(R.color.mgmtTask), TEAM(R.color.teamTask), MISC(R.color.miscTask);

    private final int color;

    TaskType(final int color) {
        this.color = color;
    }

    int getColor() {
        return this.color;
    }
}
