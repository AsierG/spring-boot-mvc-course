package com.asierg.bootstrap;

import com.asierg.domain.*;
import com.asierg.domain.security.Role;
import com.asierg.enums.OrderStatus;
import com.asierg.services.ProductService;
import com.asierg.services.RoleService;
import com.asierg.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SpringJPABootstrap implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadProducts();
        loadUsersAndCustomers();
        loadCarts();
        loadOrderHistory();
        loadRoles();
        assignUsersToDefaultRole();
    }

    private void assignUsersToDefaultRole() {
        List<Role> roles = (List<Role>) roleService.listAll();
        List<User> users = (List<User>) userService.listAll();

        roles.forEach(role -> {
            if (role.getRole().equalsIgnoreCase("CUSTOMER")) {
                users.forEach(user -> {
                    user.addRole(role);
                    userService.saveOrUpdate(user);
                });
            }
        });
    }

    private void loadRoles() {
        Role role = new Role();
        role.setRole("CUSTOMER");
        roleService.saveOrUpdate(role);
    }

    private void loadOrderHistory() {
        List<User> users = (List<User>) userService.listAll();
        List<Product> products = (List<Product>) productService.listAll();

        users.forEach(user -> {
            Order order = new Order();
            order.setCustomer(user.getCustomer());
            order.setOrderStatus(OrderStatus.SHIPPED);

            products.forEach(product -> {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(product);
                orderDetail.setQuantity(1);
                order.addToOrderDetails(orderDetail);
            });
        });
    }

    private void loadCarts() {
        List<User> users = (List<User>) userService.listAll();
        List<Product> products = (List<Product>) productService.listAll();

        users.forEach(user -> {
            user.setCart(new Cart());
            CartDetail cartDetail = new CartDetail();
            cartDetail.setProduct(products.get(0));
            cartDetail.setQuantity(2);
            user.getCart().addCartDetail(cartDetail);
            userService.saveOrUpdate(user);
        });
    }

    public void loadUsersAndCustomers() {
        User user1 = new User();
        user1.setUsername("mlanda");
        user1.setPassword("1234");

        Customer customer1 = new Customer();
        customer1.setFirstName("Mikel");
        customer1.setLastName("Landa");
        customer1.setBillingAddress(new Address());
        customer1.getBillingAddress().setAddressLine1("Elkano 12");
        customer1.getBillingAddress().setCity("Galdakao");
        customer1.getBillingAddress().setState("Bizkaia");
        customer1.getBillingAddress().setZipCode("48960");
        customer1.setEmail("mikelgalda@gmail.com");
        customer1.setPhoneNumber("654123789");
        user1.setCustomer(customer1);
        userService.saveOrUpdate(user1);

        User user2 = new User();
        user2.setUsername("Dafne");
        user2.setPassword("9876");

        Customer customer2 = new Customer();
        customer2.setFirstName("Dafne");
        customer2.setLastName("Gonzalez");
        customer2.setBillingAddress(new Address());
        customer2.getBillingAddress().setAddressLine1("Baiona 24");
        customer2.getBillingAddress().setCity("Gernika");
        customer2.getBillingAddress().setState("Bizkaia");
        customer2.getBillingAddress().setZipCode("48753");
        customer2.setEmail("dafnegonzalez@gmail.com");
        customer2.setPhoneNumber("632145123");
        user2.setCustomer(customer2);
        userService.saveOrUpdate(user2);

        User user3 = new User();
        user3.setUsername("Gaizka");
        user3.setPassword("6321");
        Customer customer3 = new Customer();
        customer3.setFirstName("Gaizka");
        customer3.setLastName("Uriarte");
        customer3.setBillingAddress(new Address());
        customer3.getBillingAddress().setAddressLine1("Gran Via 52");
        customer3.getBillingAddress().setCity("Bilbao");
        customer3.getBillingAddress().setState("Bizkaia");
        customer3.getBillingAddress().setZipCode("48001");
        customer3.setEmail("gaizkauriarte@gmail.com");
        customer3.setPhoneNumber("664123879");

        user3.setCustomer(customer3);
        userService.saveOrUpdate(user3);
    }

    public void loadProducts() {

        Product product1 = new Product();
        product1.setDescription("Product 1");
        product1.setPrice(new BigDecimal("10.99"));
        product1.setImageUrl("http://example.com/product1");
        productService.saveOrUpdate(product1);

        Product product2 = new Product();
        product2.setDescription("Product 2");
        product2.setPrice(new BigDecimal("14.99"));
        product2.setImageUrl("http://example.com/product2");
        productService.saveOrUpdate(product2);

        Product product3 = new Product();
        product3.setDescription("Product 3");
        product3.setPrice(new BigDecimal("20.99"));
        product3.setImageUrl("http://example.com/product3");
        productService.saveOrUpdate(product3);

        Product product4 = new Product();
        product4.setDescription("Product 4");
        product4.setPrice(new BigDecimal("25.99"));
        product4.setImageUrl("http://example.com/product4");
        productService.saveOrUpdate(product4);

        Product product5 = new Product();
        product5.setDescription("Product 5");
        product5.setPrice(new BigDecimal("30.99"));
        product5.setImageUrl("http://example.com/product5");
        productService.saveOrUpdate(product5);

    }
}
