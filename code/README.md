# Setup
## Install @angular/cli

```bash
  > npm install -g @angular/cli
  > ng help
```

## Scaffold out new project

```bash
  > ng new todo --directory demo --inline-style --skip-tests --skip-install
  > cd demo/
  > rm -rf e2e/ karma.conf.js protractor.conf.js README.md
  > yarn install
  > ng serve
```

Browse to [http://localhost:4200/](http://localhost:4200/)

## Update to include the styling on `index.html`

```bash
  > npm install --save todomvc-app-css todomvc-common
```

#### .angular-cli.json
```json
    "styles": [
      "styles.css",
      "../node_modules/todomvc-common/base.css",
      "../node_modules/todomvc-app-css/index.css"
    ],
```

# List the todos
## Create the store
#### src/app/todo.store.ts
> demo-store
```TypeScript
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

export class Todo {
  id?: string;
  title?: string;
  completed?: boolean;

  constructor(title: string) {
    this.completed = false;
    this.title = title.trim();
  }
}

@Injectable()
export class TodoStore {
  todos: Todo[] = [];

  constructor() { }
}
```

#### src/app/app.module.ts
```TypeScript
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { TodoStore } from './todo.store';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [TodoStore],
  bootstrap: [AppComponent]
})
export class AppModule { }

```

#### src/app/app.component.ts
```TypeScript
import { Component } from '@angular/core';
import { TodoStore, Todo } from './todo.store';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html'
})
export class AppComponent {

  constructor(public store: TodoStore) { }
}
```

#### src/app/app.component.html
> demo-html-skeleton
```html
<section class="todoapp">
  
  <header class="header">
    <h1>todos</h1>
  </header>

  <section class="main">
    <ul class="todo-list">
      <li *ngFor="let todo of store.todos" 
          [class.completed]="todo.completed" 
          [class.editing]="todo.editing">
        <div class="view">
          <input class="toggle" 
                 type="checkbox" >
          <label>{{todo.title}}</label>
        </div>
      </li>
    </ul>
  </section>
  
</section>

```

## Generate the client api using swagger

```bash
  > java -jar ../util/swagger-codegen-cli.jar generate -i http://localhost:9000/v2/api-docs -l typescript-angular2 -o src/app/services --model-name-suffix ApiModel
```

#### package.json
```json
 "scripts": {
    "ng": "ng",
    "start": "ng serve",
    "build": "ng build",
    "test": "ng test",
    "lint": "ng lint",
    "e2e": "ng e2e",
    "swagger": "java -jar ../util/swagger-codegen-cli.jar generate -i http://localhost:9000/v2/api-docs -l typescript-angular2 -o src/app/services --model-name-suffix ApiModel"
  },
```

```bash
  > npm run swagger
```

#### src/app/todo.store.ts
> demo-store-import-api
```TypeScript
import { TodocontrollerApi, TodoApiModel } from './services';
```

```TypeScript
export class Todo implements TodoApiModel {
```
> demo-store-todos
```TypeScript
 constructor(public api: TodocontrollerApi) {
    this.api.list().subscribe(x => this.set(x));
  }

  private set(todos: TodoApiModel[]) {
    this.todos = todos.map(x => <Todo>x);
  }
```

#### src/app/app.module.ts
> demo-module-import-api
```TypeScript
import { BASE_PATH, TodocontrollerApi } from './services';
```
> demo-module-providers
```TypeScript
  providers: [
    {provide: BASE_PATH, useValue:"."},
    TodocontrollerApi,
    TodoStore
  ],
```

#### proxy.config.json
> demo-proxy-config
```json
{
  "/api/*": {
    "target": "http://localhost:9000/",
    "secure": false
  }
}
```

```bash
  > ng serve --proxy-config proxy.config.json
```

#### package.json
```json
  "start": "ng serve --proxy-config proxy.config.json",
```

# Add a todo

#### src/app/app.component.html
> demo-html-add
```html
<input class="new-todo" 
       placeholder="What needs to be done?" 
       autofocus="" 
       [(ngModel)]="newTodoText" 
       (keyup.enter)="addTodo()">
```

#### src/app/app.component.ts
```TypeScript
public newTodoText : string;
```
> demo-component-add
```TypeScript
  addTodo(){
    if (this.newTodoText.trim().length) {
      this.store.add(this.newTodoText);
      this.newTodoText = '';
    }
  }
```
#### src/app/todo.store.ts
> demo-store-add
```TypeScript
  add(title: string) {
    let todo = new Todo(title);
    this.api.create(todo).subscribe(x => this.set(x));
  }  
```

# Mark a todo as complete
#### src/app/app.component.html
> demo-html-complete
```TypeScript
<input class="toggle" 
       type="checkbox" 
       (click)="toggleCompletion(todo)" 
       [checked]="todo.completed">
```

#### src/app/app.component.ts
> demo-component-complete
```TypeScript
  toggleCompletion(todo: Todo) {
    this.store.complete(todo);
  }
```

#### src/app/todo.store.ts
> demo-store-complete
```TypeScript
  complete(todo: Todo) {
    this.api.complete(todo.id).subscribe(x => this.set(x));
  }
```

# Edit a todo
#### src/app/app.component.html
> demo-html-edit-1
```html
<label (dblclick)="editTodo(todo)">{{todo.title}}</label>
```
> demo-html-edit-2
```html
<input class="edit" 
       *ngIf="todo.editing" 
       [value]="todo.title" 
       #editedtodo 
       (blur)="completeEditing(todo, editedtodo.value)" 
       (keyup.enter)="completeEditing(todo, editedtodo.value)"
       (keyup.escape)="cancelEditingTodo(todo)">
```
#### src/app/app.component.ts
> demo-component-edit1
```TypeScript
  editTodo(todo: Todo) {
    todo.editing = true;
  }
```
> demo-component-edit2
```TypeScript
  cancelEditingTodo(todo: Todo) {
    todo.editing = false;
  }
```
> demo-component-edit3
```TypeScript
  completeEditing(todo: Todo, editedTitle: string) {
    todo.title = editedTitle;
    this.store.update(todo);
  }
```

#### src/app/todo.store.ts
```TypeScript
  editing?: boolean;
```
> demo-store-edit
```TypeScript
  update(todo: Todo){
    this.api.update(todo).subscribe(x => this.set(x));
  }
```

# Delete a todo
#### src/app/app.component.html
> demo-html-delete
```html
  <button class="destroy" (click)="remove(todo)"></button>
```

#### src/app/app.component.ts
> 
```TypeScript
  remove(todo: Todo) {
    this.store.remove(todo);
  }
```
#### src/app/todo.store.ts
> demo-store-delete
```TypeScript
  remove(todo: Todo) {
    this.api.remove(todo).subscribe(x => this.set(x));
  }
```
