package com.example.store.service.implement;

import com.example.store.dto.CartItemResponseDTO;
import com.example.store.dto.OrderPaymentDTO;
import com.example.store.dto.OrderRequestDTO;
import com.example.store.dto.OrderResponseDTO;
import com.example.store.entity.*;
import com.example.store.exception.*;
import com.example.store.mapper.IOrderMapper;
import com.example.store.mapper.VnPayMapper;
import com.example.store.mapper.impl.CartItemMapperImpl;
import com.example.store.repository.*;
import com.example.store.service.IOrderService;
import com.example.store.util.PaginationAndSortingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements IOrderService {

    private final CartItemMapperImpl cartItemMapperImpl;
    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final IAddressRepository addressRepository;
    private final IOrderRepository orderRepository;
    private final ICartItemRepository cartItemRepository;
    private final IOrderMapper orderMapper;
    private final IUserRepository userRepository;
    private final IStatusOrderRepository statusOrderRepository;
    private final VnPayMapper vnPayMapper;
    private final IProductRepository productRepository;

    public OrderServiceImpl(IAddressRepository addressRepository, IOrderRepository orderRepository, ICartItemRepository cartItemRepository, IOrderMapper orderMapper, IUserRepository userRepository, IStatusOrderRepository statusOrderRepository, VnPayMapper vnPayMapper, IProductRepository productRepository, CartItemMapperImpl cartItemMapperImpl) {
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderMapper = orderMapper;
        this.userRepository = userRepository;
        this.statusOrderRepository = statusOrderRepository;
        this.vnPayMapper = vnPayMapper;
        this.productRepository = productRepository;
        this.cartItemMapperImpl = cartItemMapperImpl;
    }

    @Transactional
    @Override
    public OrderResponseDTO order(String username, OrderRequestDTO orderRequestDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Not found " + username);
        }
        User user = optionalUser.get();
        Cart cart = user.getCart();
        List<CartItem> cartItems = cartItemRepository.findAllByCartIdAndIsDeleted(cart.getId());
        if (cartItems.isEmpty()) {
            throw new CartItemNotFoundException("Cart item not found");
        }
        Optional<Address> optionalAddress = addressRepository.findByIdAndUserId(orderRequestDTO.getAddressId(), user.getId());
        if (optionalAddress.isEmpty()) {
            throw new AddressNotFoundException("Not found " + orderRequestDTO.getAddressId());
        }
        Address address = optionalAddress.get();
        // Setup for new order
        Order order = new Order();
        order.setAddress(address);
        order.setName(orderRequestDTO.getName());
        order.setPhone(orderRequestDTO.getPhone());
        order.setDeliveryTime(ZonedDateTime.now());
        order.setCartItem(cartItems);
        // Total Price
        double totalPrices = 0.0;
        for (CartItem item : cartItems) {
//            logger.info("Cart item: {}, {}", item.getId(), item.getTotalPrice());
            item.setDeleted(true); item.setOrder(order);
            totalPrices += item.getTotalPrice();
        } order.setTotalPrice(totalPrices);

        Optional<StatusOrder> statusOrder = statusOrderRepository.findById(1L);
        order.setStatusOrder(statusOrder.get());
        order.setUser(user);
        user.getOrders().add(order);
        orderRepository.save(order);
        // Remove to available product
        for (CartItem item : cartItems) {
            Long proId = item.getProduct().getId();
            int quantity = item.getQuantity();
            Optional<Product> optionalProduct = productRepository.findById(proId);
            if (optionalProduct.isEmpty()) {
                throw new ProductNotFoundException("Not found " + proId);
            } Product product = optionalProduct.get();
            if (product.getAvailable() < quantity) {
                throw new VariantProductNotFoundException("Quantity not enough");
            }
            logger.info("Product before: {} - {}",product.getId(), product.getAvailable());
            product.setAvailable(product.getAvailable() - quantity);
            logger.info("Product after: {} - {} - {}",product.getId(), product.getAvailable(), quantity);
            productRepository.save(product);
        }
        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    @Override
    public OrderResponseDTO orderPayment(String username, OrderPaymentDTO orderPaymentDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Not found " + username);
        }
        User user = optionalUser.get();
        Cart cart = user.getCart();
        List<CartItem> cartItems = cartItemRepository.findAllByCartIdAndIsDeleted(cart.getId());
        if (cartItems.isEmpty()) {
            throw new CartItemNotFoundException("Cart item not found");
        }
        Optional<Address> optionalAddress = addressRepository.findByIdAndUserId(orderPaymentDTO.getAddressId(), user.getId());
        if (optionalAddress.isEmpty()) {
            throw new AddressNotFoundException("Not found address ");
        }
        Address address = optionalAddress.get();
        // Setup for new order
        Order order = new Order();
        order.setAddress(address);
        order.setName(orderPaymentDTO.getName());
        order.setUser(user);
        order.setPhone(orderPaymentDTO.getPhone());
        order.setDeliveryTime(ZonedDateTime.now());
        order.setCartItem(cartItems);

        // Total Price
        double totalPrices = 0.0;
        for (CartItem item : cartItems) {
            totalPrices += item.getTotalPrice();
        }
        order.setTotalPrice(totalPrices);

        // Delete cart item in cart
        for (CartItem item : cartItems) {
            item.setDeleted(true);
            item.setOrder(order);
        }
        order.setCartItem(cartItems);
        Optional<StatusOrder> statusOrder = statusOrderRepository.findById(1L);
        order.setStatusOrder(statusOrder.get());

        user.getOrders().add(order);

        // Payment
        VnPayInfo vnPayInfo = new VnPayInfo();
        vnPayInfo.setVnpAmount(orderPaymentDTO.getVnpAmount());
        vnPayInfo.setVnpBankCode(orderPaymentDTO.getVnpBankCode());
        vnPayInfo.setVnpTransactionNo(orderPaymentDTO.getVnpTransactionNo());
        vnPayInfo.setVnpOrderInfo(orderPaymentDTO.getVnpOrderInfo());
        vnPayInfo.setVnpSecurityHash(orderPaymentDTO.getVnpSecureHash());
        vnPayInfo.setVnpPayDate(orderPaymentDTO.getVnpPayDate());
        vnPayInfo.setVnpTxnRef(orderPaymentDTO.getVnpTxnRef());

        order.setVnPayInfo(vnPayInfo);
        orderRepository.save(order);
        logger.info("PayInfo: {}, {}", vnPayInfo.getVnpTxnRef(), vnPayInfo.getVnpTransactionNo());
        // Remove to available product
        for (CartItem item : cartItems) {
            Long proId = item.getProduct().getId();
            Optional<Product> optionalProduct = productRepository.findById(proId);
            if (optionalProduct.isEmpty()) {
                throw new ProductNotFoundException("Not found " + proId);
            }
            Product product = optionalProduct.get();
            if (product.getAvailable() < item.getQuantity()) {
                throw new VariantProductNotFoundException("Quantity not enough");
            }
            product.setAvailable(product.getAvailable() - item.getQuantity());
            productRepository.save(product);
        }
        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    @Override
    public List<OrderResponseDTO> getAllOrder(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Pageable pageable = PaginationAndSortingUtils.getPageable(pageNo, pageSize, sortBy, sortBy);
        Page<Order> orderPage = orderRepository.findAll(pageable);
        List<Order> orderList = orderPage.getContent();
        return orderMapper.toResponseDTOs(orderList);
    }

    @Transactional
    @Override
    public OrderResponseDTO setStatusOrder(Long orderId, Long statusId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new OrderNotFoundException("Not found " + orderId);
        }
        Order order = optionalOrder.get();
        Optional<StatusOrder> optionalStatusOrder = statusOrderRepository.findById(statusId);
        if (optionalStatusOrder.isEmpty()) {
            throw new StatusOrderNotFoundException("Status order not found");
        }
        if (order.getStatusOrder().getId() == 6) {
            throw new StatusOrderOnChangeException("Status order is Cancelled.");
        }
        order.setStatusOrder(optionalStatusOrder.get());
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(savedOrder);
    }

    @Override
    public List<CartItemResponseDTO> getDetailOrder(String username, Long orderId) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Not found " + username);
        } User user = optionalUser.get();
        List<CartItem> detailOrders = orderRepository.findCartItemsByOrderAndUsername(orderId);
        if(detailOrders == null) {
            throw new OrderNotFoundException("Not found the order " + orderId);
        } List<CartItemResponseDTO> dtoList = cartItemMapperImpl.toResponseDTOs(detailOrders);
        return dtoList;
    }

    @Override
    public List<CartItemResponseDTO> getDetailOrderAdmin(Long orderId) {
        List<CartItem> detailOrders = orderRepository.findCartItemsByOrderAndUsername(orderId);
        if(detailOrders == null) {
            throw new OrderNotFoundException("Not found the order " + orderId);
        } List<CartItemResponseDTO> dtoList = cartItemMapperImpl.toResponseDTOs(detailOrders);
        return dtoList;
    }

    @Transactional
    @Override
    public List<OrderResponseDTO> getOrderByUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Not found " + username);
        }
        User user = optionalUser.get();
        List<Order> orderList = user.getOrders();
        List<OrderResponseDTO> orderResponseDTOList = orderMapper.toResponseDTOs(orderList);
        return orderResponseDTOList;
    }

    @Transactional
    @Override
    public boolean cancelOrder(Long idOrder, String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Not found " + username);
        }
        User user = optionalUser.get();
        Optional<Order> optionalOrder = orderRepository.findByIdAndUserId(idOrder, user.getId());
        if (optionalOrder.isEmpty()) {
            throw new OrderNotFoundException("Not found order " + idOrder);
        }
        Order order = optionalOrder.get();
        String currStatus = order.getStatusOrder().getName();
        Long currStatusId = order.getStatusOrder().getId();
        // Update order status
        if (currStatusId == 1L) {
            Optional<StatusOrder> optionalStatusOrder = statusOrderRepository.findById(6L);
            if (optionalStatusOrder.isEmpty()) {
                throw new OrderNotFoundException("Not found order status ");
            }
            order.setStatusOrder(optionalStatusOrder.get());
            Order savedOrder = orderRepository.save(order);
            return true;
        }
        throw new StatusOrderOnChangeException("Can't cancel order. Current status is " + currStatus);
    }

    @Transactional
    @Override
    public boolean receivedProduct(Long idOrder, String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Not found " + username);
        }
        User user = optionalUser.get();
        Optional<Order> optionalOrder = orderRepository.findByIdAndUserId(idOrder, user.getId());
        if (optionalOrder.isEmpty()) {
            throw new OrderNotFoundException("Not found order " + idOrder);
        }
        Order order = optionalOrder.get();
        String currStatus = order.getStatusOrder().getName();
        Long currStatusId = order.getStatusOrder().getId();
        // Update order status
        if (currStatusId == 4L) {
            Optional<StatusOrder> optionalStatusOrder = statusOrderRepository.findById(5L);
            if (optionalStatusOrder.isEmpty()) {
                throw new OrderNotFoundException("Not found order status ");
            }
            order.setStatusOrder(optionalStatusOrder.get());
            Order savedOrder = orderRepository.save(order);
            return true;
        }
        throw new StatusOrderOnChangeException("Can't change order. Current status is " + currStatus);
    }
}
