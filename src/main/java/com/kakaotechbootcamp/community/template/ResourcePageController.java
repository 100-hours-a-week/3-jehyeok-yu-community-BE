package com.kakaotechbootcamp.community.template;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResourcePageController {

    @GetMapping("/privacy")
    public String getPrivacy(Model model) {
        model.addAttribute("serviceName", "게임 인사이드");
        model.addAttribute("companyRep", "카카오테크 부트캠프");
        model.addAttribute("contactEmail", "wpgur07@gmail.com");
        model.addAttribute("lastUpdated", "2025-10-26");
        return "privacy";
    }

    @GetMapping("/terms")
    public String getTerms(Model model) {
        model.addAttribute("serviceName", "게임 인사이드");
        model.addAttribute("companyRep", "카카오테크 부트캠프");
        model.addAttribute("contactEmail", "wpgur07@gmail.com");
        model.addAttribute("lastUpdated", "2025-10-26");
        return "terms";
    }
}
