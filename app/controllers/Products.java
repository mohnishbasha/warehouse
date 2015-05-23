package controllers;

import models.Product;
import models.Tag;
import play.data.Form;
import play.mvc.*;
import java.util.ArrayList;
import java.util.List;
import views.html.products.details;
import views.html.products.list;


public class Products extends Controller {

    private static final Form<Product> productForm = Form.form(Product.class);

    @BodyParser.Of(BodyParser.Json.class)
    public static Result index() {

        Http.RequestBody body = request().body();
        return ok("We expect to get json: " + body.asJson());
    }


    public static Result list(Integer page) {
        List<Product> products = Product.findAll();
        return ok(list.render(products));
    }

    public  static Result newProduct() {
        return ok(details.render(productForm));
    }


    public static Result details (Product product) {
        Form<Product> filledForm = productForm.fill(product);
        return ok(details.render(filledForm));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static Result save() {

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Form<Product> boundForm = productForm.bindFromRequest();
        if(boundForm.hasErrors()) {
            flash("error", "Please correct the form below.");
            return badRequest(details.render(boundForm));
        }

        Product product = boundForm.get();

        List<Tag> tags = new ArrayList<Tag>();
        for (Tag tag : product.tags) {
            if (tag.id != null) {
                tags.add(Tag.findById(tag.id));
            }
        }
        product.tags = tags;

        product.save();
        flash("success",
                String.format("Successfully added product %s", product));

        return redirect(routes.Products.list(1));
    }


    public static Result delete(String ean) {
        final Product product = Product.findByEan(ean);
        if(product == null) {
            return notFound(String.format("Product %s doesnt not exists.", ean));
        }

        Product.remove(product);
        return redirect(routes.Products.list(1));
    }

}
