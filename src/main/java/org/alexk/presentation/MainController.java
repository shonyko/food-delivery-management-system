package org.alexk.presentation;

import lombok.SneakyThrows;
import org.alexk.business.models.User;
import org.alexk.business.models.enums.UserType;
import org.alexk.data.FileReader;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpSession;

@Controller
public class MainController extends ControllerBase {
    @GetMapping
    public ModelAndView index(HttpSession session, Model model) {
        return new ModelAndView("login");
    }

    @GetMapping("/register")
    public ModelAndView register(HttpSession session, Model model) {
        return new ModelAndView("register");
    }

    @GetMapping("/app")
    public ModelAndView app(HttpSession session, Model model) {
        var acc = (User) session.getAttribute("user");

        if(acc == null) {
            return new ModelAndView("redirect:/");
        }

        if(acc.getType() == UserType.ADMIN) {
            model.addAttribute("content", "admin/import");
        }
        if(acc.getType() == UserType.CUSTOMER) {
            model.addAttribute("content", "customer/browse");
        }
        if(acc.getType() == UserType.EMPLOYEE) {
            model.addAttribute("content", "employee/orders");
        }

        model.addAttribute("userType", acc.getType().toString());

        return new ModelAndView("layout");
    }

    @SneakyThrows
    @PostMapping
    public String post(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        FileReader.getProducts(file.getInputStream());

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @MessageMapping("/notify")
    @SendTo("/employees")
    @ResponseBody
    public String chat(String message) {
        return "Hellow";
    }
}
