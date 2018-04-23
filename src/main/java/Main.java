import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.*;
import org.eclipse.jetty.http.HttpStatus;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import java.util.List;
import static java.lang.Integer.parseInt;
import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        //logger
        logger.info("Server running...");

        // default server settings
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFileLocation("/public");
        port(8888);

        // populate some data for the memory storage
        //populateData();

        // Always start with more specific routes

        get("/fail", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render(ProductController.renderProducts(req, res, "product/fail"));
        });

        get("/", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render(ProductController.renderProducts(req, res, "product/index"));
        });
        post("/add-to-cart/:product-id", (Request req, Response res) -> {
            OrderDao orderDataStore = OrderDaoJDBC.getInstance();
            ProductDao productDataStore = ProductDaoJDBC.getInstance();
            LineItemDao lineItemDataStore = LineItemDaoJDBC.getInstance();
            Order order = orderDataStore.getByUser(req.session().attribute("user"));
            int requestedId;
            try {
                requestedId = parseInt(req.params(":product-id"));
            } catch (NumberFormatException e) {
                res.status(HttpStatus.SERVICE_UNAVAILABLE_503);
                return false;
            }
            List<LineItem> items = lineItemDataStore.getBy(order);
            Product product = productDataStore.find(requestedId);
            if (product == null) {
                res.status(HttpStatus.SERVICE_UNAVAILABLE_503);
                return false;
            }
            LineItem lineItem = new LineItem(0, product.getName(), product.getDefaultPrice(), 1, order, product);
            boolean isInList = false;
            for (LineItem item : items) {
                if (requestedId == item.getProduct().getId()) {
                    lineItemDataStore.increaseQuantity(item.getId());
                    isInList = true;
                    break;
                }
            }
            if (!isInList) {
                lineItemDataStore.add(lineItem);
            }
            return lineItemDataStore.getBy(order).size();
        });

        get("/cart", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render(ProductController.renderProducts(req, res, "product/cart"));
        });

        post("/delete/:prodid", (Request req, Response res) -> {
            OrderDao orderDataStore = OrderDaoJDBC.getInstance();
            LineItemDao lineItemDataStore = LineItemDaoJDBC.getInstance();
            int userId = req.session().attribute("user");
            Order order = orderDataStore.getByUser(userId);
            List<LineItem> lineItems = lineItemDataStore.getBy(order);
            int productId;
            try {
                productId = parseInt(req.params(":prodid"));
            } catch (NumberFormatException e) {
                res.status(HttpStatus.SERVICE_UNAVAILABLE_503);
                return false;
            }
            for (int i = 0; i < lineItems.size(); i++) {
                if (lineItems.get(i).getId() == productId) {
                    System.out.print(lineItems.get(i).getId());
                    lineItemDataStore.remove(lineItems.get(i).getId());
                }
            }
            return true;
        });

        post("/checkout", (Request req, Response res) -> {
            OrderDao orderDataStore = OrderDaoJDBC.getInstance();
            LineItemDao lineItemDataStore = LineItemDaoJDBC.getInstance();
            int userId = req.session().attribute("user");
            Order order = orderDataStore.getByUser(userId);
            List<LineItem> lineItems = lineItemDataStore.getBy(order);
            for (LineItem lineItem : lineItems) {
                lineItemDataStore.setQuantity(lineItem.getId(), parseInt(req.queryParams(lineItem.getId() + "quantity")));
            }
            return new ThymeleafTemplateEngine().render(ProductController.renderProducts(req, res, "product/checkout"));
        });

        post("/payment", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render(ProductController.renderProducts(req, res, "product/payment"));
        });
        post("/confirmation", (Request req, Response res) -> {
            OrderDao orderDao = OrderDaoJDBC.getInstance();
            UserDao userDao = UserDaoJDBC.getInstance();
            int userId = req.session().attribute("user");
            User user = userDao.find(userId);
            Order oldOrder = orderDao.getByUser(userId);
            orderDao.setStatus(oldOrder.getId(), "Payed");
            Order newOrder = new Order(0, "Order", "New", user);
            orderDao.add(newOrder);
            return new ThymeleafTemplateEngine().render(ProductController.renderProducts(req, res, "product/confirmation"));
        });
        post("/registerCheck/:username", (Request req, Response res) -> {
            UserDao userDao = UserDaoJDBC.getInstance();
            User user = userDao.getByName(req.params("username"));
            return user == null;
        });
        post("/register", (Request req, Response res) -> {
            String hashPassword = BCrypt.hashpw(req.queryParams("password"), BCrypt.gensalt());
            OrderDao orderDao = OrderDaoJDBC.getInstance();
            UserDao userDao = UserDaoJDBC.getInstance();
            User user = new User(0, req.queryParams("username"), req.queryParams("email"), hashPassword, "", "");
            userDao.add(user);
            User recalledUser = userDao.getByName(req.queryParams("username"));
            Order order = new Order(0, "Order", "New", recalledUser);
            orderDao.add(order);
            User newUser = userDao.getByName(req.queryParams("username"));
            req.session().attribute("user", newUser.getId());
            Mailer.send("codecoolershop@gmail.com", "kiskukac1234",req.queryParams("email"),"Welcome", "Hi,\nIt is so cool that you registered to our site. Have fun with the codecoolers!\nCheers,\nCodecoolerShop");
            res.redirect("/");
            return true;
        });
        post("/loginCheck/:username/:password", (Request req, Response res) -> {
            UserDao userDao = UserDaoJDBC.getInstance();
            User user = userDao.getByName(req.params("username"));
            if (user == null) return false;
            return BCrypt.checkpw(req.params("password"), user.getPassword());
        });
        post("/login", (Request req, Response res) -> {
            String userName = req.queryParams("loginUsername");
            UserDao userDao = UserDaoJDBC.getInstance();
            User user = userDao.getByName(userName);
            req.session().attribute("user", user.getId());
            res.redirect("/");
            return true;
        });

        get("/logout", (Request req, Response res) -> {
            req.session().removeAttribute("user");
            res.redirect("/");
            return true;
        });

        // Add this line to your project to enable the debug screen
        enableDebugScreen();
    }

    public static void populateData() {

        ProductDao productDataStore = ProductDaoJDBC.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
        SupplierDao supplierDataStore = SupplierDaoJDBC.getInstance();
        OrderDao orderDataStore = OrderDaoJDBC.getInstance();
        UserDao userDataStore = UserDaoJDBC.getInstance();

        //setting up a new supplier
        Supplier amazon = new Supplier(1, "Amazon", "Digital content and services");
        supplierDataStore.add(amazon);
        Supplier lenovo = new Supplier(2, "Lenovo", "Computers");
        supplierDataStore.add(lenovo);

        //setting up a new product category
        ProductCategory boys = new ProductCategory(1, "Boys", "Human Resource", "Codecoolers, who are boys.");
        productCategoryDataStore.add(boys);
        ProductCategory girls = new ProductCategory(2, "Girls", "Human Resource", "Codecoolers, who are girls.");
        productCategoryDataStore.add(girls);

        //setting up a new user
        User user = new User(1, "user", "string", "cucc", "mégegyucc", "jsduisd");
        userDataStore.add(user);


        //setting up a new shop cart
        Order order = new Order(0, "Order", "New", user);
        orderDataStore.add(order);

        //setting up products and printing it
        productDataStore.add(new Product(0, "Matykó (alias Gyuli)", 1000, "USD", "A true debug angel, cleans up your code in no time. Cute accent makes you want to hug him.", boys, amazon));
        productDataStore.add(new Product(0, "Áki the Bro", 599, "USD", "Best Scrum Master in the world, a true brother from another mother.", boys, lenovo));
        productDataStore.add(new Product(0, "Tipi", 111, "USD", "Humorous AF. Or at least tries to be, but gets the job done. ", boys, amazon));
        productDataStore.add(new Product(0, "Gabó", 0, "USD", "The priceless Queen of CodeCool. She will be your mistress for eternety.", girls, amazon));
        productDataStore.add(new Product(0, "Debi, the RedHead", 500, "USD", "Flaming spirit and hair. Basketball ace and IntelliJ lover.", girls, amazon));
        productDataStore.add(new Product(0, "Henrik, the 2nd Daddy", 750, "USD", "Family oriented, caring and has been sober for 1 year. What else do you need?", boys, amazon));
        productDataStore.add(new Product(0, "Bálint", 689, "USD", "Toned up sporty guy, never accepts failure. With him you bet on the winning side.", boys, amazon));
        productDataStore.add(new Product(0, "Sanyi", 569, "USD", "Businessman, the Wolf of CodeStreet. Reads your mind, and pulls the code from it.", boys, amazon));
        productDataStore.add(new Product(0, "Dani", 460, "USD", "Young, but ambitious, always on the hunt for work. Unless drinking in the pub down the street.", boys, amazon));
        productDataStore.add(new Product(0, "Puszedli, the Polyhistor", 999, "USD", "We are actually thinking about keeping him, we are in need of a good support like him. Also comes handy around the house.", boys, amazon));
        productDataStore.add(new Product(0, "Geri", 0, "USD", "We will even pay you, just take him already. Great material for memes or if you are searching for a drinking buddy.", boys, amazon));
        productDataStore.add(new Product(0, "Janó", 585, "USD", "Great workforce, just keep him near a tabletennis, he is kinda addicted. Never lets you down. ", boys, amazon));
        productDataStore.add(new Product(0, "Kristóf", 499, "USD", "Woah... Such elegance... Much Class... So drunk...", boys, amazon));
        productDataStore.add(new Product(0, "Attila, the 1st Daddy (Celeb)", 700, "USD", "Celeb on the stage, dad at home. Best choice for everyone.", boys, amazon));
        productDataStore.add(new Product(0, "Máté", 399, "USD", "The silent killer. Really kind, a huggable teddy bear. His beard is evergrowing.", boys, amazon));
        productDataStore.add(new Product(0, "Anikó", 769, "USD", "Good thinker, addictive personality, try to make that addiction work.", girls, amazon));
        productDataStore.add(new Product(0, "Attila, the Admin", 10000, "USD", "What is that in the sky? A bird? A plane? Noooo... It is Xattus Admin. You will not forget him.", boys, amazon));
        productDataStore.add(new Product(0, "Fruzsi", 560, "USD", "The Designer Queen, at work known as Professor Utonium, father of 3 lovely girls.", girls, amazon));
        productDataStore.add(new Product(0, "Bence, the Pro", 499, "USD", "The Designer King, Codewars Master, the most pro of the pros. Lives on Zing Burger, you will need a ton of that.", boys, amazon));
        productDataStore.add(new Product(0, "Soma", 499, "USD", "Easily gets sick, but his mind is capable of solving the mysteries of life. Also has some cute hats.", boys, amazon));
        productDataStore.add(new Product(0, "Gábor (Gabi)", 499, "USD", "Beautiful... Simply beautiful. Be aware, he is so much more than just a pretty face.", boys, amazon));
    }


}
