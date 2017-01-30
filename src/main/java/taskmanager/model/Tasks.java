package taskmanager.model;

import java.util.*;

/**
 * Created by DarkST on 12.12.2016.
 */
public class Tasks {

    public static Date getEarliestTime(TaskList list) {
        long minimum = Long.MAX_VALUE;
        for (Task t : list) {
            if (t.isRepeated()) {
                if (t.getStartTime().getTime() < minimum) {
                    minimum = t.getStartTime().getTime();
                }
            } else {
                if (t.getTime().getTime() < minimum) {
                    minimum = t.getTime().getTime();
                }
            }
        }
        return new Date(minimum);
    }

    public static Date getLatestTime(TaskList list) {
        long maximum = Long.MIN_VALUE;
        for (Task t : list) {
            if (t.isRepeated()) {
                if (t.getStartTime().getTime() > maximum) {
                    maximum = t.getStartTime().getTime();
                }
            } else {
                if (t.getTime().getTime() > maximum) {
                    maximum = t.getTime().getTime();
                }
            }
        }
        return new Date(maximum);
    }

    public static Task getEarliest(TaskList list) {
        long minimum = Long.MAX_VALUE;
        Task earliest = null;
        for (Task t : list) {
            if (t.isRepeated()) {
                if (t.getStartTime().getTime() < minimum) {
                    minimum = t.getStartTime().getTime();
                    earliest = t;
                }
            } else {
                if (t.getTime().getTime() < minimum) {
                    minimum = t.getTime().getTime();
                    earliest = t;
                }
            }
        }
        return earliest;
    }

    public static Task getLatest(TaskList list) {
        long maximum = Long.MIN_VALUE;
        Task latest = null;
        for (Task t : list) {
            if (t.isRepeated()) {
                if (t.getStartTime().getTime() > maximum) {
                    maximum = t.getStartTime().getTime();
                    latest = t;
                }
            } else {
                if (t.getTime().getTime() > maximum) {
                    maximum = t.getTime().getTime();
                    latest = t;
                }
            }
        }
        return latest;
    }

    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end) {
        SortedMap<Date, Set<Task>> map = new TreeMap<>();
        Iterable<Task> incomingTasks = null;
        try {
            incomingTasks = incoming(tasks, start, end);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        for (Task task : incomingTasks) {
            if (task.isRepeated()) {
                for (Date date = task.nextTimeAfter(start); !date.after(end); date = new Date (date.getTime() + task.getRepeatInterval())) {
                    if (map.get(date) == null) {
                        map.put(date, new HashSet<>());
                        map.get(date).add(task);
                    } else {
                        map.get(date).add(task);
                    }
                }
            } else {
                if (map.get(task.getTime()) == null) {
                    map.put(task.getTime(), new HashSet<>());
                    map.get(task.getTime()).add(task);
                } else {
                    map.get(task.getTime()).add(task);
                }
            }
        }
        return map;
    }

    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end) throws IllegalAccessException, InstantiationException {
        TaskList newList = null;
        if (tasks instanceof LinkedTaskList)
            newList = new LinkedTaskList();
        else
            newList = new ArrayTaskList();

        for (Task task : tasks) {
            if (task != null) {
                if (task.isActive()) {
                    if (task.isRepeated()) {
                        if (task.nextTimeAfter(start) != null) {
                            if (task.nextTimeAfter(start).after(start) && !task.nextTimeAfter(start).after(end))
                                newList.add(task);
                        }
                    } else {
                        if (task.getTime().after(start) && !task.getTime().after(end))
                            newList.add(task);
                    }
                }
            }
        }
        return newList;
    }
}