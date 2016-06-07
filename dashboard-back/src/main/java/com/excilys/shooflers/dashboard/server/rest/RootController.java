package com.excilys.shooflers.dashboard.server.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Mickael on 07/06/2016.
 */
@RestController
public class RootController {
	@RequestMapping("/")
	void handleFoo(HttpServletResponse response) throws IOException {
		response.sendRedirect("swagger-ui.html");
	}
}
