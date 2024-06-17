package com.example.store.controllers;

import com.example.store.dto.CartItemResponseDTO;
import com.example.store.dto.OrderPaymentDTO;
import com.example.store.dto.OrderRequestDTO;
import com.example.store.dto.OrderResponseDTO;
import com.example.store.entity.CartItem;
import com.example.store.exception.CartItemNotFoundException;
import com.example.store.exception.UserNotFoundException;
import com.example.store.service.IOrderService;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    private static Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/user/orders")
    public ResponseEntity<?> getOrdersByUser(Authentication auth) {
        try {
            String username = auth.getName();
            List<OrderResponseDTO> orderResponseDTOs = orderService.getOrderByUser(username);
//            logger.info("Item of Order: {}",orderResponseDTOs.size());
            return new ResponseEntity<>(orderResponseDTOs, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/order/payment-vnPay")
    public ResponseEntity<?> orderPaymentVnPay(Authentication auth,
                                               @RequestBody OrderPaymentDTO orderPaymentDTO) {
        try {
            String username = auth.getName();
            return new ResponseEntity<>(orderService.orderPayment(username, orderPaymentDTO), HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (CartItemNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/detail-order/{id}")
    public ResponseEntity<?> getOrderDetail(Authentication auth, @PathVariable("id") Long orderId){
        try {
            logger.info("Order ID: " + orderId);
            String username = auth.getName();
            List<CartItemResponseDTO> detailOrders = orderService.getDetailOrder(username, orderId);
            return new ResponseEntity<>(detailOrders, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admin/detail-order/{id}")
    public ResponseEntity<?> getAdminOrderDetail(Authentication auth, @PathVariable("id") Long orderId){
        try {
            logger.info("Order ID: " + orderId);
            List<CartItemResponseDTO> detailOrders = orderService.getDetailOrderAdmin(orderId);
            return new ResponseEntity<>(detailOrders, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/order")
    public ResponseEntity<?> order(Authentication auth, @RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            String username = auth.getName();
            return new ResponseEntity<>(orderService.order(username, orderRequestDTO), HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (CartItemNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/order/update-status")
    public ResponseEntity<?> setOrderStatus(@RequestParam("orderId") Long orderId,
                                            @RequestParam("statusOrderId") Long statusOrderId) {
        try {
            return new ResponseEntity<>(orderService.setStatusOrder(orderId, statusOrderId), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (CartItemNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admin/orders")
    public ResponseEntity<?> getAllOrders(@RequestParam(value = "pageNo", defaultValue = "0")int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = "10")int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = "id")String sortBy,
                                          @RequestParam(value = "sortDir", defaultValue = "asc")String sortDir) {
        try {
            return new ResponseEntity<>(orderService.getAllOrder(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (CartItemNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user/cancel-order")
    public ResponseEntity<?> cancelOrder(Authentication auth, @RequestParam("id") Long orderId) {
        try {
            String username = auth.getName();
            return new ResponseEntity<>(orderService.cancelOrder(orderId, username), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user/received-order")
    public ResponseEntity<?> receivedOrder(Authentication auth, @RequestParam("id") Long orderId) {
        try {
            String username = auth.getName();
            return new ResponseEntity<>(orderService.receivedProduct(orderId, username), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
