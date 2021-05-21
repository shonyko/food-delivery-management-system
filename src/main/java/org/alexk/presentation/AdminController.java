package org.alexk.presentation;

import lombok.val;
import org.alexk.business.DeliveryService;
import org.alexk.business.IDeliveryServiceProcessing;
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

@Controller
@RequestMapping("/admin")
public class AdminController extends ControllerBase {
    IDeliveryServiceProcessing service = DeliveryService.instance;

    @GetMapping("/import")
    public ModelAndView getImportView(HttpSession session, Model model) {
        model.addAttribute("success", true);

        model.addAttribute("data", getView("/admin/adminImport", new ModelMap()));

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/manage")
    public ModelAndView getManageView(HttpSession session, Model model) {
        model.addAttribute("success", true);

        model.addAttribute("data", getView("/admin/adminManage", new ModelMap()));

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/reports")
    public ModelAndView getReportsView(HttpSession session, Model model) {
        model.addAttribute("success", true);

        model.addAttribute("data", getView("/admin/adminReports", new ModelMap()));

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/report/{id}")
    public ModelAndView getReport1Modal(HttpSession session, Model model, @PathVariable int id) {
        model.addAttribute("success", true);

        val modelMap = new ModelMap();
        val fields = new ArrayList<Field>();

        modelMap.addAttribute("title", "Generate order");
        modelMap.addAttribute("fields", fields);

        switch (id) {
            case 1:
                fields.add(new Field("Start Hour", "startHour"));
                fields.add(new Field("End Hour", "endHour"));

                fields.forEach(field -> {
                    field.setType(FieldType.DATETIME.name());
                    field.setFormat("hh:mm a");
                });
                break;
            case 2:
                fields.add(new Field("Min. no.", "minNo"));
                break;
            case 3:
                fields.add(new Field("Min. orders", "minOrders"));
                fields.add(new Field("Min. value", "minValue"));
                break;
            case 4:
                fields.add(new Field("Day", "day"));

                fields.forEach(field -> {
                    field.setType(FieldType.DATETIME.name());
                    field.setFormat("MM/DD/YYYY");
                });
                break;
        }

        model.addAttribute("data", getView("modal", modelMap));

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/base/add")
    public ModelAndView getBaseProductAddModal(HttpSession session, Model model) {
        model.addAttribute("success", true);

        val modelMap = new ModelMap();
        val fields = new ArrayList<Field>();

        modelMap.addAttribute("title", "Add Base Product");
        modelMap.addAttribute("fields", fields);

        fields.add(new Field("Titlu", "title"));
        fields.add(new Field("Rating", "rating"));
        fields.add(new Field("Calorii", "calories"));
        fields.add(new Field("Proteine", "protein"));
        fields.add(new Field("Grasimi", "fat"));
        fields.add(new Field("Sodium", "sodium"));
        fields.add(new Field("Pret", "price"));

        model.addAttribute("data", getView("modal", modelMap));

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/base/add/{id}")
    public ModelAndView getBaseProductUpdateModal(HttpSession session, Model model, @PathVariable int id) {
        model.addAttribute("success", true);

        val modelMap = new ModelMap();
        val fields = new ArrayList<Field>();

        modelMap.addAttribute("title", "Update Base Product");
        modelMap.addAttribute("fields", fields);

        val product = service.getMenuItem(id);

        fields.add(new Field("Titlu", "title", product.getTitle()));
        fields.add(new Field("Rating", "rating", Double.toString(product.getRating())));
        fields.add(new Field("Calorii", "calories", Double.toString(product.getCalories())));
        fields.add(new Field("Proteine", "protein", Double.toString(product.getProtein())));
        fields.add(new Field("Grasimi", "fat", Double.toString(product.getFat())));
        fields.add(new Field("Sodium", "sodium", Double.toString(product.getSodium())));
        fields.add(new Field("Pret", "price", Double.toString(product.computePrice())));

        model.addAttribute("data", getView("modal", modelMap));

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/composite/add")
    public ModelAndView getCompositeProductAddModal(HttpSession session, Model model) {

        model.addAttribute("success", true);

        val modelMap = new ModelMap();
        val fields = new ArrayList<Field>();

        modelMap.addAttribute("title", "Add Composite Product");
        modelMap.addAttribute("url", "/api/admin/");
        modelMap.addAttribute("fields", fields);

        fields.add(new Field("Titlu", "title"));
        fields.add(new Field("Rating", "rating"));

        model.addAttribute("data", getView("/admin/compositeProductsModal", modelMap));

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/composite/add/{id}")
    public ModelAndView getCompositeProductUpdateModal(HttpSession session, Model model, @PathVariable int id) {
        model.addAttribute("success", true);

        val modelMap = new ModelMap();
        val fields = new ArrayList<Field>();

        modelMap.addAttribute("title", "Update Composite Product");
        modelMap.addAttribute("url", "/api/admin/" + id);
        modelMap.addAttribute("fields", fields);

        val product = service.getMenuItem(id);

        fields.add(new Field("Titlu", "title", product.getTitle()));
        fields.add(new Field("Rating", "rating", Double.toString(product.getRating())));

        model.addAttribute("data", getView("/admin/compositeProductsModal", modelMap));

        return new ModelAndView(new MappingJackson2JsonView());
    }
}
