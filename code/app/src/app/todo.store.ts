import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { TodocontrollerApi, TodoApiModel } from './services';

export class Todo implements TodoApiModel {
	id?: string;
	title?: string;
	completed?: boolean;
	editing?: boolean;

	constructor(title: string) {
		this.completed = false;
		this.editing = false;
		this.title = title.trim();
	}
}

@Injectable()
export class TodoStore {
	todos: Todo[] = [];

	constructor(public api: TodocontrollerApi) {
		this.api.list().subscribe(x => this.set(x));
	}

	private set(todos: TodoApiModel[]) {
		this.todos = todos.map(x => <Todo>x);
	}

	add(title: string) {
		let todo = new Todo(title);
		this.api.create(todo).subscribe(x => this.set(x));
	}

	remove(todo: Todo) {
		this.api.remove(todo).subscribe(x => this.set(x));
	}

	complete(todo: Todo) {
		this.api.complete(todo.id).subscribe(x => this.set(x));
	}

	update(todo: Todo){
		this.api.update(todo).subscribe(x => this.set(x));
	}

	getRemaining() { return this.todos.filter(x => !x.completed); }
}