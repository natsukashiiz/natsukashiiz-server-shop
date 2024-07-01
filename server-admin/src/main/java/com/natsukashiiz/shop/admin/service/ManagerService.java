package com.natsukashiiz.shop.admin.service;

import com.natsukashiiz.shop.admin.model.dto.ManagerDTO;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.Admin;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.ManagerException;
import com.natsukashiiz.shop.model.resposne.PageResponse;
import com.natsukashiiz.shop.repository.AdminRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor
public class ManagerService {

    private final AdminRepository adminRepository;
    private final AuthService authService;

    public PageResponse<List<ManagerDTO>> queryAllManagers(ManagerDTO request, PaginationRequest pagination) {
        Example<Admin> example = Example.of(request.toEntity(), ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase());
        Page<Admin> page = adminRepository.findAll(example, pagination);
        List<ManagerDTO> managers = page.getContent()
                .stream()
                .map(ManagerDTO::fromEntity)
                .collect(Collectors.toList());
        return new PageResponse<>(managers, page.getTotalElements());
    }

    public ManagerDTO queryManagerById(Long managerId) throws BaseException {
        Admin admin = authService.getAdmin();

        Optional<Admin> adminOptional = adminRepository.findById(managerId);
        if (!adminOptional.isPresent()) {
            log.warn("QueryManagerById-[block]:(manager not found). managerId:{}, adminId:{}", managerId, admin.getId());
            throw ManagerException.invalid();
        }

        return ManagerDTO.fromEntity(adminOptional.get());
    }

    public ManagerDTO createManager(ManagerDTO request) throws BaseException {
        Admin admin = authService.getAdmin();

        if (ObjectUtils.isEmpty(request.getUsername())) {
            log.warn("CreateManager-[block]:(username is empty). adminId:{}", admin.getId());
            throw ManagerException.invalidUsername();
        }

        if (ObjectUtils.isEmpty(request.getRole())) {
            log.warn("CreateManager-[block]:(role is empty). adminId:{}", admin.getId());
            throw ManagerException.invalidRole();
        }

        Admin entity = request.toEntity();
        Admin saved = adminRepository.save(entity);
        return ManagerDTO.fromEntity(saved);
    }

    public ManagerDTO updateManagerById(Long managerId, ManagerDTO request) throws BaseException {
        Admin admin = authService.getAdmin();

        Optional<Admin> adminOptional = adminRepository.findById(managerId);
        if (!adminOptional.isPresent()) {
            log.warn("UpdateManagerById-[block]:(manager not found). managerId:{}, adminId:{}", managerId, admin.getId());
            throw ManagerException.invalid();
        }

        Admin entity = adminOptional.get();
        entity.setUsername(request.getUsername());
        entity.setRole(request.getRole());

        Admin saved = adminRepository.save(entity);
        return ManagerDTO.fromEntity(saved);
    }

    public void deleteManagerById(Long managerId) throws BaseException {
        Admin admin = authService.getAdmin();

        boolean exists = adminRepository.existsById(managerId);
        if (!exists) {
            log.warn("DeleteManagerById-[block]:(manager not found). managerId:{}, adminId:{}", managerId, admin.getId());
            throw ManagerException.invalid();
        }

        adminRepository.deleteById(managerId);
    }
}
