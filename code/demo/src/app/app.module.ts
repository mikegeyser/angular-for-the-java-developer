import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { TodoStore } from './todo.store';
import { BASE_PATH, TodocontrollerApi } from './services';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [
    { provide: BASE_PATH, useValue: "." },
    TodocontrollerApi,
    TodoStore
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
