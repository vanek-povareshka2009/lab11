// Main.java
package org.example;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Main {

    public static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration()
                    .addAnnotatedClass(Customer.class)
                    .addAnnotatedClass(Product.class)
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void main(String[] args) {
        try {
            UserAccess userAccess = new UserAccess(sessionFactory);

            Customer customer1 = new Customer("Vanechek");
            userAccess.addCustomer(customer1);

            Customer customer2 = new Customer("Sanechek");
            userAccess.addCustomer(customer2);

            Product product1 = new Product("кура", 10.00);
            userAccess.addProduct(product1, customer1);

            Product product2 = new Product("свинка", 20.00);
            userAccess.addProduct(product2, customer1);

            Product product3 = new Product("носорог", 30.00);
            userAccess.addProduct(product3, customer1);

            Product product4 = new Product("страус", 40.00);
            userAccess.addProduct(product4, customer2);

            Product product5 = new Product("апельсин", 9.99);
            userAccess.addProduct(product5, customer2);

            List<Customer> customers = userAccess.getAllCustomers();
            List<Product> products = userAccess.getAllProducts();

            System.out.println("All customers:");
            customers.forEach(c -> {
                System.out.println(c.getCustomerId() + ": " + c.getCustomerName());
                List<Product> customerProducts = userAccess.getProductsByCustomer(c);
                customerProducts.forEach(p -> System.out.println("   - " + p.getProductName() + " - " + p.getProductPrice()));
            });

            System.out.println("\nAll products:");
            products.forEach(p -> System.out.println(p.getProductId() + ": " + p.getProductName() + " - " + p.getProductPrice()));

        } finally {
            try {
                if (sessionFactory != null && !sessionFactory.isClosed()) {
                    sessionFactory.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}