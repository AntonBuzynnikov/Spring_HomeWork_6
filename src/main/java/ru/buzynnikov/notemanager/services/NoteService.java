package ru.buzynnikov.notemanager.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.NotAcceptableStatusException;
import ru.buzynnikov.notemanager.dto.NoteDTO;
import ru.buzynnikov.notemanager.exceptions.NotFoundNoteException;
import ru.buzynnikov.notemanager.models.Note;
import ru.buzynnikov.notemanager.repositories.NoteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    /*
        Метод сохранения заметки в базе данных
     */
    public Note saveNote(NoteDTO note){
        return noteRepository.save(createNote(note));
    }
    /*
        Метод получения заметок из базы данных
     */
    public List<Note> getAllNotes(){
        return noteRepository.findAll();
    }
    /*
        Метод получения заметки по id
        Возвращает Optional, так как заметка может отсутсвовать
     */
    public Optional<Note> getNoteById(Long id){
        return noteRepository.findById(id);
    }
    /*
        Метод обновления заметки
        1. С помощью StreamApi ищем заметку по id
        1.1. В случае, если не находим, появляется ошибка
        2. Изменяем параметры заметки на полученные из запроса
        3. Сохранаем заметку в БД
     */
    public Note updateNote(Long id, NoteDTO note) {
        Note targetNote = noteRepository.findAll()
                .stream()
                .filter(x -> x.getId().equals(id))
                .findFirst().orElseThrow(NotFoundNoteException::new);
        targetNote.setTitle(note.getTitle());
        targetNote.setDescription(note.getDescription());
        return noteRepository.save(targetNote);
    }
    /*
        Удаляем заметку по id
     */
    public void deleteNote(Long id){
        noteRepository.deleteById(id);
    }
    /*
        Метод создания новой заметки
     */
    private Note createNote(NoteDTO note){
        Note newNote = new Note();
        newNote.setTitle(note.getTitle());
        newNote.setDescription(note.getDescription());
        newNote.setCreateTime(LocalDateTime.now());
        return newNote;
    }
}
