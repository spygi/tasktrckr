package spy.gi.tasktrckr;

public enum TaskType {
    ENG(0, R.color.engTask), TEAM(1, R.color.teamTask), MGMT(2, R.color.mgmtTask), MISC(3, R.color.miscTask);

    private final int index;
    private final int color;

    TaskType(final int index, final int color) {
        this.index = index;
        this.color = color;
    }

    int getColor() {
        return this.color;
    }

    int getIndex() {
        return this.index;
    }

    public static TaskType getTypeFromName(String name) {
        if (name == null) {
            return null;
        }
        for (TaskType type : values()) {
            if (name.equalsIgnoreCase(type.name())) {
                return type;
            }
        }

        return null;
    }

    public static TaskType getTypeFromIndex(int index) {
        for (TaskType type : values()) {
            if (index == type.getIndex()) {
                return type;
            }
        }

        return null;
    }
}
