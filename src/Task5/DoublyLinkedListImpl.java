package Task5;

public class DoublyLinkedListImpl {

  public Node head;
  public Node tail;
  public int size;

  public DoublyLinkedListImpl() {
    size = 0;
  }

  public class Node {
    int pID;
    Monitor.State state;
    Node next;
    Node prev;

    public Node(int id, Monitor.State s, Node next, Node prev) {
      this.pID = id;
      this.state = s;
      this.next = next;
      this.prev = prev;
    }

    public Node next()
    {
      if (this.next == null)
      {
        return head;
      }
      return this.next;
    }

    public Node prev()
    {
      if (this.prev == null)
      {
        return tail;
      }
      return this.prev;
    }
  }

  public int size() { return size; }

  public boolean isEmpty() { return size == 0; }

  public void addFirst(int id, Monitor.State s) {
    Node tmp = new Node(id, s, head, null);
    if(head != null ) {head.prev = tmp;}
    head = tmp;
    if(tail == null) { tail = tmp;}
    size++;
  }

  public void addLast(int id, Monitor.State s) {

    Node tmp = new Node(id, s, null, tail);
    if(tail != null) {tail.next = tmp;}
    tail = tmp;
    if(head == null) { head = tmp;}
    size++;
  }

  public Node find(int pID)
  {
    Node tmp = head;
    while(!tmp.equals(tail))
    {
      if (tmp.pID == pID)
      {
        return tmp;
      }
      tmp = tmp.next;
    }
    if (tmp.pID == pID) {return tmp;}
    return null;
  }

  public void remove(int pID)
  {
    Node target = find(pID);
    if (target != null)
    {
      //case target is tail
      if (target.equals(tail))
      {
        Node next = target.next;
        next.prev = null;
        target.next = null;
        tail = next;
        size--;
      }
      //case target is head
      else if (target.equals(head))
      {
        Node prev = target.prev;
        prev.next = null;
        target.prev = null;
        head = prev;
        size--;
      }
      //case target is between two nodes
      else
      {
        Node prev = target.prev;
        Node next = target.next;
        prev.next = next;
        next.prev = prev;
        size--;
      }
    }
  }
}