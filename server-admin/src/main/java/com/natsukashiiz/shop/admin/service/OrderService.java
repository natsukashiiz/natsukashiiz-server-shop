package com.natsukashiiz.shop.admin.service;

import com.natsukashiiz.shop.admin.model.dto.OrderDTO;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.Order;
import com.natsukashiiz.shop.model.resposne.PageResponse;
import com.natsukashiiz.shop.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public PageResponse<List<OrderDTO>> queryAllOrders(OrderDTO request, PaginationRequest pagination) {
        Example<Order> example = Example.of(request.toEntity(), ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase());
        Page<Order> page = orderRepository.findAll(example, pagination);
        List<OrderDTO> categories = page.getContent()
                .stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
        return new PageResponse<>(categories, page.getTotalElements());
    }
}
