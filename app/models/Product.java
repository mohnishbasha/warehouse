package models;
import play.data.validation.Constraints;
import play.libs.F;
import play.mvc.PathBindable;
import play.mvc.QueryStringBindable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.*;
import utils.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import java.lang.annotation.*;
import javax.validation.*;


public class Product implements PathBindable<Product>,
        QueryStringBindable<Product> {

    public interface Step1 {}
    public interface Step2 {}


    @Constraints.Required(groups = Step1.class)
    @EAN
    public String ean;

    @Constraints.Required(groups = Step2.class)
    public String name;

    public String description;

    public List<Tag> tags = new LinkedList<Tag>();


    @DateFormat("yyyy-MM-dd")
    public Date peremptionDate = new Date();


    public Product() {

    }

    public Product (String ean, String name, String description) {
        this.ean = ean;
        this.name = name;
        this.description = description;
    }

    public String toString() {
        return String.format("%s - %s", ean, name);
    }


    private static List<Product> products;

    static {
        products = new ArrayList<Product>();
        products.add(new Product("1", "Paper Clips 1", "Paper Clips Description 1"));
        products.add(new Product("2", "Paper Clips 2", "Paper Clips Description 2"));
        products.add(new Product("3", "Paper Clips 3", "Paper Clips Description 3"));
        products.add(new Product("4", "Paper Clips 4", "Paper Clips Description 4"));
        products.add(new Product("5", "Paper Clips 5", "Paper Clips Description 5"));
        products.add(new Product("6", "Paper Clips 6", "Paper Clips Description 6"));
        products.add(new Product("7", "Paper Clips 7", "Paper Clips Description 7"));

    }

    public static List<Product> findAll() {
        return new ArrayList<Product>(products);
    }

    public static Product findByEan(String ean) {
        for (Product candidate: products)
        {
            if (candidate.ean.equals(ean)) {
                return  candidate;
            }
        }
        return null;
    }

    public static List<Product> findByName(String term) {
        final List<Product> results = new ArrayList<Product>();

        for (Product candidate: products)
        {
            if (candidate.name.toLowerCase().contains(term.toLowerCase())) {
                results.add(candidate);
            }
        }
        return results;
    }

    public static boolean remove(Product product) {
        return products.remove(product);
    }

    public void save() {
        products.remove(findByEan(this.ean));
        products.add(this);
    }

    @Override
    public Product bind(String key, String value) {
        return findByEan(value);
    }

    @Override
    public F.Option<Product> bind(String key, Map<String, String[]> data) {
        return F.Option.Some(findByEan(data.get("ean")[0]));
    }

    @Override
    public String unbind(String s) {
        return this.ean;
    }

    @Override
    public String javascriptUnbind() {
        return this.ean;
    }

    public static class EanValidator extends Constraints.Validator<String> implements ConstraintValidator<EAN, String> {
        final static public String message = "Minimum 3 digits";

        public EanValidator() {}

        @Override
        public void initialize(EAN constraintAnnotation) {}

        @Override
        public boolean isValid(String value) {
            String pattern = "^[0-9]{3}$";
            return value != null && value.matches(pattern);
        }

        @Override
        public F.Tuple<String, Object[]> getErrorMessageKey() {
            return new F.Tuple<String, Object[]>(message,
                    new Object[]{});
        }
    }

    @Target({FIELD})
    @Retention(RUNTIME)
    @Constraint(validatedBy = EanValidator.class)
    @play.data.Form.Display(name="constraint.ean", attributes={"value"})
    public static @interface EAN {
        String message() default EanValidator.message;
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }


}
