package service;

import exception.ProductNotFoundException;
import model.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductService {
    private final Map<Integer, Product> products = new LinkedHashMap<>();

    public ProductService() {   // sample data
        addProduct(new Electronics(101, "Laptop", 55000, 10, 24));
        addProduct(new Electronics(102, "Mobile", 20000, 25, 12));
        addProduct(new Electronics(103, "Headphones", 1500, 40, 6));
        addProduct(new Clothing(104, "T-Shirt", 999, 50, "M"));
        addProduct(new Clothing(105, "Jeans", 1799, 30, "32"));
        addProduct(new Grocery(106, "Rice", 65, 100, "1 kg"));
        addProduct(new Grocery(107, "Biscuits", 30, 200, "100 g"));
    }

    public void addProduct(Product p) {
        if (products.containsKey(p.getProductId()))
            throw new IllegalArgumentException("Product ID " + p.getProductId() + " already exists.");
        products.put(p.getProductId(), p);
    }

    public Product getProduct(int id) throws ProductNotFoundException {
        Product p = products.get(id);
        if (p == null) throw new ProductNotFoundException("Product " + id + " not found.");
        return p;
    }

    public List<Product> getAllProducts() { return new ArrayList<>(products.values()); }

    public List<Product> search(String keyword) {
        List<Product> result = new ArrayList<>();
        String k = keyword.toLowerCase();
        for (Product p : products.values())
            if (p.getName().toLowerCase().contains(k) || p.getCategory().toLowerCase().contains(k))
                result.add(p);
        return result;
    }

    public void removeProduct(int id) throws ProductNotFoundException {
        getProduct(id);
        products.remove(id);
    }

    public void updateName(int id, String name) throws ProductNotFoundException { getProduct(id).setName(name); }
    public void updatePrice(int id, double price) throws ProductNotFoundException { getProduct(id).setPrice(price); }
    public void updateStock(int id, int qty) throws ProductNotFoundException { getProduct(id).setQuantity(qty); }

    public void display(List<Product> list) {
        if (list.isEmpty()) { System.out.println("No products found."); return; }
        System.out.printf("%-5s %-15s %-12s %-13s %-6s %s%n", "ID", "Name", "Category", "Price", "Stock", "Details");
        System.out.println("-------------------------------------------------------------------------------");
        for (Product p : list) System.out.println(p);
    }
}
