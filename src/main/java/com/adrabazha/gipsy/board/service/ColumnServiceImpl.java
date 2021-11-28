package com.adrabazha.gipsy.board.service;

import com.adrabazha.gipsy.board.domain.BoardColumn;
import com.adrabazha.gipsy.board.exception.GeneralException;
import com.adrabazha.gipsy.board.repository.ColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnServiceImpl implements ColumnService {

    private ColumnRepository columnRepository;

    @Autowired
    public ColumnServiceImpl(ColumnRepository columnRepository) {
        this.columnRepository = columnRepository;
    }

    @Override
    public BoardColumn create(Object form) {
        return null;
    }

    @Override
    public List<BoardColumn> findAll() {
        return columnRepository.findAll();
    }

    @Override
    public BoardColumn findById(Long id) {
        return columnRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Column not found"));
    }

    @Override
    public void deleteById(Long id) {
        columnRepository.deleteById(id);
    }

    @Override
    public BoardColumn updateById(Long id, Object form) {
        return null;
    }
}
