package cl.ecr.test.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ThyController 
{
   @RequestMapping(value = "/index")
   public String index() 
   {
      return "index";
   }
}