package club.beingsoft.sstt.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "words", uniqueConstraints = {@UniqueConstraint(columnNames = {"word_key"}, name = "unique_words_word_value_idx")})
public class Word {
    @Id
    private String word_key;

    @Column(name = "count", nullable = false)
    @NotNull
    private Integer count;

    public Word() {
    }

    public Word(String word_key, @NotNull Integer count) {
        this.word_key = word_key;
        this.count = count;
    }

    public String getWord_key() {
        return word_key;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
