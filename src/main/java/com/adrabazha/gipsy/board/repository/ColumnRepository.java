package com.adrabazha.gipsy.board.repository;

import com.adrabazha.gipsy.board.domain.Column;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<Column, Long> {
}
