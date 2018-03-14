package server.springcontext.inpoint

import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.RequestMapping
import server.springcontext.utilities.JSON

@RestController
class RequestController {
    //This is an Example of how to Respond with Plain Data
    //    @CrossOrigin
    @RequestMapping(value = ["/greeting"], method = [RequestMethod.GET], produces = ["application/json"])
    fun greetingForm(model: Model):String{
//        println(JSON.stringify(model))
        return "greeting"
    }
    //This is an Example of how to Respond with Pages
    //    @CrossOrigin
    @RequestMapping(value = ["/","/index","/index.html","/home"])
    fun index(model: Model):ModelAndView{
        val modelAndView = ModelAndView()
        modelAndView.viewName = "index"
        return modelAndView
    }
    //This is used to get a csrf for same origin authentication
    //    @CrossOrigin
    @RequestMapping("/csrf")
    fun csrf(token: CsrfToken): CsrfToken {
        return token
    }
}