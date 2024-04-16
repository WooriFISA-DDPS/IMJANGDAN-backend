package imjangdan.ddps.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MapController {
	@GetMapping("/memomap")
	public String memoMapService(){
		return "/map/memomap";
	}
}
