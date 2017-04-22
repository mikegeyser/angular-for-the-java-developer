import { Component } from '@angular/core';
import { TodoStore, Todo } from './todo.store';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html'
})
export class AppComponent {
  newTodoText: string;

  constructor(public store: TodoStore) { }

  addTodo() {
    if (this.newTodoText.trim().length) {
      this.store.add(this.newTodoText);
      this.newTodoText = '';
    }
  }

  toggleCompletion(todo: Todo) {
    this.store.complete(todo);
  }

  remove(todo: Todo) {
    this.store.remove(todo);
  }

  editTodo(todo: Todo) {
    todo.editing = true;
  }

  cancelEditingTodo(todo: Todo) {
    todo.editing = false;
  }

  completeEditing(todo: Todo, editedTitle: string) {
    todo.title = editedTitle;
    this.store.update(todo);
  }
}