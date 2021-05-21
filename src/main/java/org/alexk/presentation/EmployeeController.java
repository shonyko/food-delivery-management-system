package org.alexk.presentation;

import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/employee")
public class EmployeeController extends ControllerBase {
    @GetMapping("/orders")
    public ModelAndView getOrdersView(HttpSession session, Model model) {
        model.addAttribute("success", true);

        model.addAttribute("data", getView("employee/employeeOrders", new ModelMap()));

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/orders/{id}")
    public ModelAndView getOrderDetailsModal(HttpSession session, Model model, @PathVariable int id) {
        model.addAttribute("success", true);

        val modelMap = new ModelMap();
        modelMap.addAttribute("title", "Order Details");
        modelMap.addAttribute("url", "/api/employee/orders/" + id);
        model.addAttribute("data", getView("employee/orderDetailsModal", modelMap));

        return new ModelAndView(new MappingJackson2JsonView());
    }
}
