package be.unamur.webapp.spring.basics.dataaccess.repository;

import be.unamur.webapp.spring.basics.dataaccess.entity.Todo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JdbcTest
@TestPropertySource(locations = {"classpath:context_test_h2.properties"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {TodoRepositoryJdbcImpl.class})
public class TodoRepositoryJdbcImplTest {

    @Autowired
    private TodoRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void findByAuthor_should_not_find_anything_because_the_table_is_empty() {
        assertThat(repository.findByAuthorId(3241L)).isEmpty();
    }

    @Test
    public void create_should_insert_given_todo_and_assign_an_id_automatically() {
        jdbcTemplate.update("INSERT INTO t_author (id, username, password) VALUES (42, 'testauthor', 'pass')");

        repository.create("blabla", 42L);

        List<Todo> todo = repository.findByAuthorId(42L);

        assertThat(todo).hasSize(1);
        assertThat(todo.get(0).getId()).isPositive();
        assertThat(todo.get(0).getAuthorId()).isEqualTo(42L);
        assertThat(todo.get(0).getContent()).isEqualTo("blabla");
    }

}
