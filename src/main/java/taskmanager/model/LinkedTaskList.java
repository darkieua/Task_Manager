package main.taskmanager.model;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by DarkST on 19.10.2016.
 */
public class LinkedTaskList extends TaskList {

    private class Node {
        private Node next;
        private Task data;

        Node () {}

        Node (Node next, Task data) {
            this.next = next;
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private int size = 0;

    @Override
    public Iterator<Task> iterator() {
        return new ListIterator();
    }

    private class ListIterator  implements Iterator<Task>
    {
        private Node current = head;
        int currentPos = 0;

        public boolean hasNext()
        {
            return current != null;
        }

        public Task next()
        {
            if (!hasNext()) throw new NoSuchElementException();
            Task res = current.data;
            current = current.next;
            currentPos++;
            return res;
        }

        public void remove() {
            if (current == head){
                throw new IllegalStateException("remove() method should be used after next()");
            }
            LinkedTaskList.this.remove(getTask(currentPos - 1));
            currentPos--;
        }
    }


    @Override
    public int hashCode() {
        int hcode = 0;

        for (Task task : this) {
            hcode += task.hashCode();
        }
        return hcode;
    }

    @Override
    public Task getTask(int index){
        if (index < 0) throw new IllegalArgumentException("Index of element is less then zero");
        if (index > size) throw new ArrayIndexOutOfBoundsException("Index of element is more than size");
        int i = 0;
        Node element = head;
        while (element != null && i < index)
        {
            element = element.next;
            i++;
        }
        return element.data;
    }

    @Override
    public void add (Task task) {
        if (task == null) throw new IllegalArgumentException("Attempt to add a null task");
        Node element = new Node();
        element.data = task;
        if (tail == null) {
            head = element;
            tail = element;
        }
        else {
            tail.next = element;
            tail = element;
        }
        size++;
    }

    @Override
    public boolean remove (Task task) {
        if (task == null) throw new IllegalArgumentException("Attempt to remove a null task");
        if (head == null) return false;
        if (head == tail) {
            head = null;
            tail = null;
            size--;
            return true;
        }
        if (head.data == task) {
            head = head.next;
            size--;
            return true;
        }

        Node element = head;
        while (element.next != null) {
            if (element.next.data == task) {
                if (tail == element.next) {
                    tail = element;
                }
                element.next = element.next.next;
                size--;
                return true;
            }
            element = element.next;
        }
        return false;
    }

    @Override
    public int size () {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LinkedTaskList tasks = (LinkedTaskList) o;
        if (size() != tasks.size()) return false;
        for (int i = 0; i < size(); i++) {
            if (!getTask(i).equals(tasks.getTask(i))) return false;
        }
        return true;
    }

    @Override
    public LinkedTaskList clone () throws CloneNotSupportedException {
        LinkedTaskList list = new LinkedTaskList();
        for (Task temp : this) {
            list.add(temp);
        }
        return list;
    }
}
