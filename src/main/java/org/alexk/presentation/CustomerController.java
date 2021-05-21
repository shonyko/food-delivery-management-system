package org.alexk.presentation;

import lombok.val;
import org.alexk.business.DeliveryService;
import org.alexk.business.IDeliveryServiceProcessing;
import org.alexk.business.models.BaseProduct;
import org.alexk.business.models.MenuItem;
import org.alexk.business.models.enums.FieldType;
import org.alexk.business.models.misc.Field;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Comparator;

@Controller
@RequestMapping("/customer")
public class CustomerController extends ControllerBase {
    IDeliveryServiceProcessing service = DeliveryService.instance;

    @GetMapping("/browse")
    public ModelAndView getBrowse(HttpSession session, Model model) {
        model.addAttribute("success", true);

        val menuItems = service.getMenuItems();

        val modelMap = new ModelMap();
        modelMap.addAttribute("scoutUrl", "/api/customer/scout");
        modelMap.addAttribute("dataUrl", "/api/customer/shop");
        modelMap.addAttribute("cartUrl", "/api/customer/cart");

        val mockObj = new BaseProduct();
        modelMap.addAttribute("maxRating", Math.ceil(menuItems.stream().max(Comparator.comparingDouble(MenuItem::getRating)).orElse(mockObj).getRating()));
        modelMap.addAttribute("maxCalories", Math.ceil(menuItems.stream().max(Comparator.comparingDouble(MenuItem::getCalories)).orElse(mockObj).getCalories()));
        modelMap.addAttribute("maxProtein", Math.ceil(menuItems.stream().max(Comparator.comparingDouble(MenuItem::getProtein)).orElse(mockObj).getProtein()));
        modelMap.addAttribute("maxFat", Math.ceil(menuItems.stream().max(Comparator.comparingDouble(MenuItem::getFat)).orElse(mockObj).getFat()));
        modelMap.addAttribute("maxSodium", Math.ceil(menuItems.stream().max(Comparator.comparingDouble(MenuItem::getSodium)).orElse(mockObj).getSodium()));
        modelMap.addAttribute("maxPrice", Math.ceil(menuItems.stream().max(Comparator.comparingDouble(MenuItem::computePrice)).orElse(mockObj).computePrice()));

        model.addAttribute("data", getView("customer/customerBrowse", modelMap));

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/cart")
    public ModelAndView getCart(HttpSession session, Model model) {
        model.addAttribute("success", true);

        model.addAttribute("data", getView("customer/customerCart", new ModelMap()));

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/product/{id}")
    public ModelAndView getProductDetailsModal(HttpSession session, Model model, @PathVariable int id) {
        model.addAttribute("success", true);

        val modelMap = new ModelMap();
        val fields = new ArrayList<Field>();
        modelMap.addAttribute("title", "Product Details");
        modelMap.addAttribute("url", "/api/customer/product/" + id);
        modelMap.addAttribute("fields", fields);

        try {
            val item = service.getMenuItem(id);
            fields.add(new Field(FieldType.SIMPLE.name(), "Nume", "name", item.getTitle(), "", true, false));
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Produsul nu exista!");
        }

        model.addAttribute("data", getView("customer/productDetailsModal", modelMap));

        return new ModelAndView(new MappingJackson2JsonView());
    }
}
