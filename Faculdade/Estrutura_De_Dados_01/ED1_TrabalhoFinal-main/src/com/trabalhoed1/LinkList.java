package com.trabalhoed1;

import com.trabalhoed1.models.Pessoa;

class Link {
    public Pessoa data; // data item
    public Link next; // next link in list
    public Link previous; // previous link in list

    public Link(Pessoa p) {
        data = p;
    }

    public void displayLink() {
        System.out.print("{" + data.toString() + "} ");
    }
}

////////////////////////////////////////////////////////////////
// Utilizado o arquivo doublyLinked.java do Lafore como base.
class LinkList {
    private Link first; // ref to first item
    private Link last; // ref to last link
    private Pessoa furafila;

    // -------------------------------------------------------------
    public LinkList() // constructor
    {
        first = null;
        last = null;
        furafila = null;
    }

    // -------------------------------------------------------------
    public boolean isEmpty() // true if no links
    {
        return (first == null);
    }

    public void insertFirst(Pessoa key) // insert at front of list
    {
        Link newLink = new Link(key); // make new link

        if (isEmpty()) // if empty list,
            last = newLink; // newLink <-- last
        else
            first.previous = newLink; // newLink <-- old first
        newLink.next = first; // newLink --> old first
        first = newLink; // first --> newLink
    }

    // -------------------------------------------------------------
    // -------------------------------------------------------------
    public void insertLast(Pessoa key) // insert at end of list
    {
        Link newLink = new Link(key); // make new link
        if (isEmpty()) // if empty list,
            first = newLink; // first --> newLink
        else {
            last.next = newLink; // old last --> newLink
            newLink.previous = last; // old last <-- newLink
        }
        last = newLink; // newLink <-- last
    }

    // -------------------------------------------------------------
    public Link deleteFirst() // delete first link
    { // (assumes non-empty list)
        Link temp = first;
        if (first.next == null) // if only one item
            last = null; // null <-- last
        else
            first.next.previous = null; // null <-- old next
        first = first.next; // first --> old next
        return temp;
    }

    // -------------------------------------------------------------
    public Link deleteLast() // delete last link
    { // (assumes non-empty list)
        Link temp = last;
        if (first.next == null) // if only one item
            first = null; // first --> null
        else
            last.previous.next = null; // old previous --> null
        last = last.previous; // old previous <-- last
        return temp;
    }

    // -------------------------------------------------------------
    public Link deleteKey(Pessoa key) // delete item w/ given key
    { // (assumes non-empty list)
        Link current = first; // start at beginning
        if (current == null) {
            return null;
        }

        while (!current.data.getId().equals(key.getId())) // until match is found,
        {
            current = current.next; // move to next link
            if (current == null)
                return null; // didn't find it
        }
        if (current == first) // found it; first item?
            first = current.next; // first --> old next
        else // not first
        {
            if (current.previous != null) // old previous --> old next
                current.previous.next = current.next;
        }

        if (current == last) // last item?
            last = current.previous; // old previous <-- last
        else // not last
        {
            if (current.next != null) // old previous <-- old next
                current.next.previous = current.previous;
        }
        return current; // return value
    }

    // -------------------------------------------------------------
    public Pessoa peekFirst() {
        // Se o fura fila existir, retorna ele primeiro.
        if (furafila != null) {
            return furafila;
        }
        return first.data;
    }

    // -------------------------------------------------------------
    public Pessoa peekLast() {
        return last.data;
    }

    // -------------------------------------------------------------
    public void furaFila(Pessoa key) {
        furafila = key;
    }

    public boolean isFuraFila(Pessoa key) {
        if (furafila == null) {
            return false;
        }
        return furafila.getId().equals(key.getId());
    }

    public boolean hasFuraFila() {
        return furafila != null;
    }

    // -------------------------------------------------------------
    public void displayForward() {
        // Se o fura fila existir, retorna ele primeiro.
        if (furafila != null) {
            System.out.print("{" + furafila.toString() + "} ");
        }

        System.out.print("List (first-->last): ");
        Link current = first; // start at beginning
        while (current != null) // until end of list,
        {
            current.displayLink(); // display data
            current = current.next; // move to next link
        }
        System.out.println("");
    }

    // -------------------------------------------------------------
    public void displayBackward() {
        System.out.print("List (last-->first): ");
        Link current = last; // start at end
        while (current != null) // until start of list,
        {
            current.displayLink(); // display data
            current = current.previous; // move to previous link
        }
        System.out.println("");
    }
}
