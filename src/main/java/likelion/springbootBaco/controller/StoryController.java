package likelion.springbootBaco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("egg")
public class StoryController {

    @GetMapping("story")
    public String story(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "story";
    }
}
