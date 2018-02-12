package be.kdg.blog.controllers;

import be.kdg.blog.model.Blog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;


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

    @GetMapping(value="/blog", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getBlog() {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>  <body>");
        html.append("<h1> My Blog </h1>");
        for(int i = 0; i < blog.getEntries().size();i++) {

            html.append("<span>");
            // Maken de header van de blog
            html.append("<h2>" + blog.getEntries().get(i).getSubject() + "</h2>");

            //Voegen de tags toe
            html.append("<p>"+blog.getTags().get(i).getName()+"</p>");
            // Voegen de tekst toe
            html.append("<p>"+blog.getEntries().get(i).getMessage()+"</p>");

            // Voegen de datum toe
            html.append("<p>"+blog.getEntries().get(i).getDateTime()+"</p>");

            html.append("</span>");
        }
        html.append("</body> </html>");

        return html.toString();
    }

    @GetMapping(value = "/api/blog/entries/{entryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getEntry(@PathVariable("entryId") int entryId) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Gaan er even vanuit dat de id overeenkomen met de volgorde waarin de entries worden toegevoegd.
        // Indien dit niet het geval, is lopen we over alle entries en kijken we waar de id gelijk is aan entryid.
        String json = gson.toJson(blog.getEntries().get(entryId-1));
        return json;
        //return new Gson().toJson(blog.getEntries().get(entryId));
    }

    @GetMapping(value = "/api/blog", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getEntryWithQueryString(@RequestParam("entryId") int entryId) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String json = gson.toJson(blog.getEntries().get(entryId-1));
        return json;
    }

    @GetMapping(value = "/api/blog/dynamic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> dynamic(@RequestParam("entryId") int entryId) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = null;
        HttpStatus status;

        // De Try catch is niet de mooiste oplossing. Beter zou zoals in het boek je een aparte private methode
        // schrijven die de blog entries doorzoeket en null teruggeeft als niets gevonden is. 
        try {
            json = gson.toJson(blog.getEntries().get(entryId - 1));

        }
        catch (Exception ex) {
            //No enryy is found
            json = null;
        }
        finally {
            status = json != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
            return new ResponseEntity<String>(json,status);
        }


    }


}
