package todo.api;

import java.util.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import todo.api.model.*;
import io.swagger.annotations.ApiOperation;

@RestController
@EnableAutoConfiguration
public class TodoController {
  private List<Todo> todos;

  TodoController() {
    this.todos = new ArrayList<Todo>();
    this.todos.add(new Todo("Setup project", true));
    this.todos.add(new Todo("List todos", false));
    this.todos.add(new Todo("Create a todo", false));
    this.todos.add(new Todo("Mark a todo as completed", false));
    this.todos.add(new Todo("Edit a todo", false));
    this.todos.add(new Todo("Delete a todo", false));
  }

  @ApiOperation(value = "", nickname = "List")
  @RequestMapping(value = "api/todo", method = RequestMethod.GET)
  List<Todo> get() {
    return todos;
  }

  @ApiOperation(value = "", nickname = "Create")
  @RequestMapping(value = "api/todo", method = RequestMethod.POST)
  List<Todo> post(@RequestBody Todo todo) {
    this.todos.add(todo.initialise());
    return this.todos;
  }

  @ApiOperation(value = "", nickname = "Update")
  @RequestMapping(value = "api/todo", method = RequestMethod.PUT)
  List<Todo> put(@RequestBody Todo source) {
    Todo target = getById(source.getId());
    target.updateFrom(source);
    return this.todos;
  }

  @ApiOperation(value = "", nickname = "Remove")
  @RequestMapping(value = "api/todo", method = RequestMethod.DELETE)
  List<Todo> delete(@RequestBody Todo source) {
    Todo target = getById(source.getId());
    this.todos.remove(target);
    return this.todos;
  }

  @ApiOperation(value = "", nickname = "Complete")
  @RequestMapping(value = "api/todo/complete", method = RequestMethod.POST)
  List<Todo> complete(@RequestBody UUID id) {
    Todo todo = getById(id);
    todo.markAsComplete();
    return this.todos;
  }

  private Todo getById(UUID id) {
    return this.todos.stream().filter(x -> x.getId().equals(id)).findFirst().get();
  }
}