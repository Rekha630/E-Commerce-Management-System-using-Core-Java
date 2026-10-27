# E-Commerce Management System (Core Java, console-based)

## Run
    mkdir out
    javac -d out $(find src -name "*.java")      # Windows: dir /s /b src\*.java > files.txt && javac -d out @files.txt
    java -cp out main.ECommerceApplication

## Logins
- Admin: admin@shop.com / admin123
- Customer: choose "Register" first.
- Coupons: SAVE10 (10% off), FLAT500 (Rs.500 off on Rs.2000+)

## Packages
model (User, Customer, Admin, Product, Electronics, Clothing, Grocery, Cart, CartItem, Order, OrderStatus)
payment (Payment, UpiPayment, CardPayment, CashPayment)
discount (Discount, PercentageDiscount, FlatDiscount)
service (ProductService, CustomerService, CartService, OrderService, PaymentService)
exception (ProductNotFoundException, InsufficientStockException)
main (ECommerceApplication)
