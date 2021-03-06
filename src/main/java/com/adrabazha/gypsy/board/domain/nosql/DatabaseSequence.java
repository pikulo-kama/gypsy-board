package com.adrabazha.gypsy.board.domain.nosql;

import com.adrabazha.gypsy.board.domain.DatabaseEntityConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = DatabaseEntityConstant.MONGO_SEQUENCES)
public class DatabaseSequence {

    @Id
    private String id;

    private long seq;
}
