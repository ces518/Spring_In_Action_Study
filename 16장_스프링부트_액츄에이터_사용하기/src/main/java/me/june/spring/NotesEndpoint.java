package me.june.spring;

import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// 액츄에이터의 엔드포인트를 지정할때 @Endpoint 를 사용해야 한다.
// HTTP 로만 엔드포인트를 제한하고 싶다면, @WebEndpoint 애노테이션을 사용해야 한다.
@Component
@Endpoint(id = "notes", enableByDefault = true)
public class NotesEndpoint {

    private List<Note> notes = new ArrayList<>();

    @ReadOperation
    public List<Note> notes() {
        return notes;
    }

    @WriteOperation
    public List<Note> addNote(String text) {
        notes.add(new Note(text));
        return notes;
    }

    @DeleteOperation
    public List<Note> deleteNote(int index) {
        if (index < notes.size()) {
            notes.remove(index);
        }
        return notes;
    }
}
