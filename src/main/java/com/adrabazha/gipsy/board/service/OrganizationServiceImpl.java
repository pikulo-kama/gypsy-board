package com.adrabazha.gipsy.board.service;

import com.adrabazha.gipsy.board.domain.Organization;
import com.adrabazha.gipsy.board.exception.GeneralException;
import com.adrabazha.gipsy.board.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Organization create(Object form) {
        return null;
    }

    @Override
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    @Override
    public Organization findById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Organization not found"));
    }

    @Override
    public void deleteById(Long id) {
        organizationRepository.deleteById(id);
    }

    @Override
    public Organization updateById(Long id, Object form) {
        return null;
    }
}
