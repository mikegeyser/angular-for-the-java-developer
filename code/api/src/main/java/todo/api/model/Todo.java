package todo.api.model;

import java.util.UUID;

public class Todo {
  private UUID id;
  private String title;
  private boolean completed;

  public Todo(){ }

  public Todo(String title, boolean completed){
    this.initialise();
    this.title = title;
    this.completed = completed;
  }

  public UUID getId(){return this.id; }

  public String getTitle(){return this.title;}
  
  public boolean getCompleted(){return this.completed;}
  
  public Todo initialise(){
    this.id = java.util.UUID.randomUUID();
    return this;
  }

  public void updateFrom(Todo source){
    this.title = source.title;
    this.completed = source.completed;
  }

  public void markAsComplete(){this.completed = true;}
}