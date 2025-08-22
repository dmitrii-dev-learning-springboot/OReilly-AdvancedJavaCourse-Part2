package org.example.innter_classes;

public class LinkedList {
    private Node head;

    private class Node{
        int data;
        Node next;
        Node(int data){
            this.data = data;
        }
    }
    public void add(int value){
        Node newNode = new Node(value);
        newNode.next = head;
        head = newNode;
    }


}
