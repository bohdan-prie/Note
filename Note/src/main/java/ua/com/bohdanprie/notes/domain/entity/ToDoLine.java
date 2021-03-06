package ua.com.bohdanprie.notes.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Class represent type of user's elements
 * <br>extends {@link AbstractTextContainer}
 * <br>Fields: {@link List} of {@link ToDo}
 * @author bohda
 *
 */
public class ToDoLine extends AbstractTextContainer{
	private static final Logger LOG = LogManager.getLogger(ToDoLine.class.getName());
	
	private List<ToDo> toDo;

	public ToDoLine(String title) {
		this();
		changeTitle(title);
	}
	
	public ToDoLine() {
		super();
		toDo = new ArrayList<>();		
	}

	public List<ToDo> getToDo() {
		return toDo;
	}

	public void setToDo(List<ToDo> toDo) {
		if (toDo == null) {
			LOG.debug("ToDo list is null");
		} else {
			this.toDo = toDo;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(toDo, super.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if (obj == null || (getClass() != obj.getClass())) return false;
		ToDoLine toDoLine = (ToDoLine) obj;
		return super.equals(toDoLine) && toDo.equals(toDoLine.toDo);
	}
}