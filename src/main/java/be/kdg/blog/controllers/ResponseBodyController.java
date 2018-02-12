package be.kdg.blog.controllers;

import be.kdg.blog.model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by Michael on 11/02/2018.
 */

@Controller
public class ResponseBodyController {

    private final Blog blog;

    @Autowired
    public ResponseBodyController(Blog blog) {
        this.blog = blog;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String sayHello() {
        return "Hello, World!";
    }

    @GetMapping(value = "/html", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String sayHelloHtml() {
        final String hello = "<html><head><title>Hello!</title></head><body><h1>Hello, world!</h1></body></html>";
        return hello;
    }

}
