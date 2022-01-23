package net.therap.leavemanagement.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author rumi.dipto
 * @since 1/23/22
 */
@Controller
public class ErrorHandlerController implements ErrorController {

    public static final String ERROR_PAGE = "/error";

    @RequestMapping("/error")
    private String showError(HttpServletRequest request, ModelMap modelMap) {
        modelMap.addAttribute("message", request.getAttribute("errorMessage"));

        return ERROR_PAGE;
    }
}
